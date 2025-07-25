package cn.bravedawn.io.file.writelines;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;

/**
 * @Description : 写入多行字符串列表到文件中
 * @Author : depers
 * @Project : JavaTrain
 * @Date : Created in 2025-07-25 10:12
 */
@Slf4j
public class WriteLinesExample {


    public static void main(String[] args) throws IOException {

        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        String path = System.getProperty("user.dir") + "/src/main/resources/doc/" + "example4.txt";
        writeLines(path, lines, "GBK", false);
    }


    /**
     * 使用File.write()可以实现自动换行，生成后的文件最后一行会有一个空行
     */
    private static void writeFileV1() {
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        Path path = Paths.get(System.getProperty("user.dir") + "/src/main/resources/doc/" + "example.txt");

        try {
            Files.write(path, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,   // 如果文件不存在则创建
                    StandardOpenOption.APPEND);  // 追加模式
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 使用 BufferedWriter（适合追加前做额外处理），可以去除文件的最后一行空行
     */
    private static void writeFileV2() {
        List<String> lines = Arrays.asList("Line A", "Line B");
        File file = new File(System.getProperty("user.dir") + "/src/main/resources/doc/" + "example2.txt");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) { // true = 追加模式
            for (String line : lines) {
                writer.write(line);
                writer.newLine(); // 写入换行符
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 使用 PrintWriter（适合简单日志追加），可以实现换行和不换行
     */
    private static void writeFileV3() throws IOException {
        List<String> lines = Arrays.asList("Line 1", "Line 2", "Line 3");
        String path = System.getProperty("user.dir") + "/src/main/resources/doc/" + "example3.txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(path, true))) {
            for (String line : lines) {
                pw.println(line);
            }
        }
    }


    /**
     * 写入文件，控制编码和是否在最后一行保留空格
     * @param filePath
     * @param lines
     * @param charset
     * @param isRetainLastBlankLine
     */
    private static void writeLines(String filePath, List<String> lines, String charset, boolean isRetainLastBlankLine) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath, true), charset))) {
            int len = lines.size();
            for (int i = 0; i < len; i++) {
                writer.write(lines.get(i));
                if (i == len - 1 && !isRetainLastBlankLine) {
                    // 什么都不做
                } else {
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            log.error("写入文件出现异常", e);
        }
    }
}
