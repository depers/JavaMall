package cn.bravedawn.basic.lang.systemconvert;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;

/**
 * @Description : 将十六进制字符串转为字符串
 * @Author : depers
 * @Project : JavaTrain
 * @Date : Created in 2025-07-18 14:33
 */
public class SystemConvertExample2 {


    public static void main(String[] args) throws UnsupportedEncodingException, DecoderException {
        String str = "你好";
        String hexString = Hex.encodeHexString(str.getBytes("GBK"));
        System.out.println("将普通字符串转为十六进制字符串：" + hexString);
        byte[] bytes = Hex.decodeHex(hexString.toCharArray());
        System.out.println("将十六进制字符串转为普通字符串：" + new String(bytes, "GBK"));
    }
}
