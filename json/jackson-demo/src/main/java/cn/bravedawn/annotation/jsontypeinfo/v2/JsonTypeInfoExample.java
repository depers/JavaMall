package cn.bravedawn.annotation.jsontypeinfo.v2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

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
        String json = mapper.writeValueAsString(people);
        System.out.println(json);


        People p = mapper.readValue(json, People.class);
        System.out.println(p); // 对象p的properties中的PhysicalCondition类被转为了LinkHashMap
    }
}
