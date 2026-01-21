package cn.bravedawn.factorystrategy;

/**
 * @Author : depers
 * @Date : Created in 2026-01-21 19:48
 */
import org.springframework.stereotype.Component;

/**
 * 满减策略
 */
@Component
public class FullReductionStrategy implements DiscountStrategy {

    @Override
    public String type() {
        return "FULL";
    }

    @Override
    public double calc(double price) {
        return price - 20;
    }
}

