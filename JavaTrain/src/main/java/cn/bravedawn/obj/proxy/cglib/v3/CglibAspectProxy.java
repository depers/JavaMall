package cn.bravedawn.obj.proxy.cglib.v3;

/**
 * @Author : depers
 * @Date : Created in 2026-01-27 09:33
 */

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;
import java.util.List;

public class CglibAspectProxy implements MethodInterceptor {

    private final Object target;
    private final List<Interceptor> interceptorChain;

    private CglibAspectProxy(Object target, List<Interceptor> interceptorChain) {
        this.target = target;
        this.interceptorChain = interceptorChain;
    }

    public static Object wrap(Object target, List<Interceptor> interceptors) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setClassLoader(target.getClass().getClassLoader());
        // 传入整个拦截器链
        enhancer.setCallback(new CglibAspectProxy(target, interceptors));
        return enhancer.create();
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
        // 构造执行链上下文
        MethodInvocation invocation = new MethodInvocation(
                target, method, args, proxy, interceptorChain, -1
        );
        return invocation.proceed();
    }
}