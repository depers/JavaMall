package cn.bravedawn.collection.list.arraylist;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2025-08-20 11:09
 */

@Slf4j
public class ForeachExample {


    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> emptyList = new ArrayList<>();
        List<Integer> nullList = null;


        // 在遍历list的时候，如果要跳出本次循环，可以直接return
        list.forEach(item -> {
            if (item == 1) {
                return;
            }
            log.info("此时遍历的元素是：{}", item);
        });

        // 如果一个list是空的话，会直接跳过遍历逻辑且不会报错
        emptyList.forEach(item -> {
            log.info("此时遍历的元素是：{}", item);
        });

        // 如果一个list是null的话，会报错
        nullList.forEach(item -> {
            log.info("此时遍历的元素是：{}", item);
        });
    }
}
