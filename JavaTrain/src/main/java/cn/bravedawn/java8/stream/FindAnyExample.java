package cn.bravedawn.java8.stream;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * PC: SPDB
 * author: fengx9
 * create time: 2022/9/17 14:58
 */
public class FindAnyExample {

    /**
     * findAny的使用案例
     */

    public static void main(String[] args) {

        /**
         * findAny和findFirst的区别，他两都是查找一个元素，不同的是前者会随机取一个，而后者会取第一个
         */
        List<Integer> list = Lists.newArrayList(1, 2, 3, 4, 5, 2);
        Integer result = list.stream().filter(i -> i.equals(7)).findAny().orElse(0);
        System.out.println(result);
    }


}
