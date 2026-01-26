package cn.bravedawn.obj.proxy.cglib.v2;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2026-01-26 20:22
 */
public class Test {

    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        Object wrapperProxyBean = orderService;

        List<Interceptor> interceptors = Lists.newArrayList(new InterceptorOne(), new InterceptorTwo());
        for (Interceptor interceptor : interceptors) {
            wrapperProxyBean = CglibAspectProxy.wrap(wrapperProxyBean, interceptor);
        }

        OrderService orderServiceV2 = (OrderService) wrapperProxyBean;
        orderServiceV2.createOrder("1212");
    }
}
