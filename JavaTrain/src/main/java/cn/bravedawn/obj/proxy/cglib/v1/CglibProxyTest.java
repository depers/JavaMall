package cn.bravedawn.obj.proxy.cglib.v1;

/**
 * @Author : depers
 * @Date : Created in 2025-12-31 11:19
 */
import net.sf.cglib.proxy.Enhancer;

public class CglibProxyTest {

    /**
     * 在jdk17中运行需要添加jvm参数：--add-opens java.base/java.lang=ALL-UNNAMED
     */


    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(OrderService.class);
        enhancer.setCallback(new LogMethodInterceptor());

        OrderService proxy = (OrderService) enhancer.create();
        proxy.createOrder("ORD-001");
    }
}

