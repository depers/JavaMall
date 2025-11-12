package cn.bravedawn.basic.math;

/**
 * @Author : depers
 * @Date : Created in 2025-11-12 09:18
 */
public class MathExample2 {

    /**
     * 将long类型转换为int
     */

    public static void main(String[] args) {
        try {
            // 1. 安全转换
            long safeLong = 1000L;
            int intValue1 = Math.toIntExact(safeLong);
            System.out.println("安全转换: " + intValue1);

            // 2. 超出范围 - 会抛出异常
            long largeLong = 3_000_000_000L;
            int intValue2 = Math.toIntExact(largeLong); // 抛出 ArithmeticException

        } catch (ArithmeticException e) {
            System.out.println("转换错误: long值超出int范围");
        }
    }
}
