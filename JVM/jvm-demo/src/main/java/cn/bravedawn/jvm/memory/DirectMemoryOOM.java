package cn.bravedawn.jvm.memory;




import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @author : depers
 * @program : jvm-demo
 * @description:
 * @date : Created in 2023/3/5 14:54
 */
public class DirectMemoryOOM {

    /**
     * 通过Unsafe实例进行内存分配，使用直接内存导致溢出
     * 虚拟机参数：-Xmx20m -XX:MaxDirectMemorySize=10m
     * 这个代码谨慎运行，会导致mac假死
     */

    private static final int _1MB = 1024 * 1024;

    public static void main(String[] args) throws IllegalAccessException {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        while (true) {
            unsafe.allocateMemory(_1MB);
        }

    }
}
