package cn.bravedawn.io.bytestream;


import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;

/**
 * @Author : fengx9
 * @Project : 2.JavaTrain
 * @Date : Created in 2023-12-15 10:24
 */
public class FileOutputStreamExample {

    /**
     *  FileOutputStream 是用于将数据写入文件的输出流
     * 可以通过 FileOutputStream 类写入面向字节和面向字符的数据。但是，对于面向字符的数据，最好使用 FileWriter 而不是 FileOutputStream。
     */


    public static void main(String[] args) {

//        writeByte();
//
//        writeString();

        writeStringV2();
    }

    private static void writeByte() {
        try {
            FileOutputStream fout = new FileOutputStream("E:\\testout.txt");
            fout.write(65);
            fout.close();
            System.out.println("success...");
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    private static void writeString() {
        try {
            FileOutputStream fout = new FileOutputStream("E:\\testout.txt");
            String s = "Welcome to javaTpoint.";
            byte b[] = s.getBytes();//converting string into byte array
            fout.write(b);
            fout.close();
            System.out.println("success...");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void writeStringV2() {
        try {
            FileOutputStream fout = new FileOutputStream("E:\\testout.txt");
            String s = "Welcome to javaTpoint.";
            ByteArrayInputStream inputStream = new ByteArrayInputStream(s.getBytes(StandardCharsets.UTF_8));
            byte[] buffer = new byte[1];
            int readBytes = -1;
            while ((readBytes = inputStream.read(buffer)) != -1) {
                fout.write(buffer, 0, readBytes);
            }
            fout.close();
            System.out.println("success...");
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
