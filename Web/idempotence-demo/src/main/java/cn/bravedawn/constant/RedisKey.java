package cn.bravedawn.constant;

/**
 * @author : depers
 * @program : idempotence-demo
 * @date : Created in 2024/11/21 22:15
 */
public enum RedisKey {

    IDEMPOTENT_CONTROL("idempotent:%s", 45, "幂等性控制redis key前缀"),
    ;

    private String key;
    private String desc;
    private long ttl;


    RedisKey(String key, long ttl, String desc) {
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
