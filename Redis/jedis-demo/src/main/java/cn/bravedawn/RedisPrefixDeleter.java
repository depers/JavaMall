package cn.bravedawn;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2025-12-03 14:31
 */
public class RedisPrefixDeleter {

    public static long deleteByPrefix(Jedis jedis, String prefix) {
        String cursor = "0";
        ScanParams scanParams = new ScanParams().match(prefix + "*").count(100);
        long deletedCount = 0;

        do {
            ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
            cursor = scanResult.getCursor();
            List<String> keys = scanResult.getResult();

            if (!keys.isEmpty()) {
                jedis.del(keys.toArray(new String[0]));
                deletedCount += keys.size();
            }
        } while (!"0".equals(cursor));

        return deletedCount;
    }
}
