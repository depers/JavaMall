package cn.bravedawn.resttemplate;

import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.NoSuchAlgorithmException;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : spring-boot-demo
 * @Date : Created in 2025-08-07 13:59
 */
@Configuration
public class CustomRestTemplateConfig {


    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() throws NoSuchAlgorithmException {

        // 不校验服务器端请求的证书，测试环境使用
        SSLContext sslContext = SSLContext.getDefault();
        SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslConnectionSocketFactory)
                .build();

        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(registry);
        manager.setMaxTotal(200);          // 最大连接数
        manager.setDefaultMaxPerRoute(50); // 每个路由（域名）的最大连接数
        return manager;
    }

    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(5000)   // 连接超时（毫秒）
                .setSocketTimeout(10000)   // 读写超时（毫秒）
                .setConnectionRequestTimeout(2000) // 从连接池获取连接的超时时间
                .build();
    }

    @Bean
    public CloseableHttpClient httpClient(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager,
                                          RequestConfig requestConfig) {
        return HttpClientBuilder.create()
                .setConnectionManager(poolingHttpClientConnectionManager)
                .setDefaultRequestConfig(requestConfig())
//                .setProxy(new HttpHost("", 8080)) // 配置代理
                .build();
    }

    @Bean
    public RestTemplate restTemplate(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory factory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(factory);
    }
}
