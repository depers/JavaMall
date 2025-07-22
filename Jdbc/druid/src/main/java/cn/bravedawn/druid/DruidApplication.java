package cn.bravedawn.druid;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author : depers
 * @program : druid
 * @description: app
 * @date : Created in 2021/7/31 17:14
 */

@SpringBootApplication
@MapperScan("cn.bravedawn.druid.dao")
public class DruidApplication {

    public static void main(String[] args) {
        SpringApplication.run(DruidApplication.class, args);
    }
}
