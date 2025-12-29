package cn.bravedawn;

import java.util.Set;

/**
 * @Author : depers
 * @Date : Created in 2025-12-29 18:58
 */
public class ValidationUtilTest {


    public static void main(String[] args) {
        User user = new User();
        user.setAge(8);
        user.setUsername("");

        Set<String> validate = ValidationUtil.validate(user);
        System.out.println(validate);


        Set<String> validate1 = ValidationUtil.validate(user, AddGroup.class);
        System.out.println(validate1);

        Set<String> validate2 = ValidationUtil.validate(user, UpdateGroup.class);
        System.out.println(validate2);
    }
}
