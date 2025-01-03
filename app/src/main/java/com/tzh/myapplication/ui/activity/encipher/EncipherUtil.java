package com.tzh.myapplication.ui.activity.encipher;

import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 加密工具类
 */
public class EncipherUtil {

    public static void encode(String message){
        try {
            // 原文:
            System.out.println("Message(原始信息): " + message);

            // 256位密钥 = 32 bytes Key:
            byte[] key = "f8bf679d87444bad9c9c93287hb8894a".getBytes();

            // 加密:
            byte[] data = message.getBytes();
            byte[] encrypted = encrypt(key, data);
            System.out.println("Encrypted(加密内容): " +
                    Base64.getEncoder().encodeToString(encrypted));

            // 解密:
            byte[] decrypted = decrypt(key, encrypted);
            System.out.println("Decrypted(解密内容): " + new String(decrypted));
        }catch (GeneralSecurityException e){

        }
    }

    // 加密:
    public static byte[] encrypt(byte[] key, byte[] input) throws GeneralSecurityException {
        // 设置算法/工作模式CBC/填充
        Cipher cipher=Cipher.getInstance("AES/CBC/PKCS7Padding");

        // 恢复秘钥对象
        SecretKey keySpec=new SecretKeySpec(key, "AES");

        // CBC模式需要生成一个16 bytes的initialization vector:
        SecureRandom sr=SecureRandom.getInstanceStrong();
        byte[] iv=sr.generateSeed(16);
        System.out.println("iv字节数组(内容)："+ Arrays.toString(iv));
        System.out.println("iv字节数组(长度)："+iv.length);

        // 初始化秘钥:操作模式、秘钥、IV参数
        IvParameterSpec ivps = new IvParameterSpec(iv);

        cipher.init(Cipher.ENCRYPT_MODE, keySpec,ivps);

        // 加密
        byte[] data=cipher.doFinal(input);

        // IV不需要保密，把IV和密文一起返回:
        return join(iv,data);
    }

    // 解密:
    public static byte[] decrypt(byte[] key, byte[] input) throws GeneralSecurityException {
        // 把input分割成IV和密文:
        byte[] iv=new byte[16];
        byte[] data=new byte[input.length-16];

        System.arraycopy(input, 0, iv, 0, 16);    //IV
        System.arraycopy(input, 16, data, 0, data.length);   //密文
        System.out.println(Arrays.toString(iv));

        // 解密:
        Cipher cipher=Cipher.getInstance("AES/CBC/PKCS7Padding"); //密码对象
        SecretKeySpec keySpec=new SecretKeySpec(key, "AES");  //恢复密码
        IvParameterSpec ivps=new IvParameterSpec(iv);    //恢复IV

        // 初始化秘钥:操作模式、秘钥、IV参数
        cipher.init(Cipher.DECRYPT_MODE, keySpec,ivps);

        // 解密操作
        return cipher.doFinal(data);
    }

    // 合并数组
    public static byte[] join(byte[] bs1, byte[] bs2) {
        byte[] r=new byte[bs1.length+bs2.length];
        System.arraycopy(bs1, 0, r, 0, bs1.length);
        System.arraycopy(bs2, 0, r, bs1.length, bs2.length);
        return r;
    }


    private static final String SECRET_KEY = "f8bf679d87444bad9c9c93287hb8894a"; // 16 bytes key
    private static final String IV = "f8bf679d87444bad"; // 16 bytes IV
    private static final String ALGORITHM = "AES/CBC/PKCS7Padding";

    public static String encrypt(String data) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encryptedData) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] original = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(original, StandardCharsets.UTF_8);
    }

    public static void main() {
        try {
            String originalData = "{\"a\":\"1\", \"b\":\"2\"}";
            String encryptedData = encrypt(originalData);
            System.out.println("Encrypted: " + encryptedData);

            String decryptedData = decrypt(encryptedData);
            System.out.println("Decrypted: " + decryptedData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
