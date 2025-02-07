package cn.bravedawn.basic.lang.string;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : JavaTrain
 * @Date : Created in 2025-02-07 17:08
 */
public class StringSubExample {

    /**
     * 字符串截取
     * 第一个下标是起始下标，截取的字符串包含该下标的元素
     * 第二个下标是结束下标，截取的字符串不包含该下标的元素
     */


    public static void main(String[] args) {

        String a = "abcdefghijklmn";

        System.out.println(a.substring(0, 2));
        System.out.println(a.substring(2, 4));
    }
}
