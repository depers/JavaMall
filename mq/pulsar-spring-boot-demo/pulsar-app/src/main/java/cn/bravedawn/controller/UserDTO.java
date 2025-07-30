package cn.bravedawn.controller;

import lombok.Data;

import java.util.Date;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-29 17:47
 */

@Data
public class UserDTO {

    private String name;
    private int age;
    private Date brithday;
}
