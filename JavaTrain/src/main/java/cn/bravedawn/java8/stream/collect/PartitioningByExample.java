package cn.bravedawn.java8.stream.collect;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : depers
 * @Date : Created in 2025-12-30 18:58
 *
 * partitioningBy()方法的演示
 */
public class PartitioningByExample {


    @Data
    @NoArgsConstructor
    private static class User {

        private String name;
        private String gender;
    }


    public static void main(String[] args) {
        User u1 = new User();
        u1.setName("depers");
        u1.setGender("男");

        User u2 = new User();
        u2.setName("jennery");
        u2.setGender("女");

        List<User> userList = new ArrayList<>();
        userList.add(u1);
        userList.add(u2);

        Map<Boolean, List<User>> userMap = userList.stream().collect(Collectors.partitioningBy(user -> user.getGender().equals("男")));
        System.out.println(userMap.get(true));
        System.out.println(userMap.get(false));



    }
}
