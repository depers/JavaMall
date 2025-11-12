package cn.bravedawn.java8.stream.collect;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;
/**
 * @Author : depers
 * @Date : Created in 2025-11-11 17:30
 */


public class StreamGroupCountExample {

    public static void main(String[] args) {
        List<Person> persons = Arrays.asList(
                new Person("Alice", "IT"),
                new Person("Bob", "HR"),
                new Person("Charlie", "IT"),
                new Person("David", "Finance"),
                new Person("Eve", "HR"),
                new Person("Frank", "IT")
        );

        // 按部门分组，统计每个部门的人数
        Map<String, Long> deptCountMap = persons.stream()
                .collect(Collectors.groupingBy(
                        Person::getDepartment,
                        Collectors.counting()
                ));

        System.out.println("部门人数统计: " + deptCountMap);
        // 输出: {Finance=1, HR=2, IT=3}
    }
}

@Data
@AllArgsConstructor
class Person {
    private String name;
    private String department;

    // 构造方法、getter、setter省略
}