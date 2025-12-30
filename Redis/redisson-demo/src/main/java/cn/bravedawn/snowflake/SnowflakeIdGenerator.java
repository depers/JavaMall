package cn.bravedawn.snowflake;


import java.lang.management.ManagementFactory;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @Author : depers
 * @Date : Created in 2025-12-30 14:39
 *
 * 随机生成snowflake算法的数据中心id和机器id
 */
public class SnowflakeIdGenerator {

    /**
     * 自动初始化 datacenterId 和 workerId
     * 返回一个数组：[datacenterId, workerId]
     */
    public static long[] initIds() {
        long maxId = 31L; // 5bit 的最大值
        long datacenterId = getDatacenterId(maxId);
        long workerId = getWorkerId(datacenterId, maxId);

        return new long[]{datacenterId, workerId};
    }

    /**
     * 基于 MAC 地址计算 DatacenterId
     */
    private static long getDatacenterId(long maxDatacenterId) {
        long id = 0L;
        try {
            Enumeration<NetworkInterface> el = NetworkInterface.getNetworkInterfaces();
            while (el.hasMoreElements()) {
                NetworkInterface ni = el.nextElement();
                byte[] mac = ni.getHardwareAddress();
                if (mac != null && mac.length > 0) {
                    // 取 MAC 地址的最后两个字节进行位运算
                    id = ((0x000000FFL & (long) mac[mac.length - 1])
                            | (0x0000FF00L & (((long) mac[mac.length - 2]) << 8))) >> 6;
                    id = id % (maxDatacenterId + 1);
                    break;
                }
            }
        } catch (Exception e) {
            // 异常时降级：使用当前时间的哈希值
            id = System.currentTimeMillis() % (maxDatacenterId + 1);
        }
        return id;
    }

    /**
     * 基于 进程PID 计算 WorkerId
     */
    private static long getWorkerId(long datacenterId, long maxWorkerId) {
        StringBuilder mpid = new StringBuilder();
        mpid.append(datacenterId);

        // 获取 JVM 进程名称，格式通常为 "PID@hostname"
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (name != null && !name.isEmpty()) {
            // 提取 PID 部分
            mpid.append(name.split("@")[0]);
        }
        // 使用哈希值并取模，确保在 0-31 之间
        return (long) (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    public static void main(String[] args) {
        long[] ids = initIds();
        System.out.println("自动分配结果：");
        System.out.println("Datacenter ID: " + ids[0]);
        System.out.println("Worker ID:     " + ids[1]);
    }

}
