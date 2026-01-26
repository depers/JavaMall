package cn.bravedawn.obj.proxy.cglib.v2;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author : depers
 * @Date : Created in 2026-01-26 20:22
 */

@Slf4j
public class InterceptorTwo extends Interceptor{


    @Override
    public Object intercept(MethodInvocation methodInvocation) {
        log.info("开始执行第二个拦截器逻辑");
        Object result = methodInvocation.proceed();
        log.info("第二个拦截器执行结束");
        return result;
    }
}
