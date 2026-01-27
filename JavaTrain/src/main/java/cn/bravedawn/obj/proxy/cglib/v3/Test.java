package cn.bravedawn.obj.proxy.cglib.v3;

/**
 * @Author : depers
 * @Date : Created in 2026-01-27 09:34
 */

import com.google.common.collect.Lists;
import java.util.List;

public class Test {

    /**
     * 在jdk17中运行需要添加jvm参数：--add-opens java.base/java.lang=ALL-UNNAMED
     */

    public static void main(String[] args) {
        OrderService orderService = new OrderService();

        // 准备拦截器链
        List<Interceptor> interceptors = Lists.newArrayList(
                new InterceptorOne(),
                new InterceptorTwo()
        );

        // 只进行一次代理包装
        OrderService proxyService = (OrderService) CglibAspectProxy.wrap(orderService, interceptors);

        // 执行方法
        proxyService.createOrder("1212");
    }
}