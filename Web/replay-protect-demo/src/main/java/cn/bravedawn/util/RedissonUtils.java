package cn.bravedawn.util;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author : depers
 * @program : replay-protect-demo
 * @date : Created in 2024/10/29 21:38
 */
@Component
@Slf4j
public class RedissonUtils {


    @Autowired
    private RedissonClient redissonClient;

    /**
     * 获取key的value
     * @param key
     * @return
     */
    public Object get(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    /**
     * 删除key的值，若键值存在且删除成功返回true
     * @param key
     * @return
     */
    public boolean del(String key) {
        return redissonClient.getBucket(key).delete();
    }

    /**
     * 设置键值对
     *
     * @param key     键
     * @param value   值
     * @param expired 有效时间，单位:秒
     */
    public void set(String key, String value, long expired) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.set(value, Duration.ofSeconds(expired < 0 ? 10 : expired));

    }


    /**
     * 加分布式锁无过期时间
     * @param lockKey
     * @param waitTime
     * @return
     */
    public boolean tryLock(String lockKey, long waitTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, TimeUnit.SECONDS);
        } catch (Throwable e) {
            log.error("获取所失败");
        }
        return false;
    }


    /**
     * 释放分布式锁
     * @param lockKey
     */
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }


    /**
     * 设置键值对，如果存在则返回false
     * @param key
     * @param value
     * @param expired
     */
    public void setNx(String key, String value, long expired) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        bucket.setIfAbsent(value, Duration.ofSeconds(expired < 0 ? 10 : expired));
    }


    /**
     * 添加键值对到列表中
     * @param key
     * @param value
     * @param expired
     * @param <T>
     */
    public <T> void lPush(String key, T value, long expired) {
        RDeque<T> deque = redissonClient.getDeque(key);
        deque.addFirst(value);
        deque.expire(Duration.ofSeconds(expired));
    }

    /**
     * 批量添加键值对到列表中
     * @param key
     * @param values
     * @param expired
     * @param <T>
     */
    public <T> void lPushForBatch(String key, List<T> values, long expired) {
        RDeque<T> deque = redissonClient.getDeque(key);
        deque.addAll(values);
        deque.expire(Duration.ofSeconds(expired));
    }

    public Object rPop(String key) {
        RDeque<String> deque = redissonClient.getDeque(key);
        return deque.pollLast();
    }

    /**
     * 获取列中中的子列表
     * @param key
     * @param start
     * @param end
     * @return
     * @param <T>
     */
    public <T> List<T> lRange(String key, int start, int end) {
        RList<T> list = redissonClient.getList(key);
        return list.range(start, end);
    }

    /**
     * 删除list
     * @param key
     * @return
     */
    public boolean lDel(String key) {
        RDeque<Object> deque = redissonClient.getDeque(key);
        return deque.delete();
    }


    /**
     * INCR的实现
     * @param key
     * @param expireSeconds
     * @return
     */
    public Long incr(String key, long expireSeconds) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
        if (atomicLong.get() == 0) {
            atomicLong.set(0);
            // 设置有效时间需要设置初值
            atomicLong.expire(Duration.ofSeconds(expireSeconds));
        }
        return atomicLong.incrementAndGet();

    }


}