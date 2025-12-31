package cn.bravedawn.obj.proxy.cglib;

/**
 * @Author : depers
 * @Date : Created in 2025-12-31 11:19
 */
import net.sf.cglib.proxy.Enhancer;

public class CglibProxyTest {
    

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(OrderService.class);
        enhancer.setCallback(new LogMethodInterceptor());

        OrderService proxy = (OrderService) enhancer.create();
        proxy.createOrder("ORD-001");
    }
}

