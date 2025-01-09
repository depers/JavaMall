package cn.bravedawn.basic.flow;

public class ContinueExample2 {

    /**
     * 带标签的continue关键字
     * 跳出某一次循环之后，会继续执行循环的逻辑，这一点与break正好相反
     */
    public static void main(String[] args) {

        retry:
        for (int i = 0; i < 10; i++) {

            if (i == 3) {
                continue retry;
            }

            System.out.println("i = " + i);
        }

        System.out.println("done");
    }
}
