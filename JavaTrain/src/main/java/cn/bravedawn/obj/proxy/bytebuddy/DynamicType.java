package cn.bravedawn.obj.proxy.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.InvocationTargetException;

/**
 * @Author : depers
 * @Date : Created in 2026-01-27 14:13
 */
public class DynamicType {

    /**
     * 动态生成一个类并复写toString方法
     * @param args
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */


    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        // 生成一个 Object 的子类，并覆盖 toString 方法
        Class<?> dynamicType = new ByteBuddy()
                .subclass(Object.class)
                .method(ElementMatchers.named("toString")) // 匹配 toString 方法
                .intercept(FixedValue.value("Hello from ByteBuddy!")) // 拦截并返回固定值
                .make()
                .load(DynamicType.class.getClassLoader()) // 将生成的类加载到 JVM 中
                .getLoaded();

        Object instance = dynamicType.getDeclaredConstructor().newInstance();
        System.out.println(instance.toString()); // 输出: Hello from ByteBuddy!
    }
}
