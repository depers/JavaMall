package cn.bravedawn.annotation.jsontypeinfo.v3;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : jackson-demo
 * @Date : Created in 2025-07-30 09:30
 */
public class JsonTypeInfoExample {


    public static void main(String[] args) throws JsonProcessingException {
        People.PhysicalCondition physicalCondition = new People.PhysicalCondition();
        physicalCondition.setWeight(70);
        physicalCondition.setHeight(175);

        Map<String, People.PhysicalCondition> properties = new HashMap<>();
        properties.put("condition", physicalCondition);

        People people = new People();
        people.setName("小明");
        people.setAge(18);
        people.setProperties(properties);


        JsonMapper mapper = JsonMapper.builder().build();
        /**
         *  在 JSON 中写入 Java 对象的“类型信息”，以便反序列化时能还原真实类型（多态），否则 Jackson 不知道该反序列化成哪个子类。
         *  效果是：
         *  {
         *   "@class": "com.example.User",
         *   "id": 1,
         *   "name": "张三"
         *  }
          */

        mapper.activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);

        /**
         * 在 JSON 中写入 Java 对象的“类型信息”，以便反序列化时能还原真实类型（多态），否则 Jackson 不知道该反序列化成哪个子类。
         * 效果是：
         * [
         *   "com.example.User",
         *   {
         *     "id": 1,
         *     "name": "张三"
         *   }
         * ]
         */
//        mapper.activateDefaultTyping(
//                LaissezFaireSubTypeValidator.instance,
//                ObjectMapper.DefaultTyping.NON_FINAL
//        );
        String json = mapper.writeValueAsString(people);
        System.out.println(json);


        People p = mapper.readValue(json, People.class);
        System.out.println(p); // 对象p的properties中的PhysicalCondition类被转为了LinkHashMap
    }
}
