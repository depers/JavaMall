package cn.bravedawn.basic.base64;

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

    public static void main(String[] args) throws UnsupportedEncodingException {
        basicRun();
        urlSafeRun();
        mineRun();
    }

    public static void basicRun(){
        String text = "哈哈haha123!/+";

        Base64.Encoder encoder = Base64.getEncoder();
        String encodeStrA = encoder.encodeToString(text.getBytes(StandardCharsets.UTF_8));
        log.info("basic Base64 encode: {}.", encodeStrA);

        Base64.Decoder decoder = Base64.getDecoder();
        byte[] decodeByteArr = decoder.decode(encodeStrA);
        String decodeStrA = new String(decodeByteArr, StandardCharsets.UTF_8);
        log.info("basic Base64 decode: {}.", decodeStrA);
    }

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