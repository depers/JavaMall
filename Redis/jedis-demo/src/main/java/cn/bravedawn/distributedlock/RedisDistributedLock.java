package cn.bravedawn.distributedlock;


import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @Author : depers
 * @Date : Created in 2025-11-20 11:32
 */



/**
 * 带有看门狗功能的分布式锁
 */
@Slf4j
public class RedisDistributedLock {

    private final JedisPool jedisPool;
    private final String lockKey;
    private final String lockValue;
    private final long expireTime; // 锁的过期时间，单位：毫秒
    private final long watchDogInterval; // 看门狗检查间隔，单位：毫秒

    private volatile boolean isLocked = false;
    private ScheduledExecutorService watchDogExecutor;
    private ScheduledFuture<?> watchDogFuture;

    // 默认配置
    private static final long DEFAULT_EXPIRE_TIME = 30000; // 30秒
    private static final long DEFAULT_WATCH_DOG_INTERVAL = 10000; // 10秒
    protected static final long DEFAULT_ACQUIRE_TIMEOUT = 5000; // 5秒获取超时

    public RedisDistributedLock(JedisPool jedisPool, String lockKey) {
        this(jedisPool, lockKey, DEFAULT_EXPIRE_TIME, DEFAULT_WATCH_DOG_INTERVAL);
    }

    public RedisDistributedLock(JedisPool jedisPool, String lockKey, long expireTime, long watchDogInterval) {
        this.jedisPool = jedisPool;
        this.lockKey = lockKey;
        this.lockValue = UUID.randomUUID().toString();
        this.expireTime = expireTime;
        this.watchDogInterval = watchDogInterval;
        this.watchDogExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "RedisLock-WatchDog-" + lockKey);
            t.setDaemon(true);
            return t;
        });
    }

    /**
     * 尝试获取锁
     * @param timeout 获取锁的超时时间，单位：毫秒
     * @return 是否获取成功
     */
    public boolean tryLock(long timeout) {
        long endTime = System.currentTimeMillis() + timeout;

        try (Jedis jedis = jedisPool.getResource()) {
            SetParams params = SetParams.setParams().nx().px(expireTime);

            while (System.currentTimeMillis() < endTime) {
                String result = jedis.set(lockKey, lockValue, params);
                if ("OK".equals(result)) {
                    isLocked = true;
                    startWatchDog();
                    return true;
                }

                // 短暂休眠后重试
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("Failed to acquire lock: " + lockKey, e);
        }

        return false;
    }

    /**
     * 获取锁（使用默认超时时间）
     */
    public boolean tryLock() {
        return tryLock(DEFAULT_ACQUIRE_TIMEOUT);
    }

    /**
     * 释放锁
     */
    public void unlock() {
        if (!isLocked) {
            return;
        }

        stopWatchDog();

        try (Jedis jedis = jedisPool.getResource()) {
            // 使用Lua脚本保证原子性：只有锁的value匹配时才删除
            String luaScript =
                    "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                            "   return redis.call('del', KEYS[1]) " +
                            "else " +
                            "   return 0 " +
                            "end";

            jedis.eval(luaScript, Collections.singletonList(lockKey),
                    Collections.singletonList(lockValue));
        } catch (Exception e) {
            log.error("Failed to release lock: " + lockKey, e);
        } finally {
            isLocked = false;
        }
    }

    /**
     * 启动看门狗，定期续期
     */
    private void startWatchDog() {
        if (watchDogFuture != null && !watchDogFuture.isCancelled()) {
            watchDogFuture.cancel(true);
        }

        watchDogFuture = watchDogExecutor.scheduleAtFixedRate(() -> {
            if (isLocked) {
                try (Jedis jedis = jedisPool.getResource()) {
                    String luaScript =
                            "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                                    "   return redis.call('pexpire', KEYS[1], ARGV[2]) " +
                                    "else " +
                                    "   return 0 " +
                                    "end";

                    Long result = (Long) jedis.eval(luaScript,
                            Collections.singletonList(lockKey),
                            Arrays.asList(lockValue, String.valueOf(expireTime)));

                    if (result == 1) {
                        log.info("WatchDog renewed lock: " + lockKey);
                    } else {
                        log.info("WatchDog failed to renew lock: " + lockKey);
                        isLocked = false;
                    }
                } catch (Exception e) {
                    log.error("WatchDog error for lock: " + lockKey + ", " + e.getMessage());
                    isLocked = false;
                }
            }
        }, watchDogInterval, watchDogInterval, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止看门狗
     */
    private void stopWatchDog() {
        if (watchDogFuture != null) {
            watchDogFuture.cancel(true);
            watchDogFuture = null;
        }
    }

    /**
     * 关闭资源
     */
    public void close() {
        if (isLocked) {
            unlock();
        }
        if (watchDogExecutor != null && !watchDogExecutor.isShutdown()) {
            watchDogExecutor.shutdown();
        }
    }

    public boolean isLocked() {
        return isLocked;
    }

    public String getLockKey() {
        return lockKey;
    }
}
