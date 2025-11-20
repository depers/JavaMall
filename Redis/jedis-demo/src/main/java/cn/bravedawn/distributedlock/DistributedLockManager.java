package cn.bravedawn.distributedlock;

/**
 * @Author : depers
 * @Date : Created in 2025-11-20 11:35
 */
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 分布式锁管理器
 */
public class DistributedLockManager {

    private final JedisPool jedisPool;
    private final Map<String, RedisDistributedLock> activeLocks;

    public DistributedLockManager(String host, int port) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(100);
        poolConfig.setMaxIdle(20);
        poolConfig.setMinIdle(5);
        poolConfig.setMaxWaitMillis(3000);

        this.jedisPool = new JedisPool(poolConfig, host, port);
        this.activeLocks = new ConcurrentHashMap<>();
    }

    public DistributedLockManager(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        this.activeLocks = new ConcurrentHashMap<>();
    }

    /**
     * 获取分布式锁实例
     */
    public RedisDistributedLock getLock(String lockKey) {
        return activeLocks.computeIfAbsent(lockKey,
                key -> new RedisDistributedLock(jedisPool, key));
    }

    /**
     * 使用锁执行任务（自动获取和释放锁）
     */
    public <T> T executeWithLock(String lockKey, long timeout, LockTask<T> task) {
        RedisDistributedLock lock = getLock(lockKey);

        try {
            if (lock.tryLock(timeout)) {
                return task.execute();
            } else {
                throw new RuntimeException("Failed to acquire lock: " + lockKey);
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * 使用锁执行任务（默认超时时间）
     */
    public <T> T executeWithLock(String lockKey, LockTask<T> task) {
        return executeWithLock(lockKey, RedisDistributedLock.DEFAULT_ACQUIRE_TIMEOUT, task);
    }

    /**
     * 清理资源
     */
    public void close() {
        activeLocks.values().forEach(RedisDistributedLock::close);
        activeLocks.clear();
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
        }
    }

    /**
     * 锁任务接口
     */
    @FunctionalInterface
    public interface LockTask<T> {
        T execute();
    }
}