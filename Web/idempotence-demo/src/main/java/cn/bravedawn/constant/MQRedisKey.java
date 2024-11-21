package cn.bravedawn.constant;

/**
 * @author : depers
 * @program : replay-protect-demo
 * @date : Created in 2024/11/21 21:19
 */
public enum MQRedisKey {

    MQ_CONSUME_FLAG("ycsb:mq:consume_flag:%s", 3600, "消息消费幂等性控制"),
    REDIS_MQ_CONSUME_TIME("ycsb:mq:consume_time:%s", 3600, "消息消费的次数记录")
    ;


    private String key;
    private String desc;
    private long ttl;


    MQRedisKey(String key, long ttl, String desc) {
        this.key = key;
        this.desc = desc;
        this.ttl = ttl;
    }

    public String getKey() {
        return key;
    }


    public String getDesc() {
        return desc;
    }

    public long getTtl() {
        return ttl;
    }

}