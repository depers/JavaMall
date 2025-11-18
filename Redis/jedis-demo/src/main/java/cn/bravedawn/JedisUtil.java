package cn.bravedawn;


import cn.bravedawn.config.RedisConfig;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import java.util.*;
import java.util.function.Supplier;

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
                        "end ";

        try (Jedis jedis = getJedis()) {
            Object result = jedis.eval(luaScript, Collections.singletonList(key),
                    Collections.singletonList(value));
            return Long.valueOf(1L).equals(result);
        }
    }

    /**
     * 存储对象为 JSON
     */
    public static <T> String setObject(String key, T obj) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = JsonUtil.toJson(obj);
            return jedis.set(key, json);
        }
    }

    /**
     * 存储对象为 JSON 并设置过期时间
     */
    public static <T> String setObject(String key, T obj, int seconds) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = JsonUtil.toJson(obj);
            return jedis.setex(key, seconds, json);
        }
    }

    /**
     * 获取对象
     */
    public static <T> T getObject(String key, Class<T> clazz) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = jedis.get(key);
            if (json == null) {
                return null;
            }
            return JsonUtil.fromJson(json, clazz);
        }
    }

    /**
     * 获取复杂对象（如 List、Map 等）
     */
    public static <T> T getObject(String key, TypeReference<T> typeReference) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = jedis.get(key);
            if (json == null) {
                return null;
            }
            return JsonUtil.fromJson(json, typeReference);
        }
    }

    /**
     * 存储 List 对象
     */
    public static <T> String setList(String key, List<T> list) {
        return setObject(key, list);
    }

    /**
     * 获取 List 对象
     */
    public static <T> List<T> getList(String key, Class<T> elementClass) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = jedis.get(key);
            if (json == null) {
                return null;
            }
            return JsonUtil.fromJson(json, new TypeReference<List<T>>() {
            });
        }
    }

    /**
     * 存储 Map 对象
     */
    public static <K, V> String setMap(String key, Map<K, V> map) {
        return setObject(key, map);
    }

    /**
     * 获取 Map 对象
     */
    public static <K, V> Map<K, V> getMap(String key, Class<K> keyClass, Class<V> valueClass) {
        try (Jedis jedis = jedisPool.getResource()) {
            String json = jedis.get(key);
            if (json == null) {
                return null;
            }
            return JsonUtil.fromJson(json, new TypeReference<Map<K, V>>() {
            });
        }
    }

    public <T> T safeGet(String key, Class<T> clazz, Supplier<T> cacheLoader, int timeout) {
        T result = getObject(key, clazz);
        if (Objects.nonNull(result) || (result instanceof String && StringUtils.isNotBlank((String) result))) {
            return result;
        }


        T value = cacheLoader.get();
        if (value == null) {
            setObject(key, "", timeout);
        } else {
            setObject(key, value, timeout);
        }
        return value;
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
