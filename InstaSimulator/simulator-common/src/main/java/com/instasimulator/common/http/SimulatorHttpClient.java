package com.instasimulator.common.http;

import com.instasimulator.common.constants.SimulatorConstants;
import com.instasimulator.common.enums.HttpMethod;
import com.instasimulator.common.exception.HttpClientException;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpPut;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Reusable Apache HttpClient 5 wrapper with pooling, timeouts, logging, and retries.
 */
public class SimulatorHttpClient implements Closeable {

    private static final Logger log = LoggerFactory.getLogger(SimulatorHttpClient.class);

    private final CloseableHttpClient httpClient;
    private final HttpClientConfig config;

    public SimulatorHttpClient(HttpClientConfig config) {
        this.config = config == null ? HttpClientConfig.defaults() : config;
        this.httpClient = buildClient(this.config);
    }

    public SimulatorHttpClient() {
        this(HttpClientConfig.defaults());
    }

    public HttpResponse execute(HttpRequest request) {
        int attempts = 0;
        Exception lastError = null;

        while (attempts <= config.getMaxRetries()) {
            attempts++;
            try {
                return doExecute(request, attempts - 1);
            } catch (HttpClientException ex) {
                lastError = ex;
                if (ex.getStatusCode() >= 400 && ex.getStatusCode() < 500 && ex.getStatusCode() != 429) {
                    throw ex;
                }
                if (attempts > config.getMaxRetries()) {
                    throw ex;
                }
                sleepBackoff(attempts);
            } catch (IOException ex) {
                lastError = ex;
                if (attempts > config.getMaxRetries()) {
                    throw new HttpClientException("HTTP request failed after retries: " + request.getUrl(), ex);
                }
                sleepBackoff(attempts);
            }
        }

        throw new HttpClientException("HTTP request failed: " + request.getUrl(), lastError);
    }

    public HttpResponse get(String url, Map<String, String> headers) {
        return execute(HttpRequest.builder().method(HttpMethod.GET).url(url).headers(headers).build());
    }

    public HttpResponse post(String url, String body, Map<String, String> headers) {
        return execute(HttpRequest.builder().method(HttpMethod.POST).url(url).body(body).headers(headers).build());
    }

    public HttpResponse put(String url, String body, Map<String, String> headers) {
        return execute(HttpRequest.builder().method(HttpMethod.PUT).url(url).body(body).headers(headers).build());
    }

    public HttpResponse delete(String url, Map<String, String> headers) {
        return execute(HttpRequest.builder().method(HttpMethod.DELETE).url(url).headers(headers).build());
    }

    public HttpResponse patch(String url, String body, Map<String, String> headers) {
        return execute(HttpRequest.builder().method(HttpMethod.PATCH).url(url).body(body).headers(headers).build());
    }

    private HttpResponse doExecute(HttpRequest request, int retryCount) throws IOException {
        long start = System.currentTimeMillis();
        HttpUriRequestBase apacheRequest = createApacheRequest(request);

        request.getHeaders().forEach(apacheRequest::addHeader);
        if (!request.getHeaders().containsKey("User-Agent")) {
            apacheRequest.addHeader("User-Agent", SimulatorConstants.DEFAULT_USER_AGENT);
        }

        if (config.isLogRequests()) {
            log.info("HTTP {} {} retry={} body={}",
                    request.getMethod(), request.getUrl(), retryCount,
                    truncate(request.getBody()));
        }

        return httpClient.execute(apacheRequest, classicResponse -> {
            long duration = System.currentTimeMillis() - start;
            String body = classicResponse.getEntity() == null
                    ? ""
                    : EntityUtils.toString(classicResponse.getEntity(), StandardCharsets.UTF_8);

            Map<String, String> responseHeaders = new HashMap<>();
            for (var header : classicResponse.getHeaders()) {
                responseHeaders.put(header.getName(), header.getValue());
            }

            HttpResponse response = HttpResponse.builder()
                    .statusCode(classicResponse.getCode())
                    .body(body)
                    .headers(responseHeaders)
                    .durationMs(duration)
                    .build();

            if (config.isLogResponses()) {
                log.info("HTTP {} {} -> {} ({} ms) body={}",
                        request.getMethod(), request.getUrl(),
                        response.getStatusCode(), duration, truncate(body));
            }

            if (!response.isSuccessful() && response.getStatusCode() >= 500) {
                throw new HttpClientException(
                        "Server error " + response.getStatusCode() + " for " + request.getUrl(),
                        response.getStatusCode());
            }

            return response;
        });
    }

    private HttpUriRequestBase createApacheRequest(HttpRequest request) {
        HttpUriRequestBase apacheRequest = switch (request.getMethod()) {
            case GET -> new HttpGet(request.getUrl());
            case POST -> new HttpPost(request.getUrl());
            case PUT -> new HttpPut(request.getUrl());
            case DELETE -> new HttpDelete(request.getUrl());
            case PATCH -> new HttpPatch(request.getUrl());
        };

        if (request.getBody() != null && !request.getBody().isBlank()) {
            apacheRequest.setEntity(new StringEntity(request.getBody(), ContentType.APPLICATION_JSON));
        }
        return apacheRequest;
    }

    private void sleepBackoff(int attempt) {
        try {
            long delay = config.getRetryBackoffMs() * attempt;
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new HttpClientException("Retry interrupted", e);
        }
    }

    private static String truncate(String value) {
        if (value == null) {
            return "";
        }
        return value.length() > 500 ? value.substring(0, 500) + "..." : value;
    }

    private static CloseableHttpClient buildClient(HttpClientConfig config) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(config.getMaxConnections());
        connectionManager.setDefaultMaxPerRoute(config.getMaxConnectionsPerRoute());
        connectionManager.setDefaultConnectionConfig(ConnectionConfig.custom()
                .setConnectTimeout(Timeout.of(config.getConnectTimeoutMs(), TimeUnit.MILLISECONDS))
                .setSocketTimeout(Timeout.of(config.getResponseTimeoutMs(), TimeUnit.MILLISECONDS))
                .build());

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(Timeout.of(config.getConnectTimeoutMs(), TimeUnit.MILLISECONDS))
                .setResponseTimeout(Timeout.of(config.getResponseTimeoutMs(), TimeUnit.MILLISECONDS))
                .build();

        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }
}
