package cn.bravedawn.java8.stream;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2025-11-18 15:19
 */
public class MapToIntExample {

    /**
     * 拆分对象属性
     */

    @Data
    @AllArgsConstructor
    private static class User {

        private int age;
        private String name;
    }


    public static void main(String[] args) {
        User user = new User(1, "张三");
        User user1 = new User(2, "李四");

        List<User> userList = new ArrayList<>();
        userList.add(user);
        userList.add(user1);

        int sum = userList.stream().mapToInt(User::getAge).sum();
        System.out.println(sum);
    }
}
