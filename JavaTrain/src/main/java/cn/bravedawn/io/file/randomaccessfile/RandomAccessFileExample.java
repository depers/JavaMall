package cn.bravedawn.io.file.randomaccessfile;

import cn.bravedawn.io.utils.ResourceHelper;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @Author : fengx9
 * @Project : 2.JavaTrain
 * @Date : Created in 2023-12-18 16:28
 */
public class RandomAccessFileExample {

    /**
     * 此类用于读取和写入随机访问文件。随机访问文件的行为类似于一个大型字节数组。数组中隐含着一个称为文件指针的游标，通过移动游标，我们执行读写操作。
     * 如果在读取所需的字节数之前达到文件末尾，则会引发 EOFException。它是一种 IOException。
     */

    static final String FILEPATH = ResourceHelper.getAbsoluteFilePath("doc") + "/myFile.TXT";

    public static void main(String[] args) {
        try {
            // 从0开始读取18个字节的数据
            System.out.println(new String(readFromFile(FILEPATH, 0, 18)));
            // 在文件游标的31位置处写入数据
            writeToFile(FILEPATH, "I love my country and my people", 31);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] readFromFile(String filePath, int position, int size) throws IOException {
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        file.seek(position);
        byte[] bytes = new byte[size];
        file.read(bytes);
        file.close();
        return bytes;
    }

    private static void writeToFile(String filePath, String data, int position) throws IOException {
        RandomAccessFile file = new RandomAccessFile(filePath, "rw");
        file.seek(position);
        file.write(data.getBytes());
        file.close();
    }
}
