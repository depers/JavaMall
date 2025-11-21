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
 * 带有完善资源管理的分布式锁
 */
@Slf4j
public class RedisDistributedLock implements AutoCloseable {

    private final JedisPool jedisPool;
    private final String lockKey;
    private final String lockValue;
    private final long expireTime; // 锁的过期时间，单位：毫秒
    private final long watchDogInterval; // 看门狗检查间隔，单位：毫秒

    private volatile boolean isLocked = false;
    private volatile boolean isClosed = false;
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
        this.lockValue = UUID.randomUUID().toString() + ":" + Thread.currentThread().getId();
        this.expireTime = expireTime;
        this.watchDogInterval = watchDogInterval;
        this.watchDogExecutor = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "RedisLock-WatchDog-" + lockKey);
            t.setDaemon(true);
            return t;
        });
    }

    public boolean tryLock(long timeout) {
        if (isClosed) {
            log.error("Lock has been closed");
            return false;
        }

        long endTime = System.currentTimeMillis() + timeout;

        try (Jedis jedis = jedisPool.getResource()) {
            SetParams params = SetParams.setParams().nx().px(expireTime);
            String result = jedis.set(lockKey, lockValue, params);
            if ("OK".equals(result)) {
                isLocked = true;
                startWatchDog();
                return true;
            }

            while (System.currentTimeMillis() < endTime) {
                result = jedis.set(lockKey, lockValue, params);
                if ("OK".equals(result)) {
                    isLocked = true;
                    startWatchDog();
                    return true;
                }

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

    public boolean tryLock() {
        return tryLock(DEFAULT_ACQUIRE_TIMEOUT);
    }

    /**
     * 释放锁并清理资源
     */
    public void unlock() {
        if (!isLocked || isClosed) {
            return;
        }

        stopWatchDog();

        try (Jedis jedis = jedisPool.getResource()) {
            String luaScript =
                    "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                            "   return redis.call('del', KEYS[1]) " +
                            "else " +
                            "   return 0 " +
                            "end";

            Long result = (Long) jedis.eval(luaScript,
                    Collections.singletonList(lockKey),
                    Collections.singletonList(lockValue));

            if (result == 1) {
                log.info("Successfully released lock: " + lockKey);
            } else {
                log.info("Lock was already released or expired: " + lockKey);
            }
        } catch (Exception e) {
            System.err.println("Error releasing lock: " + lockKey + ", " + e.getMessage());
        } finally {
            isLocked = false;
        }
    }

    /**
     * 实现AutoCloseable接口，支持try-with-resources
     */
    @Override
    public void close() {
        if (!isClosed) {
            unlock();
            shutdownWatchDogExecutor();
            isClosed = true;
        }
    }

    private void startWatchDog() {
        if (watchDogFuture != null && !watchDogFuture.isCancelled()) {
            watchDogFuture.cancel(true);
        }

        watchDogFuture = watchDogExecutor.scheduleAtFixedRate(() -> {
            if (isLocked && !isClosed) {
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
                    log.error("WatchDog error: " + e.getMessage());
                    isLocked = false;
                }
            }
        }, watchDogInterval, watchDogInterval, TimeUnit.MILLISECONDS);
    }

    private void stopWatchDog() {
        if (watchDogFuture != null) {
            watchDogFuture.cancel(true);
            watchDogFuture = null;
        }
    }

    private void shutdownWatchDogExecutor() {
        if (watchDogExecutor != null && !watchDogExecutor.isShutdown()) {
            watchDogExecutor.shutdown();
            try {
                if (!watchDogExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    watchDogExecutor.shutdownNow();
                }
            } catch (InterruptedException e) {
                watchDogExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}
