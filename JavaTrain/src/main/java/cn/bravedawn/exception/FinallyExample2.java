package cn.bravedawn.exception;

/**
 * @author : depers
 * @program : JavaTrain
 * @date : Created in 2025/5/18 11:05
 */
public class FinallyExample2 {

    public static void main(String[] args) {
        FinallyExample2 finallyExample2 = new FinallyExample2();
        int result = finallyExample2.inc();
        System.out.println(result);
    }


    public int inc() {
        int x;
        try {
            x = 1;
            int i = 1 / 0;
            return x;
        } catch (Exception e) {
            x = 2;
            return x;
        } finally {
            x = 3;
        }
    }
}
