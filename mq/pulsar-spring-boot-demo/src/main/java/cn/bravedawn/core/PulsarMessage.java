package cn.bravedawn.core;

import cn.bravedawn.contant.PriorityEnum;
import lombok.Data;

import java.util.Map;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-21 22:04
 */

@Data
public class PulsarMessage {

    private Long messageId;
    private String topic;
    private PriorityEnum priorityEnum;
    private Map<String, Object> properties;
}
