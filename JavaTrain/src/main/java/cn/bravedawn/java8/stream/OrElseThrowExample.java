package cn.bravedawn.java8.stream;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2026-01-26 16:19
 */
public class OrElseThrowExample {

    /**
     * 当值不存在时，抛出异常；存在时，直接返回值。
     */

    public static void main(String[] args) {
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5);


        Integer a = list.stream().filter(i -> i > 5).findAny().orElseThrow(() -> new IllegalArgumentException("没有找到大于5的参数"));
        System.out.println(a);
    }
}
