package cn.bravedawn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description : 商店
 * @Author : depers
 * @Project : fastjson2-demo
 * @Date : Created in 2025-02-10 10:02
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Store {


    private String name;
    private Fruit fruit;
}
