package cn.bravedawn.contant;

/**
 * @Description : TODO
 * @Author : depers
 * @Project : pulsar-spring-boot-demo
 * @Date : Created in 2025-07-22 11:09
 */
public enum MessageStatus {


    SENDING(0, "发送中"),
    SEND_SUCCESS(1, "发送成功"),
    SEND_FAIL(2, "发送失败")
    ;


    private int status;
    private String desc;

    MessageStatus(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
