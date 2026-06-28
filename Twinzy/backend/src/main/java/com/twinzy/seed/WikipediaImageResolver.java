package com.twinzy.seed;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class WikipediaImageResolver {

    private static final Logger log = LoggerFactory.getLogger(WikipediaImageResolver.class);
    private static final String API = "https://en.wikipedia.org/w/api.php";
    private static final String USER_AGENT = "TwinzySeed/1.0 (celebrity lookalike demo; twinzy@example.com)";
    private static final int THUMB_SIZE = 500;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public WikipediaImageResolver(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public String resolveThumbnailUrl(String title) {
        if (title == null || title.isBlank()) {
            return null;
        }
        try {
            String url = API
                    + "?action=query&format=json&origin=*"
                    + "&prop=pageimages&pithumbsize=" + THUMB_SIZE
                    + "&titles=" + encode(title.trim());
            JsonNode root = fetchJson(url);
            JsonNode pages = root.path("query").path("pages");
            if (!pages.isObject()) {
                return null;
            }
            var fields = pages.fields();
            while (fields.hasNext()) {
                JsonNode page = fields.next().getValue();
                String source = page.path("thumbnail").path("source").asText(null);
                if (source != null && !source.isBlank()) {
                    return ImageDownloadService.normalizeCommonsUrl(source, THUMB_SIZE);
                }
            }
        } catch (Exception ex) {
            log.debug("Wikipedia image lookup failed for '{}': {}", title, ex.getMessage());
        }
        return null;
    }

    private JsonNode fetchJson(String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .timeout(Duration.ofSeconds(20))
                .header("User-Agent", USER_AGENT)
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IllegalStateException("HTTP " + response.statusCode());
        }
        return objectMapper.readTree(response.body());
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
