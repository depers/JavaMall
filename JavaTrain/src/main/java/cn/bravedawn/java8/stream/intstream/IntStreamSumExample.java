package cn.bravedawn.java8.stream.intstream;

import java.util.stream.IntStream;

/**
 * @Author : depers
 * @Date : Created in 2025-11-18 15:15
 */
public class IntStreamSumExample {

    /**
     * 求和
     */

    public static void main(String[] args) {
        int sum = IntStream.rangeClosed(1, 3).sum();
        System.out.println(sum);
    }

}
