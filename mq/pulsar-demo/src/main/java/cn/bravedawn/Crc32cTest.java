package cn.bravedawn;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-demo
 * @Date : Created in 2025-07-15 11:06
 */
public class Crc32cTest {

    /**
     * 使用jdk1.8，puslar报Unable to use reflected methods: java.lang.ClassNotFoundException: java.util.zip.CRC32C
     * 这个问题不用管了，java8有默认的实现
     */
    public static void main(String[] args) {
        try {
            Class.forName("java.util.zip.CRC32C");
            System.out.println("CRC32C 类存在，当前 JDK 支持");
        } catch (ClassNotFoundException e) {
            System.out.println("CRC32C 类不存在，请升级 JDK 至 11+");
        }
    }
}
