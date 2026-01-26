package cn.bravedawn.obj.proxy.cglib.v1;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author : depers
 * @Date : Created in 2025-12-31 11:17
 */
public class LogMethodInterceptor implements MethodInterceptor {

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
            throws Throwable {

        System.out.println("【前置】方法：" + method.getName());
        Object result = proxy.invokeSuper(obj, args);
        System.out.println("【后置】方法：" + method.getName());
        return result;
    }
}