package cn.bravedawn.requestparam.controller;

import cn.bravedawn.requestparam.dto.validate.UserRequestDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : spring-mvc-demo
 * @Date : Created in 2025-01-23 16:38
 */
@Slf4j
@RestController
@Validated
public class ValidateController {

    /**
     * 如果请求参数不满足条件会抛出MethodArgumentNotValidException异常
     */
    @PostMapping("/putUser")
    public String add(@Validated UserRequestDTO userRequestDTO) {
        log.info("userRequestDTO:{}", userRequestDTO);
        return "success";
    }

    /**
     * 如果请求参数不满足条件会抛出MethodArgumentNotValidException异常
     */
    @GetMapping("/getUser")
    public String getUser(@Validated UserRequestDTO userRequestDTO) {
        log.info("userRequestDTO:{}", userRequestDTO);
        return "success";
    }

    /**
     * 如果username为空，就会抛出ConstraintViolationException异常
     * 要抛出这个异常必须满足三个条件：
     * 1. 这个方法的类上面必须写上@Validated注解
     * 2. @NotEmpty校验注解必须写到参数这里
     * 3. 必须对应每一个参数，也就接口入参直接映射到控制器方法参数
     */
    @GetMapping("/getUserByName")
    public String getUser(@NotEmpty String username) {
        log.info("user:{}", username);
        return "success";
    }
}
