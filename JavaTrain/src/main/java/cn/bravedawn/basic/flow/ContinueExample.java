package cn.bravedawn.basic.flow;

/**
 * @author : depers
 * @program : JavaTrain
 * @date : Created in 2022/1/12 22:05
 */
public class ContinueExample {

    /**
     * 跳过本次循环
     */

    public static void main(String[] args) {
        for (int i = 1; i <= 3; i++) {

            for (int j = 1; j <= 3; j++) {
                System.out.println("i=" + i + " j=" + j);

                if (i == 2) {
                    System.out.println("***" + "i=" + i + " j=" + j);
                    continue;
                }
                System.out.println("------------------------");
            }
        }
    }
}
