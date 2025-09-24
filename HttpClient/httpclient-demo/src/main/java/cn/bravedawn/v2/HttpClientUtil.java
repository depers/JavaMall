package cn.bravedawn.v2;

/**
 * @Author : depers
 * @Date : Created in 2025-09-24 14:47
 */
import org.apache.http.*;
import org.apache.http.client.config.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.*;
import org.apache.http.util.EntityUtils;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class HttpClientUtil {
    private final CloseableHttpClient httpClient;
    private final PoolingHttpClientConnectionManager connectionManager;

    private HttpClientUtil() {
        this.connectionManager = createConnectionManager();
        this.httpClient = createHttpClient();
        startMonitorThread();
    }

    // 单例模式
    private static class Holder {
        private static final HttpClientUtil INSTANCE = new HttpClientUtil();
    }

    public static HttpClientUtil getInstance() {
        return Holder.INSTANCE;
    }

    // 配置连接管理器
    private PoolingHttpClientConnectionManager createConnectionManager() {
        // 这里的60秒表示：从连接池中借出的连接，在使用完毕后返回到连接池后，最多可以存活60秒。超过这个时间，连接会被自动关闭。
        PoolingHttpClientConnectionManager cm =
                new PoolingHttpClientConnectionManager(60, TimeUnit.SECONDS);
        cm.setMaxTotal(200); // 最大连接数
        cm.setDefaultMaxPerRoute(50); // 每个路由最大连接数
        return cm;
    }

    private CloseableHttpClient createHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000) // 连接超时时间（毫秒）
                .setSocketTimeout(30000) // 读取超时时间（毫秒）
                .setConnectionRequestTimeout(5000) // 从连接池获取连接超时时间
                .build();

        return HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setDefaultRequestConfig(requestConfig)
                .setRetryHandler(new DefaultHttpRequestRetryHandler(3, true))
                .addInterceptorLast(new CustomRequestInterceptor())
                .addInterceptorLast(new CustomResponseInterceptor())
                .setConnectionManagerShared(true) // 共享连接管理器
                .build();
    }

    private void startMonitorThread() {
        Thread monitorThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Thread.sleep(30000);
                    connectionManager.closeExpiredConnections();
                    connectionManager.closeIdleConnections(60, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        monitorThread.setDaemon(true);
        monitorThread.start();
    }

    // HTTP GET请求
    public String doGet(String url) throws IOException {
        return doGet(url, null);
    }

    public String doGet(String url, Header[] headers) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        if (headers != null) {
            httpGet.setHeaders(headers);
        }

        try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
            return handleResponse(response);
        }
    }

    // HTTP POST请求
    public String doPost(String url, String jsonBody) throws IOException {
        return doPost(url, jsonBody, null);
    }

    public String doPost(String url, String jsonBody, Header[] headers) throws IOException {
        HttpPost httpPost = new HttpPost(url);
        if (headers != null) {
            httpPost.setHeaders(headers);
        }

        StringEntity entity = new StringEntity(jsonBody, StandardCharsets.UTF_8);
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            return handleResponse(response);
        }
    }

    // 处理响应
    private String handleResponse(CloseableHttpResponse response) throws IOException {
        int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();

        if (statusCode >= 200 && statusCode < 300) {
            return entity != null ? EntityUtils.toString(entity, StandardCharsets.UTF_8) : "";
        } else {
            String errorMsg = entity != null ?
                    EntityUtils.toString(entity, StandardCharsets.UTF_8) : "无响应内容";
            throw new HttpClientException("HTTP错误 " + statusCode + ": " + errorMsg);
        }
    }

    // 关闭HttpClient
    public void close() throws IOException {
        if (httpClient != null) {
            httpClient.close();
        }
        if (connectionManager != null) {
            connectionManager.shutdown();
        }
    }

    // 自定义异常
    public static class HttpClientException extends RuntimeException {
        public HttpClientException(String message) {
            super(message);
        }

        public HttpClientException(String message, Throwable cause) {
            super(message, cause);
        }
    }


}
