package com.twinzy.seed;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class ImageDownloadService {

    private static final String USER_AGENT =
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 "
                    + "(KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";
    private static final String WIKIMEDIA_REFERER = "https://en.wikipedia.org/";

    private final HttpClient httpClient;
    private final WikipediaImageResolver wikipediaImageResolver;
    private final WikimediaImageFetcher wikimediaImageFetcher;

    public ImageDownloadService(
            WikipediaImageResolver wikipediaImageResolver,
            WikimediaImageFetcher wikimediaImageFetcher
    ) {
        this.wikipediaImageResolver = wikipediaImageResolver;
        this.wikimediaImageFetcher = wikimediaImageFetcher;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(15))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    public byte[] downloadWithFallbacks(String preferredUrl, String searchQuery) {
        List<String> candidates = buildCandidates(preferredUrl, searchQuery);
        for (String candidate : candidates) {
            byte[] bytes = download(candidate);
            if (bytes != null) {
                return bytes;
            }
        }
        return null;
    }

    public static String normalizeCommonsUrl(String url, int width) {
        if (url == null || url.isBlank()) {
            return null;
        }
        if (url.contains("/thumb/")) {
            return url;
        }
        return toThumbnailUrl(url, width);
    }

    public byte[] download(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }
        for (int attempt = 0; attempt < 3; attempt++) {
            byte[] bytes = downloadViaHttpClient(url);
            if (bytes != null) {
                return bytes;
            }
            bytes = downloadViaUrlConnection(url);
            if (bytes != null) {
                return bytes;
            }
            if (!isWikimediaUrl(url)) {
                bytes = downloadViaWeservProxy(url);
                if (bytes != null) {
                    return bytes;
                }
            }
            try {
                Thread.sleep(250L * (attempt + 1));
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                return null;
            }
        }
        return null;
    }

    private byte[] downloadViaWeservProxy(String url) {
        String hostPath = toHostPath(url);
        if (hostPath == null) {
            return null;
        }
        String proxyUrl = "https://images.weserv.nl/?url="
                + URLEncoder.encode(hostPath, StandardCharsets.UTF_8)
                + "&w=900&h=1100&fit=inside&we";
        return downloadViaHttpClient(proxyUrl);
    }

    private String toHostPath(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }
        if (url.startsWith("https://")) {
            return url.substring("https://".length());
        }
        if (url.startsWith("http://")) {
            return url.substring("http://".length());
        }
        return url;
    }

    private byte[] downloadViaHttpClient(String url) {
        try {
            HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url))
                    .timeout(Duration.ofSeconds(25))
                    .header("User-Agent", USER_AGENT)
                    .header("Accept", "image/avif,image/webp,image/apng,image/*,*/*;q=0.8")
                    .header("Accept-Language", "en-US,en;q=0.9")
                    .GET();
            if (isWikimediaUrl(url)) {
                builder.header("Referer", WIKIMEDIA_REFERER);
            }
            HttpResponse<byte[]> response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() == 200 && isLikelyImage(response.body())) {
                return response.body();
            }
        } catch (Exception ignored) {
            // try fallback transport
        }
        return null;
    }

    private byte[] downloadViaUrlConnection(String url) {
        try {
            URL connectionUrl = URI.create(url).toURL();
            java.net.URLConnection connection = connectionUrl.openConnection();
            connection.setRequestProperty("User-Agent", USER_AGENT);
            connection.setRequestProperty("Accept", "image/avif,image/webp,image/apng,image/*,*/*;q=0.8");
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.9");
            if (isWikimediaUrl(url)) {
                connection.setRequestProperty("Referer", WIKIMEDIA_REFERER);
            }
            connection.setConnectTimeout(15_000);
            connection.setReadTimeout(25_000);
            try (InputStream input = connection.getInputStream()) {
                byte[] bytes = input.readAllBytes();
                if (isLikelyImage(bytes)) {
                    return bytes;
                }
            }
        } catch (Exception ignored) {
            // retry outer loop
        }
        return null;
    }

    private boolean isLikelyImage(byte[] bytes) {
        if (bytes == null || bytes.length < 500) {
            return false;
        }
        if (bytes.length >= 3 && bytes[0] == (byte) 0xFF && bytes[1] == (byte) 0xD8) {
            return true;
        }
        if (bytes.length >= 8
                && bytes[0] == (byte) 0x89
                && bytes[1] == 'P'
                && bytes[2] == 'N'
                && bytes[3] == 'G') {
            return true;
        }
        if (bytes.length >= 12
                && bytes[0] == 'R'
                && bytes[1] == 'I'
                && bytes[2] == 'F'
                && bytes[3] == 'F') {
            return true;
        }
        return bytes.length > 2_000;
    }

    private boolean isWikimediaUrl(String url) {
        return url.contains("upload.wikimedia.org") || url.contains("wikipedia.org");
    }

    private List<String> buildCandidates(String preferredUrl, String searchQuery) {
        List<String> candidates = new ArrayList<>();
        String wikiThumb = normalizeCommonsUrl(wikipediaImageResolver.resolveThumbnailUrl(searchQuery), 500);
        addCandidate(candidates, wikiThumb);
        addCandidate(candidates, normalizeCommonsUrl(preferredUrl, 800));
        addCandidate(candidates, normalizeCommonsUrl(wikimediaImageFetcher.searchFirstPortraitUrl(searchQuery, Set.of()), 400));
        return candidates;
    }

    private void addCandidate(List<String> candidates, String url) {
        if (url == null || url.isBlank()) {
            return;
        }
        if (!candidates.contains(url)) {
            candidates.add(url);
        }
    }

    static String toThumbnailUrl(String url, int width) {
        if (url == null || url.isBlank()) {
            return null;
        }
        String marker = "/wikipedia/commons/";
        int commonsIndex = url.indexOf(marker);
        if (commonsIndex < 0 || url.contains("/thumb/")) {
            return null;
        }
        int fileStart = commonsIndex + marker.length();
        int lastSlash = url.lastIndexOf('/');
        if (lastSlash <= fileStart) {
            return null;
        }
        String hashPath = url.substring(fileStart, lastSlash);
        String filename = url.substring(lastSlash + 1);
        if (filename.isBlank()) {
            return null;
        }
        return url.substring(0, commonsIndex)
                + "/wikipedia/commons/thumb/"
                + hashPath
                + "/"
                + filename
                + "/"
                + width
                + "px-"
                + filename;
    }
}
