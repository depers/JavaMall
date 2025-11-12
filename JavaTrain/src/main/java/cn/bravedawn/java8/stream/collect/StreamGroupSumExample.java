package cn.bravedawn.java8.stream.collect;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author : depers
 * @Date : Created in 2025-11-11 17:32
 */
public class StreamGroupSumExample {

    public static void main(String[] args) {
        List<Person> persons = Arrays.asList(
                new Person("Alice", "IT", 25),
                new Person("Bob", "HR", 30),
                new Person("Charlie", "IT", 28),
                new Person("David", "Finance", 35),
                new Person("Eve", "HR", 26),
                new Person("Frank", "IT", 32)
        );

        // 如果需要统计其他数值字段的总和
        Map<String, Integer> deptAgeSumMap = persons.stream()
                .collect(Collectors.groupingBy(
                        Person::getDepartment,
                        Collectors.summingInt(Person::getAge)
                ));

        System.out.println("部门年龄总和: " + deptAgeSumMap);
    }

    @Data
    @AllArgsConstructor
    static class Person {
        private String name;
        private String department;
        private int age;

        // 构造方法、getter、setter省略
    }
}


