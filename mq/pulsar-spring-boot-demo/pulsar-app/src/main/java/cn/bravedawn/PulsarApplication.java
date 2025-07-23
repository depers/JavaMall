package cn.bravedawn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cn.bravedawn")
public class PulsarApplication {


    public static void main(String[] args) {
        SpringApplication.run(PulsarApplication.class, args);
    }
}
