package cn.bravedawn.toolkit;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author : depers
 * @program : redisson-demo
 * @date : Created in 2024/4/29 20:07
 */

@Slf4j
@AllArgsConstructor
public class RedissonUtils {


    /**
     * 工具类
     */

    private final RedissonClient redissonClient;

    /**
     * 获取值
     * @param key 键
     * @return
     */
    public String get(String key) {
        RBucket<String> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    /**
     * 删除键值对
     * @param key 键
     * @return
     */
    public boolean del(String key) {
        return redissonClient.getBucket(key).delete();
    }

    /**
     * 设置键值对，并设置有效时间
     * @param key 键
     * @param vaue 值
     * @param expired 有效时间
     */
    public void set(String key, String vaue, long expired) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        bucket.set(vaue, Duration.ofSeconds(expired < 0 ? 10 : expired));
    }

    /**
     * 获取锁
     * @param lockName 锁名称
     * @param waitTime 等待时间
     * @return
     */
    public boolean tryLock(String lockName, int waitTime) {
        RLock lock = redissonClient.getLock(lockName);
        try {
            return lock.tryLock(waitTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("获取锁失败", e);
        }
        return false;
    }


    /**
     * 解锁
     * @param lockName 锁名称
     */
    public void unlock(String lockName) {
        RLock lock = redissonClient.getLock(lockName);
        lock.unlock();
    }

    /**
     * 原子性设置键值对，如果存在返回false
     * @param key 键
     * @param value 值
     * @param expired 有效时间
     * @return
     */
    public boolean setNx(String key, Object value, long expired) {
        RBucket<Object> bucket = redissonClient.getBucket(key);
        return bucket.setIfAbsent(value, Duration.ofSeconds(expired < 0 ? 10 : expired));
    }

    /**
     * 添加键值对到列表中
     * @param key
     * @param value
     * @param expired
     */
    public <T> void lPush(String key, T value, long expired) {
        RDeque<T> deque = redissonClient.getDeque(key);
        deque.addFirst(value);
        deque.expire(Duration.ofSeconds(expired));
    }

    /**
     * 批量添加键值对到列表中
     * @param key
     * @param list
     * @param expired
     * @param <T>
     */
    public <T> void lBatchPush(String key, List<T> list, long expired) {
        RDeque<T> deque = redissonClient.getDeque(key);
        deque.addAll(list);
        deque.expire(Duration.ofSeconds(expired));
    }


    /**
     * 从列表中获取一个元素
     * @param key
     * @return
     */
    public Object rPop(String key) {
        Deque<Object> deque = redissonClient.getDeque(key);
        return deque.pollLast();
    }

    /**
     * 获取列表中的子列表
     * @param key 键
     * @param start 开始索引，0代表从开头开始
     * @param end 结束索引，-1代表列表末尾
     * @return
     * @param <T>
     */
    public <T> List<T> LRange(String key, int start, int end) {
        RList<T> rList = redissonClient.getList(key);
        return rList.range(start, end);
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
     * incr命令的实现
     * @param key
     * @param expireSeconds
     * @return
     */
    public Long incr(String key, long expireSeconds) {
        RAtomicLong atomicLong = redissonClient.getAtomicLong(key);
        if (atomicLong.get() == 0) {
            atomicLong.set(0);
            // 设置有效期只需设置初始值
            atomicLong.expire(Duration.ofSeconds(expireSeconds));
        }
        return atomicLong.incrementAndGet();
    }


}
