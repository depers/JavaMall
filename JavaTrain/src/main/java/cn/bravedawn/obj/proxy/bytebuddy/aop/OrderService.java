package cn.bravedawn.obj.proxy.bytebuddy.aop;

/**
 * @Author : depers
 * @Date : Created in 2025-12-30 20:50
 */
public class OrderService {

    public void createOrder(String orderNo) {
        System.out.println("创建订单：" + orderNo);
    }
}

