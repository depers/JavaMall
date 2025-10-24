package cn.bravedawn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author : depers
 * @Date : Created in 2025-10-24 15:09
 */

@SpringBootApplication
@MapperScan("cn.bravedawn.mapper")
public class ShardingJdbcApplication {


    public static void main(String[] args) {
        SpringApplication.run(ShardingJdbcApplication.class, args);
    }
}
