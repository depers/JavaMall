package cn.bravedawn.obj.proxy.cglib.v2;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2026-01-26 20:22
 */
public class Test {
    /**
     * 在jdk17中运行需要添加jvm参数：--add-opens java.base/java.lang=ALL-UNNAMED
     */

    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        Object wrapperProxyBean = orderService;

        List<Interceptor> interceptors = Lists.newArrayList(new InterceptorOne(), new InterceptorTwo());
        for (Interceptor interceptor : interceptors) {
            // 第一次包装：OrderService -> Proxy1 (持有 OrderService)
            // 第二次包装：Proxy1 -> Proxy2 (持有 Proxy1)
            wrapperProxyBean = CglibAspectProxy.wrap(wrapperProxyBean, interceptor);
        }

        OrderService orderServiceV2 = (OrderService) wrapperProxyBean;
        orderServiceV2.createOrder("1212");
    }
}
