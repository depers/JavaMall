package cn.bravedawn.io.file.randomaccessfile;

import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author : depers
 * @Date : Created in 2025-11-17 17:42
 */
public class RandomAccessFileLimitTest {

    public static void main(String[] args) {
        String fileName = "test.txt";
        createTestFile(fileName);

        int maxFiles = 0;
        List<RandomAccessFile> fileList = new ArrayList<>();

        try {
            while (true) {
                // 每次创建新的 RandomAccessFile
                RandomAccessFile raf = new RandomAccessFile(fileName, "r");
                fileList.add(raf);
                maxFiles++;

                if (maxFiles % 100 == 0) {
                    System.out.println("已创建: " + maxFiles + " 个 RandomAccessFile");
                }
            }
        } catch (Exception e) {
            System.out.println("达到限制，最多创建: " + maxFiles + " 个");
            System.out.println("错误信息: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 清理资源
            for (RandomAccessFile raf : fileList) {
                try { raf.close(); } catch (Exception e) {}
            }
        }
    }

    private static void createTestFile(String fileName) {
        try (FileWriter fw = new FileWriter(fileName)) {
            fw.write("Test content");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
