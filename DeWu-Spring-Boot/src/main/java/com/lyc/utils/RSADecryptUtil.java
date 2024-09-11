package com.lyc.utils;

import com.lyc.config.RSAProperties;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Optional;

@Component
public class RSADecryptUtil {

    private static final String ALGORITHM = "RSA";
    private static final String CIPHER_INSTANCE = "RSA/ECB/PKCS1Padding";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private PrivateKey privateKey;

    @Autowired
    public RSADecryptUtil(RSAProperties rsaProperties) {
        this.privateKey = getPrivateKeyFromString(rsaProperties.getPrivateKey()).orElse(null);
        if (this.privateKey == null) {
            // 使用日志框架记录初始化失败
            System.err.println("Failed to initialize RSA private key");
        }
    }

    public Optional<String> decrypt(String encryptedMessage) {
        if (privateKey == null) {
            return Optional.empty();
        }

        try {
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedMessage);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            return Optional.of(new String(decryptedBytes));
        } catch (Exception e) {
            // 使用日志框架记录解密失败
            System.err.println("Decryption failed: " + e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<PrivateKey> getPrivateKeyFromString(String privateKeyPEM) {
        try {
            privateKeyPEM = privateKeyPEM.replace("-----BEGIN PRIVATE KEY-----", "");
            privateKeyPEM = privateKeyPEM.replace("-----END PRIVATE KEY-----", "");
            privateKeyPEM = privateKeyPEM.replaceAll("\\s+", "");

            byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            return Optional.of(keyFactory.generatePrivate(keySpec));
        } catch (Exception e) {
            // 使用日志框架记录私钥生成失败
            System.err.println("Failed to generate private key: " + e.getMessage());
            return Optional.empty();
        }
    }
}
