package cn.bravedawn.distributedlock;

import cn.bravedawn.config.RedisConfig;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.JedisPool;

/**
 * @Author : depers
 * @Date : Created in 2025-11-21 13:32
 */
@Slf4j
public class Test2 {

    public static void main(String[] args) throws InterruptedException {
        RedisConfig redisConfig = new RedisConfig();
        JedisPool jedisPool = redisConfig.createJedisPool();
//        testLock(jedisPool);
        testLockNoClose(jedisPool);
        log.info("加锁逻辑执行完成");
        for (int i = 0; i < 30; i++) {
            log.info("处理主线程逻辑中。。。。");
            Thread.sleep(1000);
        }
    }

    private static void testLock(JedisPool jedisPool) {
        try (RedisDistributedLock redisDistributedLock = new RedisDistributedLock(jedisPool, "lock")) {
            try {
                boolean b = redisDistributedLock.tryLock(0);
                if (b) {
                    log.info("获取锁成功");
                    for (int i = 0; i < 30; i++) {
                        log.info("处理逻辑中。。。。");
                        Thread.sleep(1000);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().isInterrupted();
            }
        }
    }


    /**
     * 错误的使用，没有释放线程资源
     * @param jedisPool
     */
    private static void testLockNoClose(JedisPool jedisPool) {
        RedisDistributedLock redisDistributedLock = new RedisDistributedLock(jedisPool, "lock");
        try {
            boolean b = redisDistributedLock.tryLock(0);
            if (b) {
                log.info("获取锁成功");
                for (int i = 0; i < 30; i++) {
                    log.info("处理逻辑中。。。。");
                    Thread.sleep(1000);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().isInterrupted();
        } finally {
            redisDistributedLock.unlock();
        }
    }

}
