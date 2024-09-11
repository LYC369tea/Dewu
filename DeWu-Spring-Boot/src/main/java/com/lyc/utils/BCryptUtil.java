package com.lyc.utils;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptUtil {

    // 加密密码
    public static String encrypt(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // 验证密码
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
