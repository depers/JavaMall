package cn.bravedawn.obj.proxy.bytebuddy.aop;

import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @Author : depers
 * @Date : Created in 2026-01-27 19:40
 */
public class InterceptorDelegate {

    // 拦截器链由外部注入
    private final List<Interceptor> interceptors;

    public InterceptorDelegate(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

    @RuntimeType
    public Object intercept(@SuperCall Callable<?> zuper) throws Exception {
        // 构造链式执行上下文
        ByteBuddyMethodInvocation invocation = new ByteBuddyMethodInvocation(interceptors, zuper);
        return invocation.proceed();
    }
}
