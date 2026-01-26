package cn.bravedawn.obj.proxy.cglib.v2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @Author : depers
 * @Date : Created in 2026-01-26 19:27
 */
@Slf4j
@Getter
@AllArgsConstructor
public class MethodInvocation {

    //target object
    private final Object targetObject;
    //target method
    private final Method targetMethod;
    //the parameter of target method
    private final Object[] args;

    public Object proceed() {
        try {
            return targetMethod.invoke(targetObject, args);
        } catch (Throwable e) {
            log.error("方法执行出现异常", e);
        }
        return null;
    }

}
