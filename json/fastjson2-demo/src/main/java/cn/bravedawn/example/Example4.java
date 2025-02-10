package cn.bravedawn.example;

import cn.bravedawn.dto.Apple;
import cn.bravedawn.dto.Store;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;

import java.math.BigDecimal;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : fastjson2-demo
 * @Date : Created in 2025-02-10 10:12
 */
public class Example4 {

    /**
     * 在VM参数中添加：-Dfastjson2.parser.safeMode=true，这样会完全禁用AutoType特性。
     * 如果我们使用AutoFilter，缩小反序列化的对象范围，在完全禁用AutoType功能的情况下，也是可以实现自动类型转换。
     */

    public static void main(String[] args) {

        final Filter autoTypeFilter = JSONReader.autoTypeFilter(
                // 支持autoType的类名前缀，范围越小越安全
                "cn.bravedawn.dto");

        // 序列化
        Store store = new Store();
        store.setName("Hollis");
        Apple apple = new Apple();
        apple.setPrice(new BigDecimal("10.5"));
        store.setFruit(apple);
        String jsonStr = JSON.toJSONString(store, JSONWriter.Feature.WriteClassName); // 声明使用AutoType特性
        System.out.println("toJsonString: " + jsonStr);

        // 反序列化
        Store newStore = JSON.parseObject(jsonStr, Store.class, autoTypeFilter); // 声明使用AutoType特性
        System.out.println("newStore: " + newStore);
        Apple newApple = (Apple) newStore.getFruit();
        System.out.println("newApple: " + newApple);
    }
}
