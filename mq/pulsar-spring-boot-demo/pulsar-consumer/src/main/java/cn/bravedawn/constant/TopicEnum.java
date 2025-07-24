package cn.bravedawn.constant;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-24 09:47
 */
public enum TopicEnum {

    SMS_TOPIC("sms", "短信发送消费者")
    ;


    private String topicPrefix;
    private String desc;

    TopicEnum(String topicPrefix, String desc) {
        this.topicPrefix = topicPrefix;
        this.desc = desc;
    }

    public String getTopicPrefix() {
        return topicPrefix;
    }

    public String getDesc() {
        return desc;
    }
}
