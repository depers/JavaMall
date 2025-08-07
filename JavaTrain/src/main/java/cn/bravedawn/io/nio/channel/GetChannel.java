package cn.bravedawn.io.nio.channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @author : depers
 * @description :
 * @program : 2.JavaTrain
 * @date : Created in 2023/12/31 14:37
 */
public class GetChannel {

    /**
     * 从FileInputStream、FileOutputStream、RandomAccessFile中获取FileChannel
     */


    private static final int BSIZE = 1024;


    public static void main(String[] args) throws IOException {

        // 写一个文件
        FileChannel fc = new FileOutputStream("data.txt").getChannel();
        fc.write(ByteBuffer.wrap("some text".getBytes()));
        fc.close();

        // 向文件末尾追加内容
        fc = new RandomAccessFile("data.txt", "rw").getChannel();
        fc.position(fc.size()); // 移动到文件的末尾
        fc.write(ByteBuffer.wrap("some more".getBytes()));
        fc.close();

        // 读取文件，只读情况下需要显示的调用allocate()方法来分配ByteBuffer
        fc = new FileInputStream("data.txt").getChannel();
        ByteBuffer buff = ByteBuffer.allocate(BSIZE);
        fc.read(buff);
        buff.flip();
        while (buff.hasRemaining()) {
            System.out.println((char) buff.get());
        }


    }
}
