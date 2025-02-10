package cn.bravedawn.example;

import cn.bravedawn.dto.Apple;
import cn.bravedawn.dto.Store;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;

import java.math.BigDecimal;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : fastjson2-demo
 * @Date : Created in 2025-02-10 10:12
 */
public class Example2 {

    /**
     * 如果要使用AutoType特性，需要显式在序列化和反序列化参数中进行声明
     */

    public static void main(String[] args) {
        // 序列化
        Store store = new Store();
        store.setName("Hollis");
        Apple apple = new Apple();
        apple.setPrice(new BigDecimal("10.5"));
        store.setFruit(apple);
        String jsonStr = JSON.toJSONString(store, JSONWriter.Feature.WriteClassName); // 声明使用AutoType特性
        System.out.println("toJsonString: " + jsonStr);

        // 反序列化
        Store newStore = JSON.parseObject(jsonStr, Store.class, JSONReader.Feature.SupportAutoType); // 声明使用AutoType特性
        System.out.println("newStore: " + newStore);
        Apple newApple = (Apple) newStore.getFruit();
        System.out.println("newApple: " + newApple);
    }
}
