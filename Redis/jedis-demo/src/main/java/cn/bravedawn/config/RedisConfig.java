package cn.bravedawn.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author : depers
 * @Date : Created in 2025-11-14 11:16
 */


public class RedisConfig {

    private String host;
    private int port;
    private int timeout;
    private String password;
    private int database;

    private int maxTotal;
    private int maxIdle;
    private int minIdle;
    private long maxWaitMillis;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private boolean testWhileIdle;

    public RedisConfig() {
        loadConfig();
    }

    private void loadConfig() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("redis.properties")) {

            if (input == null) {
                throw new RuntimeException("无法找到 redis.properties 配置文件");
            }

            props.load(input);

            // 读取基本配置
            this.host = props.getProperty("redis.host", "127.0.0.1");
            this.port = Integer.parseInt(props.getProperty("redis.port", "6379"));
            this.timeout = Integer.parseInt(props.getProperty("redis.timeout", "2000"));
            this.password = props.getProperty("redis.password", "");
            this.database = Integer.parseInt(props.getProperty("redis.database", "0"));

            // 读取连接池配置
            this.maxTotal = Integer.parseInt(props.getProperty("redis.pool.maxTotal", "100"));
            this.maxIdle = Integer.parseInt(props.getProperty("redis.pool.maxIdle", "50"));
            this.minIdle = Integer.parseInt(props.getProperty("redis.pool.minIdle", "10"));
            this.maxWaitMillis = Long.parseLong(props.getProperty("redis.pool.maxWaitMillis", "3000"));
            this.testOnBorrow = Boolean.parseBoolean(props.getProperty("redis.pool.testOnBorrow", "true"));
            this.testOnReturn = Boolean.parseBoolean(props.getProperty("redis.pool.testOnReturn", "true"));
            this.testWhileIdle = Boolean.parseBoolean(props.getProperty("redis.pool.testWhileIdle", "true"));

        } catch (IOException e) {
            throw new RuntimeException("加载Redis配置失败", e);
        }
    }

    public JedisPool createJedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        poolConfig.setTestOnBorrow(testOnBorrow);
        poolConfig.setTestOnReturn(testOnReturn);
        poolConfig.setTestWhileIdle(testWhileIdle);

        if (password != null && !password.trim().isEmpty()) {
            return new JedisPool(poolConfig, host, port, timeout, password, database);
        } else {
            return new JedisPool(poolConfig, host, port, timeout, null, database);
        }
    }

    // Getter 方法
    public String getHost() { return host; }
    public int getPort() { return port; }
    public int getTimeout() { return timeout; }
    public String getPassword() { return password; }
    public int getDatabase() { return database; }
}