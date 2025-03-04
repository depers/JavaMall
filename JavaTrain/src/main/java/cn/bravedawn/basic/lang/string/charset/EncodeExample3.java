package cn.bravedawn.basic.lang.string.charset;

import org.bouncycastle.util.encoders.Hex;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * @author : depers
 * @program : JavaTrain
 * @date : Created in 2025/3/4 20:16
 */
public class EncodeExample3 {


    /**
     * 䶮，𠅤
     * 生僻词多次编解码
     * gbk是中文编码格式，采用固定两个字节存储一个汉字。
     * gb18030也是中文编码格式，采用1、2、4个字节存储一个汉字。
     * utf-8 可变长字符编码格式，UTF-8通常使用3个字节来存储一个汉字。
     * utf8mb4支持完整的utf-8，但是还支持存储罕见的汉字和表情。
     */


    public static void main(String[] args) throws UnsupportedEncodingException {
        String str = "\uD840\uDD64";

        // 使用utf-8将字符串编码和解码
        byte[] byteUtf8 = str.getBytes("UTF-8");
        System.out.println(Arrays.toString(byteUtf8));
        System.out.println(new String(byteUtf8, "UTF-8"));

        // 使用gbk将字符串编码和解码
        byte[] byteGBK = str.getBytes("GBK");
        System.out.println(Arrays.toString(byteGBK));
        System.out.println(new String(byteGBK, "GBK"));

        // 使用gbk将字符串编码和解码后，获取到的字符串已经是个?了，即使获取gb18030的编码字节数组，接着再解码也不能将其还原了
        byte[] bytes = new String(byteGBK, "GBK").getBytes("gb18030");
        System.out.println(new String(bytes, "gb18030"));

        // 下面这个是同理
        byte[] bytes2 = new String(byteGBK, "GBK").getBytes("utf-8");
        System.out.println(new String(bytes2, "utf-8"));


    }
}
