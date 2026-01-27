package cn.bravedawn.obj.proxy.bytebuddy.aop;

import com.google.common.collect.Lists;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2026-01-27 19:39
 */
public class Test {

    /**
     * 使用 ByteBuddy 实现一个类似 Spring AOP 的多层拦截器链
     */


    public static void main(String[] args) throws Exception {
        OrderService orderService = wrap(OrderService.class, Lists.newArrayList(new InterceptorOne(), new InterceptorTwo()));
        orderService.createOrder("byte buddy");
    }


    public static <T> T wrap(Class<T> clazz, List<Interceptor> interceptors) throws Exception {
        return new ByteBuddy()
                .subclass(clazz) // 1. 指定父类
                .method(ElementMatchers.any()) // 2. 匹配所有方法
                .intercept(MethodDelegation.to(new InterceptorDelegate(interceptors))) // 3. 委托给拦截器链执行器
                .make()
                .load(clazz.getClassLoader()) // 4. 加载字节码
                .getLoaded()
                .getDeclaredConstructor()
                .newInstance();
    }
}
