package cn.bravedawn.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * @author : depers
 * @program : idempotence-demo
 * @date : Created in 2024/11/13 21:56
 */
public class SnowflakeUtil {

    private static final Snowflake snowflake;

    static {
        long workId = getWorkId();
        long dataCenterId = getDataCenterId();
        snowflake = IdUtil.getSnowflake(workId, dataCenterId);
    }

    public static Long getWorkId() {
        try {
            String hostAddress = Inet4Address.getLocalHost().getHostAddress();
            int[] ints = StringUtils.toCodePoints(hostAddress);
            int sums = 0;
            for (int b : ints) {
                sums = sums + b;
            }
            return (long) (sums % 32);
        } catch (UnknownHostException e) {
            return RandomUtils.nextLong(0, 31);
        }

    }


    public static Long getDataCenterId() {
        try {
            String hostName = SystemUtils.getHostName();
            int[] ints = StringUtils.toCodePoints(hostName);
            int sums = 0;
            for (int i : ints) {
                sums = sums + i;
            }
            return (long) (sums % 32);
        } catch (Exception e) {
            return RandomUtils.nextLong(0, 31);
        }
    }

    public static long getId() {
        return snowflake.nextId();
    }

}