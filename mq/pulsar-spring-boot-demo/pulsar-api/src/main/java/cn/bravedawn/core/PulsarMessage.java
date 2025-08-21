package cn.bravedawn.core;

import cn.bravedawn.contant.PriorityEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
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
    private String topicPrefix;
    private Date firstSendTime;
    private PriorityEnum priorityEnum;
    private Long delayMills;
    private Map<String, Object> properties;
}
