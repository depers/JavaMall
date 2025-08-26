package cn.bravedawn.example.encrypt;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author : depers
 * @program : 3.spring-security
 * @date : Created in 2024/7/12 22:59
 *
 * BCrypt算法的实践
 */
public class BcryptPasswordEncoderExample {


    public static void main(String[] args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        // 每次生成的密文是不一样的
        System.out.println(bCryptPasswordEncoder.encode("123456"));
        System.out.println(bCryptPasswordEncoder.encode("123456"));

        // 校验密码是否匹配
        String encryptPassword = "$2a$10$PH48kceUBVvVi6mN/2hKGemjPUjBpKCENBkP7nNzL1WxcFwpv.Aay";
        System.out.println(bCryptPasswordEncoder.matches("123456", encryptPassword));
    }
}
