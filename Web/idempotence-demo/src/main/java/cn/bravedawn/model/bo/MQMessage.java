package cn.bravedawn.model.bo;

import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : depers
 * @program : idempotence-demo
 * @date : Created in 2024/11/21 21:38
 */
@Data
public class MQMessage {

    private Long messageId;
    private String topic;
    private String routingKey;
    private Map<String, Object> attributes = new HashMap<String, Object>();
    private String messageType;
    private Date nextRetryTime;
    private Long ttl = -1L;
    private Date firstSendTime;
    private Integer priority;
}
