package cn.bravedawn.constant;

/**
 * @author : depers
 * @program : replay-protect-demo
 * @date : Created in 2024/10/29 21:36
 */
public enum RedisKey {

    REPLAY_PROTECT_NONCE("replay_protect:redis:replay_protect:nonce_%s", 60, "key为防重放的随机数，value为1")
    ;


    private String key;
    private String desc;
    private long ttl;


    RedisKey(String key, long ttl, String desc) {
        this.key = key;
        this.desc = desc;
        this.ttl = ttl;
    }


    public String getDesc() {
        return desc;
    }

    public String getKey() {
        return key;
    }

    public long getTtl() {
        return ttl;
    }
}
