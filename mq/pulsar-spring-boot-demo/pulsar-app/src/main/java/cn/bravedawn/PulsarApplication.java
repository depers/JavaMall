package cn.bravedawn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "cn.bravedawn")
@MapperScan("cn.bravedawn.dao")
public class PulsarApplication {


    public static void main(String[] args) {
        SpringApplication.run(PulsarApplication.class, args);
    }
}
