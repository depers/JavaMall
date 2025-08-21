package cn.bravedawn.contant;

/**
 * @Author : depers
 * @Date : Created in 2025-08-21 15:03
 */
public enum RedisKey {


    IDEMPOTENT_REDIS_KEY("mq:consume_flag:%s", "pulsar消费端幂等性控制", 3600),
    ;


    private String key;
    private String desc;
    private long ttl;


    RedisKey(String key, String desc, long ttl) {
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
