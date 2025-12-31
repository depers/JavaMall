package cn.bravedawn.obj.proxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author : depers
 * @Date : Created in 2025-12-30 20:48
 */

public class LogInvocationHandler implements InvocationHandler {

    private final Object target;

    public LogInvocationHandler(Object target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("【前置】方法：" + method.getName());
        Object result = method.invoke(target, args);
        System.out.println("【后置】方法：" + method.getName());
        return result;
    }
}

