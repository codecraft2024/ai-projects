package com.twinzy.seed;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class WikimediaImageFetcher {

    private static final Logger log = LoggerFactory.getLogger(WikimediaImageFetcher.class);
    private static final String API = "https://commons.wikimedia.org/w/api.php";
    private static final String USER_AGENT = "TwinzySeed/1.0 (celebrity lookalike demo; twinzy@example.com)";

    private static final List<String> PORTRAIT_CATEGORIES = List.of(
            "Category:Portrait photographs",
            "Category:Head shots",
            "Category:Facial expressions",
            "Category:People of Africa",
            "Category:People of Asia",
            "Category:People of Europe",
            "Category:People of North America",
            "Category:People of South America",
            "Category:Male actors",
            "Category:Female actors",
            "Category:Models (people)",
            "Category:Beauty pageant winners"
    );

    private static final List<String> SEARCH_QUERIES = List.of(
            "portrait photograph person",
            "headshot face",
            "actor portrait",
            "actress portrait",
            "politician portrait",
            "singer portrait",
            "athlete portrait",
            "scientist portrait",
            "model portrait",
            "journalist portrait"
    );

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public WikimediaImageFetcher(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public List<String> fetchPortraitUrls(int targetCount, Set<String> excludeUrls) {
        Set<String> collected = new LinkedHashSet<>();

        for (String category : PORTRAIT_CATEGORIES) {
            if (collected.size() >= targetCount) {
                break;
            }
            int before = collected.size();
            collectFromGenerator(
                    "categorymembers",
                    "gcmtitle=" + encode(category) + "&gcmtype=file&gcmlimit=500",
                    targetCount,
                    excludeUrls,
                    collected
            );
            log.info("Category {} added {} URLs (total {})", category, collected.size() - before, collected.size());
        }

        for (String query : SEARCH_QUERIES) {
            if (collected.size() >= targetCount) {
                break;
            }
            int before = collected.size();
            collectFromGenerator(
                    "search",
                    "gsrsearch=" + encode(query) + "&gsrnamespace=6&gsrlimit=500",
                    targetCount,
                    excludeUrls,
                    collected
            );
            log.info("Search '{}' added {} URLs (total {})", query, collected.size() - before, collected.size());
        }

        return new ArrayList<>(collected);
    }

    public String searchFirstPortraitUrl(String query, Set<String> excludeUrls) {
        if (query == null || query.isBlank()) {
            return null;
        }
        Set<String> collected = new LinkedHashSet<>();
        collectFromGenerator(
                "search",
                "gsrsearch=" + encode(query + " portrait") + "&gsrnamespace=6&gsrlimit=20",
                1,
                excludeUrls == null ? Set.of() : excludeUrls,
                collected
        );
        return collected.isEmpty() ? null : collected.iterator().next();
    }

    private void collectFromGenerator(
            String generator,
            String generatorParams,
            int targetCount,
            Set<String> excludeUrls,
            Set<String> collected
    ) {
        Map<String, String> continuation = null;
        int pages = 0;

        while (collected.size() < targetCount && pages < 500) {
            try {
                String url = buildUrl(generator, generatorParams, continuation);
                JsonNode root = fetchJson(url);
                JsonNode pageNodes = root.path("query").path("pages");
                if (!pageNodes.isObject() || pageNodes.isEmpty()) {
                    break;
                }

                int addedThisPage = 0;
                Iterator<Map.Entry<String, JsonNode>> fields = pageNodes.fields();
                while (fields.hasNext()) {
                    if (collected.size() >= targetCount) {
                        break;
                    }
                    JsonNode imageInfo = fields.next().getValue().path("imageinfo");
                    if (!imageInfo.isArray() || imageInfo.isEmpty()) {
                        continue;
                    }
                    String imageUrl = firstNonBlank(
                            imageInfo.get(0).path("thumburl").asText(null),
                            ImageDownloadService.normalizeCommonsUrl(imageInfo.get(0).path("url").asText(null), 400)
                    );
                    if (isValidImageUrl(imageUrl) && !excludeUrls.contains(imageUrl) && collected.add(imageUrl)) {
                        addedThisPage++;
                    }
                }

                if (addedThisPage == 0) {
                    break;
                }

                JsonNode next = root.path("continue");
                if (next.isMissingNode() || next.isEmpty()) {
                    break;
                }
                continuation = toContinueMap(next);
                pages++;
                Thread.sleep(60);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception ex) {
                log.warn("Wikimedia {} fetch failed on page {}: {}", generator, pages, ex.getMessage());
                break;
            }
        }
    }

    private Map<String, String> toContinueMap(JsonNode continueNode) {
        java.util.LinkedHashMap<String, String> map = new java.util.LinkedHashMap<>();
        continueNode.fields().forEachRemaining(entry -> map.put(entry.getKey(), entry.getValue().asText()));
        return map;
    }

    private String buildUrl(String generator, String generatorParams, Map<String, String> continuation) {
        StringBuilder builder = new StringBuilder(API)
                .append("?action=query&format=json&origin=*")
                .append("&generator=").append(generator)
                .append("&").append(generatorParams)
                .append("&prop=imageinfo&iiprop=url&iiurlwidth=400");
        if (continuation != null) {
            continuation.forEach((key, value) -> builder.append("&").append(key).append("=").append(encode(value)));
        }
        return builder.toString();
    }

    private JsonNode fetchJson(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(30))
                .header("User-Agent", USER_AGENT)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IllegalStateException("HTTP " + response.statusCode());
        }
        return objectMapper.readTree(response.body());
    }

    private boolean isValidImageUrl(String url) {
        if (url == null || url.isBlank()) {
            return false;
        }
        String lower = url.toLowerCase();
        if (!lower.contains("upload.wikimedia.org")) {
            return false;
        }
        return lower.contains(".jpg")
                || lower.contains(".jpeg")
                || lower.contains(".png")
                || lower.contains(".webp");
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String firstNonBlank(String first, String second) {
        if (first != null && !first.isBlank()) {
            return first;
        }
        return second;
    }
}
