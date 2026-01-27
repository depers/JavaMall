package cn.bravedawn.obj.proxy.bytebuddy.log;

/**
 * @Author : depers
 * @Date : Created in 2026-01-27 19:26
 */
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

public class LoggingProxyExample {


    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        MyService proxy = createLoggingProxy();

        String result = proxy.doSomething();
        System.out.println(result);  // Output: Before: RealService.doSomething() After
    }

    public static MyService createLoggingProxy() throws InstantiationException, IllegalAccessException {
        return new ByteBuddy()
                .subclass(MyService.class)
                .method(ElementMatchers.named("doSomething"))
                .intercept(MethodDelegation.to(LoggingInterceptor.class))
                .make()
                .load(MyService.class.getClassLoader())
                .getLoaded()
                .newInstance();
    }
}


