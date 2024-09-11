package com.lyc.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private Algorithm algorithm;

    @PostConstruct
    public void init() {
        // 初始化算法实例
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    // 接收业务数据,生成token并返回
    public String genToken(Map<String, Object> claims) {
        return JWT.create()
                .withClaim("claims", claims)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime))
                .sign(algorithm);
    }

    // 接收token,验证token,并返回业务数据
    public Map<String, Object> parseToken(String token) {
        return JWT.require(algorithm)
                .build()
                .verify(token) // 验证token
                .getClaim("claims") // 获取负荷中名为"claims"的声明
                .asMap(); // 转换为Map<String, Object>类型
    }
}
