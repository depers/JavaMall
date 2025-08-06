package cn.bravedawn.hutool;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.util.List;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : jackson-demo
 * @Date : Created in 2025-08-06 15:50
 */
public class JsonObjectExample {

    @Data
    static class Item {

        private int id;
        private String title;
    }

    public static void main(String[] args) {
        String json = "{\n" +
                "  \"code\": 0,\n" +
                "  \"msg\": \"ok\",\n" +
                "  \"data\": {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"张三\",\n" +
                "    \"skills\": [\"Java\", \"Go\", \"Rust\"]\n" +
                "  },\n" +
                "  \"list\": [\n" +
                "    {\"id\": 10, \"title\": \"A\"},\n" +
                "    {\"id\": 11, \"title\": \"B\"}\n" +
                "  ]\n" +
                "}";


        // 解析为 JSONObject
        JSONObject root = JSONUtil.parseObj(json);


        // 1. 取字段
        System.out.println(root.getStr("msg"));

        // 2. 取子对象
        JSONObject data = root.getJSONObject("data");
        System.out.println(data.getStr("name"));

        // 3. 取数组
        JSONArray skills = data.getJSONArray("skills");
        System.out.println(skills.toList(String.class));

        // 4. 对象数组转 Bean
        JSONArray list = root.getJSONArray("list");
        List<Item> items = list.toList(Item.class);
        items.forEach(System.out::println);
    }
}
