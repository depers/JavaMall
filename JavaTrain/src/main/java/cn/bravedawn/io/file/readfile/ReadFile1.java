package cn.bravedawn.io.file.readfile;

import cn.bravedawn.io.utils.ResourceHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * @author : depers
 * @program : JavaTrain
 * @description:
 * @date : Created in 2023/2/7 17:44
 */
public class ReadFile1 {

    /**
     * 方法一：使用Java 8 Files.lines()去读取文件，内部使用try-with-resources包装代码，无需过多操作'
     *
     * 其中：foreach() 是进行遍历但不保证顺序
     *      forEachOrdered()是进行遍历会保持顺序
     */

    public static void main(String[] args) throws IOException {
        String fileName = ResourceHelper.getAbsoluteFilePath("doc/test.txt");

        // auto-close the resources
        try (Stream<String> lines = Files.lines(Paths.get(fileName))) {
            // does not preserve order
            lines.forEach(System.out::println);

            // preserve order
            // lines.forEachOrdered(System.out::println);
        }


    }

}
