package cn.bravedawn.encrypt.symmetric;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

/**
 * @Author : depers
 * @Date : Created in 2025-08-27 13:33
 */
public class AESExample5 {


    /**
     * 从弱密码中获取AES密钥
     * @param args
     */
    public static void main(String[] args) {
        String password = "mySuperSecretPassword"; // 用户提供的弱密码

        // 1. 生成一个随机的盐（Salt）（通常16字节或更长）
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        // 2. 设置PBKDF2参数
        int iterationCount = 65536; // 迭代次数，增加计算成本以抵御暴力破解
        int keyLength = 256;        // 指定要生成的密钥长度（位）

        // 3. 使用 PBKDF2WithHmacSHA256 算法派生密钥
        try {
            // 创建 PBEKeySpec（Password-Based Encryption Key Specification）
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterationCount, keyLength);

            // 获取密钥工厂
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            // 生成密钥（这是一个 PBE 密钥，还不是 AES 密钥）
            byte[] derivedKeyBytes = factory.generateSecret(spec).getEncoded();

            // 4. 将派生出的字节数组包装成 AES 密钥
            SecretKey aesKey = new SecretKeySpec(derivedKeyBytes, "AES");

            System.out.println("AES Key generated: " + bytesToHex(derivedKeyBytes));
            System.out.println("Salt: " + bytesToHex(salt));
            // 注意：你必须将 salt 和 iterationCount 存储起来，解密时需要它们重新生成相同的密钥。

        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
    }

    // 辅助方法：将字节数组转换为十六进制字符串用于显示
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
