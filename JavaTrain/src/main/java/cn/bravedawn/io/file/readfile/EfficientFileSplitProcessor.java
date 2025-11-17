package cn.bravedawn.io.file.readfile;

/**
 * @Author : depers
 * @Date : Created in 2025-11-17 09:11
 */
import java.io.*;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class EfficientFileSplitProcessor {
    private final int totalThreads;
    private final ExecutorService executor;

    public EfficientFileSplitProcessor(int totalThreads) {
        this.totalThreads = totalThreads;
        this.executor = Executors.newFixedThreadPool(totalThreads);
    }

    /**
     * 方法2：基于文件位置拆分，避免预读整个文件
     */
    public void processFileByPosition(String inputFilePath, String outputDir) throws Exception {
        File file = new File(inputFilePath);
        long fileSize = file.length();
        long chunkSize = fileSize / totalThreads;

        Files.createDirectories(Paths.get(outputDir));

        List<Future<?>> futures = new ArrayList<>();

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            long startPos = 0;

            for (int i = 0; i < totalThreads; i++) {
                long endPos = (i == totalThreads - 1) ? fileSize : startPos + chunkSize;

                // 调整结束位置到行尾
                raf.seek(endPos);
                while (endPos < fileSize) {
                    byte b = raf.readByte();
                    endPos++;
                    if (b == '\n') break;
                }

                String outputFile = outputDir + "/part-" + i + ".txt";
                PositionBasedProcessor task = new PositionBasedProcessor(
                        inputFilePath, outputFile, startPos, endPos);
                futures.add(executor.submit(task));

                startPos = endPos;
            }
        }

        // 等待所有任务完成
        for (Future<?> future : futures) {
            future.get();
        }

        executor.shutdown();
    }

    static class PositionBasedProcessor implements Runnable {
        private final String inputFile;
        private final String outputFile;
        private final long startPos;
        private final long endPos;

        public PositionBasedProcessor(String inputFile, String outputFile, long startPos, long endPos) {
            this.inputFile = inputFile;
            this.outputFile = outputFile;
            this.startPos = startPos;
            this.endPos = endPos;
        }

        @Override
        public void run() {
            try (RandomAccessFile raf = new RandomAccessFile(inputFile, "r");
                 BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {

                raf.seek(startPos);
                long currentPos = startPos;
                int lineCount = 0;

                // 如果不是文件开头，跳过第一行（可能是被截断的行）
                if (startPos > 0) {
                    raf.readLine();
                    currentPos = raf.getFilePointer();
                }

                // 读取指定范围内的行
                while (currentPos < endPos) {
                    String line = raf.readLine();
                    if (line == null) break;

                    // 处理行数据
                    String processedLine = processLine(line);
                    writer.write(processedLine);
                    writer.newLine();

                    lineCount++;
                    currentPos = raf.getFilePointer();

                    if (lineCount % 10000 == 0) {
                        System.out.println(Thread.currentThread().getName() +
                                " 已处理: " + lineCount + " 行");
                    }
                }

                System.out.println(Thread.currentThread().getName() +
                        " 处理完成，共 " + lineCount + " 行");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private String processLine(String line) {
            // 实际的处理逻辑
            return line.trim().toUpperCase();
        }
    }
}