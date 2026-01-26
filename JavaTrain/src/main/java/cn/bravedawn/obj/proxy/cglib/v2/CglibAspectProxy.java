package cn.bravedawn.obj.proxy.cglib.v2;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author : depers
 * @Date : Created in 2026-01-26 19:58
 */
public class CglibAspectProxy implements MethodInterceptor {

    private final Object target;
    private final Interceptor interceptor;

    public CglibAspectProxy(Object target, Interceptor interceptor) {
        this.target = target;
        this.interceptor = interceptor;
    }


    public static Object wrap(Object target, Interceptor interceptor) {
        Class<?> rootClass = target.getClass();
        Class<?> proxySuperClass = rootClass;

        if (target.getClass().getName().contains("$$")) {
            proxySuperClass = rootClass.getSuperclass();
        }

        Enhancer enhancer = new Enhancer();
        enhancer.setClassLoader(target.getClass().getClassLoader());
        enhancer.setSuperclass(proxySuperClass);
        enhancer.setCallback(new CglibAspectProxy(target, interceptor));
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {

        MethodInvocation methodInvocation = new MethodInvocation(target, method, args);
        return interceptor.intercept(methodInvocation);
    }
}
