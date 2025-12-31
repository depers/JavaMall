package cn.bravedawn.obj.proxy.jdk;

/**
 * @Author : depers
 * @Date : Created in 2025-12-30 20:48
 */
import java.lang.reflect.Proxy;

public class JdkProxyTest {

    public static void main(String[] args) {
        UserService target = new UserServiceImpl();

        UserService proxy = (UserService) Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new LogInvocationHandler(target)
        );

        proxy.save("Tom");
    }
}

