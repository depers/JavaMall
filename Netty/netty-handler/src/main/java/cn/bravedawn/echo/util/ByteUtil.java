package cn.bravedawn.echo.util;

import java.io.UnsupportedEncodingException;

/**
 * @Author : depers
 * @Date : Created in 2025-10-10 17:23
 */
public class ByteUtil {


    /**
     * 字节转字符串
     * @param bytes
     * @return
     */
    public static String bytes2GBKStr(byte[] bytes) {
        try {
            return new String(bytes, "GBK");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }
}
