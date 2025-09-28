package cn.bravedawn.controlleradvice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author : depers
 * @Date : Created in 2025-09-28 14:23
 */


@Data
@Configuration
@ConfigurationProperties(prefix = "test.encrypt-filter")
public class RequestFilterConfig {

    /**
     * 是否需要开启加解密
     */
    private boolean enable;

    /**
     * 需要进行加解密的路由
     */
    private String filterUrl;

    /**
     * 第三方公钥
     */
    private String thirdPartyPublicKey;

    /**
     * 自己的私钥
     */
    private String bankPrivateKey;

    /**
     * 自己的公钥
     */
    private String bankPublicKey;


    public List<String> getFilterUrlList() {
        return Stream.of(filterUrl.split(",")).map(String::trim).collect(Collectors.toList());
    }
}
