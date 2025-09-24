package cn.bravedawn.java8.stream;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2025-09-24 09:26
 */
public class ReduceExample {

    /**
     * reduce方法是Stream API中一个非常强大的操作，用于将流中的元素组合起来，生成一个单一的结果。它主要用于执行归约操作（如求和、求积、找最大值等）。
     * 下面演示的带有初始值的形式：reduce(identity, accumulator)
     */

    public static void main(String[] args) {
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);

        // 求和
        int sum = numbers.stream().reduce(0, Integer::sum);

        // 求积
        int product = numbers.stream().reduce(1, (a, b) -> a * b);
        System.out.println(product); // 输出: 120

        // 求最大值
        int max = numbers.stream().reduce(Integer.MIN_VALUE, Integer::max);
        System.out.println(max); // 输出: 5

        // 求最小值
        int min = numbers.stream().reduce(Integer.MAX_VALUE, Integer::min);
        System.out.println(min); // 输出: 1


        List<BigDecimal> bigDecimalList = List.of(new BigDecimal("23.21"), new BigDecimal("100"));
        BigDecimal reduce = bigDecimalList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println(reduce);
    }
}
