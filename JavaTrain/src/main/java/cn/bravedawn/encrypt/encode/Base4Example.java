package cn.bravedawn.encrypt.encode;

import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

/**
 * @author : depers
 * @program : JavaTrain
 * @description: Base64案例
 * @date : Created in 2021/2/3 15:18
 */
@Slf4j
public class Base4Example {


    /**
     * 二进制数据的传输问题，Base64编码是对二进制数据进行编码，表示成文本格式。
     */

    public static void main(String[] args) throws UnsupportedEncodingException {
        basicRun();
//        urlSafeRun();
//        mineRun();
    }

    public static void basicRun(){
        String text = "哈哈haha123!/+";

        Base64.Encoder encoder = Base64.getEncoder();
        String encodeStrA = encoder.encodeToString(text.getBytes(StandardCharsets.UTF_8));
        log.info("textByte: {}, basic Base64 encode: {}.", text.getBytes(StandardCharsets.UTF_8), encodeStrA);

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodeByteArr = decoder.decode(encodeStrA);
        String decodeStrA = new String(decodeByteArr, StandardCharsets.UTF_8);
        log.info("textByte: {}, basic Base64 decode: {}.", decodeByteArr, decodeStrA);
    }

    // urlSafe中将base64索引表中的62(+)和63(/)替换成了62(-)和63(_)。具体可以参考RFC 4648
    public static void urlSafeRun(){
        String text = "哈哈haha123!/+";

        Base64.Encoder encoder = Base64.getUrlEncoder();
        String encodeStrA = encoder.encodeToString(text.getBytes(StandardCharsets.UTF_8));
        log.info("url safe Base64 encode: {}.", encodeStrA);

        Base64.Decoder decoder = Base64.getUrlDecoder();
        byte[] decodeByteArr = decoder.decode(encodeStrA);
        String decodeStrA = new String(decodeByteArr, StandardCharsets.UTF_8);
        log.info("url safe Base64 decode: {}.", decodeStrA);
    }


    /**
     * MIME的全称是"Multipurpose Internet Mail Extensions"，中译为"多用途互联网邮件扩展"，它的一个重要特点，就是规定电子邮件只能使用ASCII字符。
     * 使用Base64.getMimeEncoder().encodeToString()生成的字符编码只有ASCII字符。
     */
    public static void mineRun() throws UnsupportedEncodingException {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10; ++i) {
            stringBuilder.append(UUID.randomUUID().toString());
        }

        byte[] mimeBytes = stringBuilder.toString().getBytes("utf-8");
        String mimeEncodedString = Base64.getMimeEncoder().encodeToString(mimeBytes);
        System.out.println("Base64 编码字符串 (MIME) :" + mimeEncodedString);
    }
}
