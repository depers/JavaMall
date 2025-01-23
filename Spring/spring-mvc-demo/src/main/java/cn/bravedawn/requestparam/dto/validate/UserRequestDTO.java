package cn.bravedawn.requestparam.dto.validate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : spring-mvc-demo
 * @Date : Created in 2025-01-23 16:39
 */
@Data
public class UserRequestDTO {

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @Max(value = 10)
    private int age;
}
