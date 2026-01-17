package cn.bravedawn.java8.stream;

import java.util.Collections;
import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2026-01-15 13:49
 */
public class ForeachExample {


    public static void main(String[] args) {
        List<String> list = Collections.emptyList();

        list.forEach(item -> {
            System.out.println("foreach输出字段：" + item);
        });

        list.stream().forEach(item -> {
            System.out.println("foreach输出字段：" + item);
        });
    }
}
