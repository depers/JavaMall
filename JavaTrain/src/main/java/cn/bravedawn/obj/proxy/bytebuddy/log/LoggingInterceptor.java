package cn.bravedawn.obj.proxy.bytebuddy.log;

/**
 * @Author : depers
 * @Date : Created in 2026-01-27 19:26
 */
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.concurrent.Callable;

public class LoggingInterceptor {

    
    @RuntimeType
    public static String intercept(@SuperCall Callable<String> superCall, @AllArguments Object[] args) throws Exception {
        System.out.println("before");
        String result = superCall.call(); // 调用原始方法
        System.out.println("after");
        return "Modified: " + result;
    }
}

