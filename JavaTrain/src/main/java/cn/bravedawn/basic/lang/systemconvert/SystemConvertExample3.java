package cn.bravedawn.basic.lang.systemconvert;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * @Description : 将int与十六进制字符串的互转
 * @Author : depers
 * @Project : JavaTrain
 * @Date : Created in 2025-07-18 15:11
 */
public class SystemConvertExample3 {


    public static String intToHexWithByteBuffer(int value, boolean bigEndian) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(value);

        StringBuilder sb = new StringBuilder();
        for (byte b : buffer.array()) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }


    public static int hexToIntWithByteBuffer(String hexStr, boolean bigEndian) throws DecoderException {
        byte[] bytes = Hex.decodeHex(hexStr.toCharArray());
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(bigEndian ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
        return buffer.getInt();
    }


    public static void main(String[] args) throws DecoderException {
        String hexStr = intToHexWithByteBuffer(551, true);
        System.out.println(hexStr);

        int val = hexToIntWithByteBuffer(hexStr, true);
        System.out.println(val);
    }
}
