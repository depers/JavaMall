package cn.bravedawn.collection.list.removeduplicate.example4;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author : depers
 * @Date : Created in 2025-10-21 11:02
 */
public class RemoveDuplicateExample4 {

    public static void main(String[] args) {
        List<Person> persons = Arrays.asList(
                new Person("Alice", 25),
                new Person("Bob", 30),
                new Person("Alice", 25), // 重复
                new Person("Charlie", 35),
                new Person("Bob", 30)    // 重复
        );

        List<Person> distinctPersons = persons.stream()
                .distinct()
                .collect(Collectors.toList());

        System.out.println(distinctPersons);
        // 输出: [Alice(25), Bob(30), Charlie(35)]
    }
}
