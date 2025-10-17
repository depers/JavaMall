package cn.bravedawn.basic.random;


import java.util.UUID;

/**
 * @author : depers
 * @program : JavaTrain
 * @description:
 * @date : Created in 2023/2/2 11:05
 */
public class UUIDExample {

    /**
     * UUID
     */

    public static void main(String[] args) {
        String uid = UUID.randomUUID().toString();
        System.out.println(uid);
        System.out.println(uid.replaceAll("-", "").length());
        System.out.println(uid.replaceAll("-", ""));
    }
}
