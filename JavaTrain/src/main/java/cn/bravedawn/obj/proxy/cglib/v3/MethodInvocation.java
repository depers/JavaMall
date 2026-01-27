package cn.bravedawn.obj.proxy.cglib.v3;

/**
 * @Author : depers
 * @Date : Created in 2026-01-27 09:31
 */

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodProxy;
import java.lang.reflect.Method;
import java.util.List;

@Slf4j
@AllArgsConstructor
public class MethodInvocation {
    private final Object targetObject;
    private final Method targetMethod;
    private final Object[] args;
    private final MethodProxy methodProxy; // 引入 CGLIB 高性能代理
    private final List<Interceptor> interceptorChain; // 拦截器链
    private int currentInterceptorIndex = -1; // 当前索引

    public Object proceed()  {
        try {
            // 如果所有拦截器都执行完了，执行目标方法
            if (this.currentInterceptorIndex == this.interceptorChain.size() - 1) {
                // 使用 MethodProxy 避免反射，提高性能
                // 使用 invoke 作用于 targetObject（原始对象）
                return methodProxy.invoke(targetObject, args);
            }

            // 获取下一个拦截器并执行
            Interceptor interceptor = interceptorChain.get(++currentInterceptorIndex);
            return interceptor.intercept(this);
        } catch (Throwable e) {
            log.error("执行代理异常", e);
        }
        return null;

    }
}
