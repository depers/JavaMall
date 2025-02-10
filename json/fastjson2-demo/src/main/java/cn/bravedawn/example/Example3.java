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
public class Example3 {

    /**
     * 在VM参数中添加：-Dfastjson2.parser.safeMode=true，这样会完全禁用AutoType特性，如果在程序中显示声明使用AutoType特性，AutoType特性也不会生效。
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
