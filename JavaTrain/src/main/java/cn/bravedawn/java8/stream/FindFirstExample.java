package cn.bravedawn.java8.stream;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FindFirstExample {

    /**
     * findFirst的使用
     */

    public static void main(String[] args) {
        List<String> strings = Arrays.asList("1", "2", "3");
        Stream<String> stream = strings.stream();
        Optional<String> first = stream.findFirst();
        if (first.isPresent()) {
            System.out.println(first.get());
        }

        // 查看流中是否还存在该元素
        // stream.forEachOrdered(System.out::println); // 这里会报错，因为之前的流已经被关闭了
    }
}
