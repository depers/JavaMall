package cn.bravedawn.basic.flow;

public class BreakExample2 {

    /**
     * 带标签的break关键字
     * 在跳出循环之后，不会继续再执行for循环的逻辑，会执行循环后续的逻辑
     */

    public static void main(String[] args) {

        retry:
        for (int i = 0; i < 10; i++) {
            System.out.println("i = " + i);
            if (i == 3) {
                break retry;
            }
        }

        System.out.println("done");
    }
}
