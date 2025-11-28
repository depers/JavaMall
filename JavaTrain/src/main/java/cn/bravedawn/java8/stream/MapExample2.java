package cn.bravedawn.java8.stream;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author : depers
 * @Date : Created in 2025-11-28 14:45
 */
@Slf4j
public class MapExample2 {

    /**
     * 在使用map函数时如果出现异常如何返回和过滤结果
     */


    public static void main(String[] args) {

        List<Integer> list = new ArrayList<>();
        list.add(10);
        list.add(0);
        list.add(2);

        List<Integer> collect = list.stream().map(i -> {
            try {
                return 100 / i;
            } catch (Throwable e) {
                log.error("出现异常:{}", e.getMessage());
                return null;
            }
        })
        .filter(Objects::nonNull)   // 过滤掉 null
        .collect(Collectors.toList());
        System.out.println(collect);
    }
}
