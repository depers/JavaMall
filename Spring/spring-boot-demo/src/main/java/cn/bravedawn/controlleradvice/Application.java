package cn.bravedawn.controlleradvice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author : depers
 * @Date : Created in 2025-09-28 15:46
 */

@SpringBootApplication(scanBasePackages = {"cn.bravedawn.controlleradvice"})
public class Application {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.run(args);

    }
}
