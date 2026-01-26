package cn.bravedawn.obj.proxy.cglib.v2;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author : depers
 * @Date : Created in 2026-01-26 20:19
 */

@Slf4j
public class InterceptorOne extends Interceptor{


    @Override
    public Object intercept(MethodInvocation methodInvocation) {
        log.info("开始执行第一个拦截器逻辑");
        Object result = methodInvocation.proceed();
        log.info("第一个拦截器执行结束");
        return result;
    }
}
