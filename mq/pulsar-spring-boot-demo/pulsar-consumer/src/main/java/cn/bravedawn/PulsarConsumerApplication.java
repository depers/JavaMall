package cn.bravedawn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-24 09:40
 */

@SpringBootApplication(scanBasePackages = "cn.bravedawn")
public class PulsarConsumerApplication {


    public static void main(String[] args) {
        // 设置非 Web 模式
        SpringApplication app = new SpringApplication(PulsarConsumerApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE); // 关键设置
        app.run(args);
    }
}
