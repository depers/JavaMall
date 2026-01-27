package cn.bravedawn.obj.proxy.bytebuddy.aop;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * @Author : depers
 * @Date : Created in 2026-01-27 19:35
 */

public class ByteBuddyMethodInvocation {


    private final List<Interceptor> interceptorChain;
    private final Callable<?> zuper; // ByteBuddy 提供的原方法调用引用
    private int currentInterceptorIndex = -1;

    public ByteBuddyMethodInvocation(List<Interceptor> interceptorChain, Callable<?> zuper) {
        this.interceptorChain = interceptorChain;
        this.zuper = zuper;
    }

    public Object proceed() throws Exception {
        // 如果所有拦截器都执行完了，则调用目标类的原方法
        if (this.currentInterceptorIndex == this.interceptorChain.size() - 1) {
            return zuper.call(); // 相当于 CGLIB 的 invokeSuper
        }

        // 递归执行下一个拦截器
        Interceptor interceptor = interceptorChain.get(++currentInterceptorIndex);
        // 注意：这里需要修改 Interceptor 接口，使其接受当前这个 ByteBuddyMethodInvocation
        return interceptor.intercept(this);
    }
}