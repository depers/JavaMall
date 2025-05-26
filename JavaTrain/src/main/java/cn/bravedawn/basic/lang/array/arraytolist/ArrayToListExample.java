package cn.bravedawn.basic.lang.array.arraytolist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : JavaTrain
 * @Date : Created in 2025-05-26 18:10
 */
public class ArrayToListExample {

    public static void main(String[] args) {
        getOnlyReadList();

        getListOfJava8();

        getListByCollection();

    }

    /**
     * 返回只读视图的list
     */
    private static void getOnlyReadList() {

        String[] array = {"a", "b", "c"};
        List<String> list = Arrays.asList(array);
        System.out.println(list);
    }

    /**
     * 使用Java8的api，获取的list是可变的
     */
    private static void getListOfJava8() {
        String[] array = {"a", "b", "c"};
        List<String> list = Arrays.stream(array).collect(Collectors.toList());
        System.out.println(list);

        // 基本类型数组处理
        int[] intArray = {1, 2, 3};
        List<Integer> intList = Arrays.stream(intArray).boxed().collect(Collectors.toList());
        System.out.println(list);
    }

    /**
     * 对于大数组，Collections.addAll()可能更高效
     */
    private static void getListByCollection() {
        String[] array = {"a", "b", "c"};
        List<String> list = new ArrayList<>(array.length);
        Collections.addAll(list, array);
        System.out.println(list);
    }
}
