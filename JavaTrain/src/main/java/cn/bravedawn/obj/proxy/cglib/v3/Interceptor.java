package cn.bravedawn.obj.proxy.cglib.v3;


/**
 * @Author : depers
 * @Date : Created in 2026-01-26 19:26
 */
public abstract class Interceptor {

    public abstract Object intercept(MethodInvocation methodInvocation);
}
