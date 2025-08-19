package cn.bravedawn.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "canal")
public class CanalConfig {

    /**
     * 主机 ip
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 主题名
     */
    private String topic;

    /**
     * 批量获取数据大小
     */
    private Integer batchSize;

    /**
     * 启用
     */
    private Boolean enable;

}