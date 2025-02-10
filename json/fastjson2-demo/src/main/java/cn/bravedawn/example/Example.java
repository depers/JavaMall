package cn.bravedawn.example;

import cn.bravedawn.dto.Apple;
import cn.bravedawn.dto.Store;
import com.alibaba.fastjson2.JSON;

import java.math.BigDecimal;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : fastjson2-demo
 * @Date : Created in 2025-02-10 10:07
 */
public class Example {

    /*
    *  在不开启AutoType特性的情况下，这里会报错，无法强制转换为Apple
     */

    public static void main(String[] args) {
        // 序列化
        Store store = new Store();
        store.setName("Hollis");
        Apple apple = new Apple();
        apple.setPrice(new BigDecimal("10.5"));
        store.setFruit(apple);
        String jsonStr = JSON.toJSONString(store);
        System.out.println("toJsonString: " + jsonStr);

        // 反序列化
        Store newStore = JSON.parseObject(jsonStr, Store.class);
        System.out.println("newStore: " + newStore);
        Apple newApple = (Apple) newStore.getFruit(); // 在不开启AutoType特性的情况下，这里会报错，无法强制转换为Apple
        System.out.println("newApple: " + newApple);
    }
}
