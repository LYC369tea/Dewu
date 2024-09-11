package com.lyc;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import java.util.Base64;
import java.io.FileOutputStream;
import java.io.IOException;

public class KeyPairGeneratorExample {
    public static void main(String[] args) throws Exception {
        // 创建 KeyPairGenerator 对象，指定算法为 RSA
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048); // 指定密钥长度

        // 生成密钥对
        KeyPair pair = keyGen.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();

        // 将私钥和公钥保存到文件
        saveKeyToFile("private_key.pem", privateKey.getEncoded());
        saveKeyToFile("public_key.pem", publicKey.getEncoded());

        System.out.println("密钥对生成成功");
    }

    private static void saveKeyToFile(String fileName, byte[] key) throws IOException {
        String keyContent = Base64.getEncoder().encodeToString(key);
        try (FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(("-----BEGIN " + (fileName.contains("private") ? "PRIVATE" : "PUBLIC") + " KEY-----\n").getBytes());
            fos.write(keyContent.getBytes());
            fos.write(("\n-----END " + (fileName.contains("private") ? "PRIVATE" : "PUBLIC") + " KEY-----\n").getBytes());
        }
    }
}
