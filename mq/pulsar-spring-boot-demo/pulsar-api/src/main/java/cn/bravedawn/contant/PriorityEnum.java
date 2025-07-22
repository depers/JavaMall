package cn.bravedawn.contant;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-21 22:07
 */
public enum PriorityEnum {

    NORMAL("normal", "正常优先级"),
    HIGH("high", "高优先级"),
    ;

    private String priority;
    private String desc;

    PriorityEnum(String priority, String desc) {
        this.priority = priority;
        this.desc = desc;
    }

    public String getPriority() {
        return priority;
    }

    public String getDesc() {
        return desc;
    }
}
