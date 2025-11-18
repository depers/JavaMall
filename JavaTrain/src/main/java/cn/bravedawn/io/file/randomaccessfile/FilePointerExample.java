package cn.bravedawn.io.file.randomaccessfile;

/**
 * @Author : depers
 * @Date : Created in 2025-11-18 13:57
 */
import java.io.RandomAccessFile;
import java.io.IOException;

public class FilePointerExample {

    /**
     * java.io.RandomAccessFile#getFilePointer 方法用于获取当前文件指针的位置，即下一次读取或写入操作将在文件的哪个字节位置开始。
     */
    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile("test.dat", "rw")) {
            // 初始位置
            System.out.println("初始位置: " + file.getFilePointer() + " 字节"); // 0

            // 写入一些数据
            file.writeInt(123);  // 写入4字节的int
            System.out.println("写入int后位置: " + file.getFilePointer() + " 字节"); // 4

            file.writeDouble(45.67);  // 写入8字节的double
            System.out.println("写入double后位置: " + file.getFilePointer() + " 字节"); // 12

            file.writeUTF("Hello");  // 写入字符串（2字节长度 + 字符串内容）
            System.out.println("写入字符串后位置: " + file.getFilePointer() + " 字节"); // 19

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
