package cn.bravedawn.encrypt.symmetric;

import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AESExample3 {

    /**
     * 基于密码的AES加解密方法，IV同密文一同返回，IV是一个随机数
     */

    public static final String SALT = "dc0cc7e01d964d82864a5435bdf7250a";
    public static final int KEY_SIZE = 128;

    /**
     * 加密
     * @param plainText 明文
     * @param key 密钥
     * @param iv 随机值
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String encryptPasswordBased(String plainText, SecretKey key, IvParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encryptData = ArrayUtils.addAll(iv.getIV(), cipher.doFinal(plainText.getBytes()));
        return Base64.getUrlEncoder().encodeToString(encryptData);
    }


    /**
     * 解密
     * @param cipherText 密文
     * @param key 密钥
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     * @throws InvalidKeyException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     */
    public static String decryptPasswordBased(String cipherText, SecretKey key)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
            InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        byte[] decodeData = Base64.getUrlDecoder().decode(cipherText);
        byte[] iv = ArrayUtils.subarray(decodeData, 0, 16);
        byte[] data = ArrayUtils.subarray(decodeData, 16, decodeData.length);
        IvParameterSpec ivps = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, key, ivps);
        return new String(cipher.doFinal(data));
    }


    /**
     * 获取随机值
     * @return
     */
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }


    /**
     * 从密码中获取密钥
     * @param password 密码
     * @param salt 盐值
     * @param keySize 密钥长度
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static SecretKey getKeyFromPassword(String password, String salt, int keySize)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, keySize);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
        return secret;
    }


    public static void main(String[] args) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        String plainText = "hello world";
        // 用户提供的弱密码
        String password = "QeMk6G1U/pRzZDux";
        // 生成偏移
        IvParameterSpec ivParameterSpec = generateIv();
        // 获取aes密钥
        SecretKey key = getKeyFromPassword(password, SALT, KEY_SIZE);
        // 加密
        String cipherText = encryptPasswordBased(plainText, key, ivParameterSpec);
        // 解密
        String decryptedCipherText = decryptPasswordBased(cipherText, key);

        System.out.println(plainText.equals(decryptedCipherText));
    }

}
