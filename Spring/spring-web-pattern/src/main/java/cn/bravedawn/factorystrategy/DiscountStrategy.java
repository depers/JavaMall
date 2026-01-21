package cn.bravedawn.factorystrategy;

/**
 * @Author : depers
 * @Date : Created in 2026-01-21 19:18
 */
public interface DiscountStrategy {

    /**
     * 策略唯一标识（非常关键）
     */
    String type();

    /**
     * 计算折扣后的价格
     */
    double calc(double price);
}

