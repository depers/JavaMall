package cn.bravedawn.factorystrategy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author : depers
 * @Date : Created in 2026-01-21 19:59
 */


@SpringBootTest
public class DiscountStrategyFactoryTest {


    @Autowired
    private DiscountStrategyFactory discountStrategyFactory;

    @DisplayName("测试简单工厂模式和策略模式的结合使用")
    @Test
    public void testDiscountStrategy() {
        DiscountStrategy discountStrategy = discountStrategyFactory.get("FULL");
        double calc = discountStrategy.calc(100);
        System.out.println(calc);
    }
}
