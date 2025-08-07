package cn.bravedawn.resttemplate;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : spring-boot-demo
 * @Date : Created in 2025-08-07 14:57
 */

@Slf4j
public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {


    @Override
    public ClientHttpResponse intercept(org.springframework.http.HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        // 记录请求日志
        log.info("\n=== Request ===");
        log.info("URI: {}", request.getURI());
        log.info("Method: {}", request.getMethod());
        log.info("Headers: {}", request.getHeaders());
        log.info("Body: {}", new String(body, "UTF-8"));

        // 执行请求
        ClientHttpResponse response = execution.execute(request, body);

        // 记录响应日志
        log.info("\n=== Response ===");
        log.info("Status: {}", response.getStatusCode());
        log.info("Headers: {}", response.getHeaders());

        // 复制响应流以重复读取（避免流被关闭）
        BufferingClientHttpResponseWrapper bufferedResponse =
                new BufferingClientHttpResponseWrapper(response);
        log.info("Body: {}", IOUtils.toString(bufferedResponse.getBody(), "UTF-8"));

        return bufferedResponse;
    }
}