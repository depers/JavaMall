package cn.bravedawn.factorystrategy;

/**
 * @Author : depers
 * @Date : Created in 2026-01-21 19:20
 */
import org.springframework.stereotype.Component;

/**
 * VIP折扣策略
 */
@Component
public class VipDiscountStrategy implements DiscountStrategy {

    @Override
    public String type() {
        return "VIP";
    }

    @Override
    public double calc(double price) {
        return price * 0.8;
    }
}

