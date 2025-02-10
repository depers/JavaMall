package cn.bravedawn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @Description : 苹果水果
 * @Author : depers
 * @Project : fastjson2-demo
 * @Date : Created in 2025-02-10 10:04
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Apple implements Fruit{


    private BigDecimal price;
}
