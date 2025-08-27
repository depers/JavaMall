package cn.bravedawn.encrypt.asymmetric;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * @Author : depers
 * @Date : Created in 2025-08-27 10:55
 */
public class RSAExample2 {


    /**
     * 加载私钥
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    private void loadPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        // 1. 读取你的私钥字符串（通常从文件中读取，这里省略了文件操作）
        // 注意：需要去除 "-----BEGIN/END PRIVATE KEY-----" 头和尾，并连接成一行
        String privateKeyPEM = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC..."; // 你的Base64编码密钥

        // 2. 移除头尾标记和换行符，进行Base64解码，得到原始的字节数组
        byte[] decoded = Base64.getDecoder().decode(privateKeyPEM);

        // 3. 使用 PKCS8EncodedKeySpec，因为这是标准的PKCS#8格式
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decoded);

        // 4. 获取 RSA 的 KeyFactory
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        // 5. 从 KeySpec 生成私钥对象
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        System.out.println("私钥加载成功: " + privateKey.getAlgorithm());
    }

    /**
     * 加载公钥
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    private void loadPublicKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        String publicKeyPEM = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKjDg..."; // 你的Base64编码公钥

        byte[] decoded = Base64.getDecoder().decode(publicKeyPEM);
        // 注意：这里使用 X509EncodedKeySpec
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decoded);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        System.out.println("公钥加载成功: " + publicKey.getAlgorithm());
    }
}
