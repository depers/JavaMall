package cn.bravedawn.collection.list.arraylist;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * PC: SPDB
 * author: fengx9
 * create time: 2022/9/13 11:37
 */
public class ArrayListExample {

    /**
     * ArrayList的基本操作
     */


    public static void main(String[] args) {
        // 默认无参构造器
        List<String> list = new ArrayList<>();
        System.out.println(list.isEmpty()); // true

        // 接收初始配置容量
        List<String> list2 = new ArrayList<>(20);

        // 构造器接收Collection参数
        Collection<Integer> numbers = IntStream.range(0, 10).boxed().collect(Collectors.toSet());
        List<Integer> list3 = new ArrayList<>(numbers);
        System.out.println(list3.size()); // 10
        System.out.println(numbers.containsAll(list3)); // true


        // 添加元素到ArrayList
        List<Long> list4 = new ArrayList<>();
        list4.add(1l);
        list4.add(2l);
        list4.add(1, 3l);
        System.out.println("指定下标添加元素：" + Arrays.asList(1L, 3L, 2L).equals(list4)); // true

        // 一次插入一个Collection或一些元素
        List<Long> list5 = new ArrayList<>(Arrays.asList(1l, 2l, 3l));
        LongStream.range(4, 10).boxed()
                .collect(Collectors
                        .collectingAndThen(Collectors.toCollection(ArrayList::new), ys -> list5.addAll(0, ys)));
        System.out.println(Arrays.asList(4L, 5L, 6L, 7L, 8L, 9L, 1L, 2L, 3L).equals(list5));

        // 遍历ArrayList，ListIterator可以从两个方向上遍历列表
        List<Integer> list6 = new ArrayList<>(IntStream.range(0, 10).boxed()
                .collect(Collectors.toCollection(ArrayList::new)));
        ListIterator<Integer> it = list6.listIterator(list6.size());
        List<Integer> result = new ArrayList<>(list6.size());
        while (it.hasPrevious()) {
            result.add(it.previous());
        }
        Collections.reverse(list6);
        System.out.println(result); // [9, 8, 7, 6, 5, 4, 3, 2, 1, 0]
        System.out.println(list6.equals(result)); // true

        // 遍历ArrayList，Iterator只能从一个方向上遍历列表
        List<Integer> list7 = new ArrayList<>(IntStream.range(0, 10).boxed()
                .collect(Collectors.toCollection(ArrayList::new)));
        Iterator<Integer> iterator = list7.iterator();
        List<Integer> result2 = new ArrayList<>(list7.size());
        while (iterator.hasNext()) {
            result2.add(iterator.next());
        }
        System.out.println(result2); // [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]
        System.out.println(list7.equals(result2)); // true

    }
}
