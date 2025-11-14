package cn.bravedawn;


import cn.bravedawn.config.RedisConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.Collections;
/**
 * @Author : depers
 * @Date : Created in 2025-11-14 11:18
 */


public class JedisUtil {

    private static JedisPool jedisPool;

    static {
        RedisConfig redisConfig = new RedisConfig();
        jedisPool = redisConfig.createJedisPool();
    }

    /**
     * 获取 Jedis 实例
     */
    public static Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 关闭 Jedis 连接
     */
    public static void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * 设置字符串值
     */
    public static String set(String key, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.set(key, value);
        }
    }

    /**
     * 设置带过期时间的字符串值
     */
    public static String setex(String key, int seconds, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.setex(key, seconds, value);
        }
    }

    /**
     * 获取字符串值
     */
    public static String get(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.get(key);
        }
    }

    /**
     * 删除键
     */
    public static Long del(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.del(key);
        }
    }

    /**
     * 设置分布式锁
     */
    public static boolean setLock(String key, String value, int expireSeconds) {
        try (Jedis jedis = getJedis()) {
            SetParams params = SetParams.setParams().nx().ex(expireSeconds);
            return "OK".equals(jedis.set(key, value, params));
        }
    }

    /**
     * 释放分布式锁
     */
    public static boolean releaseLock(String key, String value) {
        String luaScript =
                "if redis.call('get', KEYS[1]) == ARGV[1] then " +
                        "    return redis.call('del', KEYS[1]) " +
                        "else " +
                        "    return 0 " +
                        "end";

        try (Jedis jedis = getJedis()) {
            Object result = jedis.eval(luaScript, Collections.singletonList(key),
                    Collections.singletonList(value));
            return Long.valueOf(1L).equals(result);
        }
    }

    /**
     * 关闭连接池
     */
    public static void shutdown() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
        }
    }
}
