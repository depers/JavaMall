package cn.bravedawn.factorystrategy;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author : depers
 * @Date : Created in 2026-01-21 19:50
 */
@Service
public class DiscountStrategyFactory {

    private final Map<String, DiscountStrategy> discountStrategyMap = new HashMap<>();

    public DiscountStrategyFactory(List<DiscountStrategy> discountStrategyList) {
        discountStrategyList.forEach(discountStrategy -> {
            discountStrategyMap.put(discountStrategy.type(), discountStrategy);
        });
    }

    public DiscountStrategy get(String type) {
        return discountStrategyMap.get(type);
    }
}
