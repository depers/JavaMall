package cn.bravedawn;

/**
 * @Author : depers
 * @Date : Created in 2025-12-29 18:53
 */
import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class User {

    @NotNull(message = "id 不能为空", groups = {AddGroup.class})
    private Long id;

    @NotBlank(message = "用户名不能为空", groups = {UpdateGroup.class})
    @Size(min = 3, max = 20, message = "用户名长度必须在3~20之间")
    private String username;

    @Min(value = 18, message = "年龄不能小于18")
    @Max(value = 60, message = "年龄不能大于60")
    private Integer age;

}
