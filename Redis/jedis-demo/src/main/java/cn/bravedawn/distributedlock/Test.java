package cn.bravedawn.distributedlock;

import cn.bravedawn.config.RedisConfig;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPool;

/**
 * @Author : depers
 * @Date : Created in 2025-11-20 11:42
 */
@Slf4j
public class Test {

    public static void main(String[] args) {
        RedisConfig redisConfig = new RedisConfig();
        JedisPool jedisPool = redisConfig.createJedisPool();

        try(RedisDistributedLock redisDistributedLock = new RedisDistributedLock(jedisPool, "lock")) {
            try {
                boolean b = redisDistributedLock.tryLock(0);
                while (b) {
                    log.info("处理逻辑中。。。。");
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().isInterrupted();
            }
        }
    }
}
