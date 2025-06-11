package cn.bravedawn.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : spring-security-basic-demo
 * @Date : Created in 2025-06-11 16:46
 */

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }
}
