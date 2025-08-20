package cn.bravedawn.entity;

import lombok.Data;

/**
 * @Author : depers
 * @Date : Created in 2025-08-20 17:26
 */
@Data
public class User {

    /**
     * 用户 id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * email
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

}