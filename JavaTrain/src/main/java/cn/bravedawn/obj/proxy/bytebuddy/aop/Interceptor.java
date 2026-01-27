package cn.bravedawn.obj.proxy.bytebuddy.aop;


import cn.bravedawn.obj.proxy.cglib.v3.MethodInvocation;

/**
 * @Author : depers
 * @Date : Created in 2026-01-26 19:26
 */
public abstract class Interceptor {

    public abstract Object intercept(ByteBuddyMethodInvocation methodInvocation) throws Exception;
}
