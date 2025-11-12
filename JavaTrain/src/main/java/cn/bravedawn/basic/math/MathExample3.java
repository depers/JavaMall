package cn.bravedawn.basic.math;

import cn.hutool.core.util.NumberUtil;

/**
 * @Author : depers
 * @Date : Created in 2025-11-12 09:31
 */
public class MathExample3 {

    /**
     * 除法，向上取整，向下取整
     */

    public static void main(String[] args) {
        int i = Math.floorDiv(10, 3);
        System.out.println(i);

        int i1 = NumberUtil.ceilDiv(10, 3);
        System.out.println(i1);

        double div = NumberUtil.div(10, 3);
        System.out.println(div);
    }
}
