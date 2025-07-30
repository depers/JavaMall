package cn.bravedawn.annotation.jsontypeinfo.v2;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.util.Map;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : jackson-demo
 * @Date : Created in 2025-07-30 09:31
 */

@Data
public class People {


    private String name;
    private int age;
    private Map<String, People.PhysicalCondition> properties;


    @Data
    @JsonTypeInfo(
            use = JsonTypeInfo.Id.CLASS,
            include = JsonTypeInfo.As.PROPERTY,
            property = "type"
    )
    public static class PhysicalCondition {

        private int height;
        private int weight;
    }
}
