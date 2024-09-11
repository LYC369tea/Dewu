package com.lyc.interceptors;

import com.lyc.utils.JwtUtil;
import com.lyc.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;


@Component // 标记为组件，让Spring容器自动管理
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 从HTTP头部获取Token
        String token = request.getHeader("Authorization");

        try {
            if (token == null || token.isEmpty()) {
                // 如果Token为空，抛出异常
                throw new RuntimeException();
            }

            // 检查Token是否存储在Redis中，这样可以验证Token的有效性
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            Map<String, Object> claims = jwtUtil.parseToken(token);
            Integer userId = (Integer) claims.get("id");
            String baseKey = "Token:UserID:" + userId;

            // 检查两个可能的slot
            String redisToken1 = operations.get(baseKey + ":1");
            String redisToken2 = operations.get(baseKey + ":2");


            if (!token.equals(redisToken1) && !token.equals(redisToken2)) {
                // 如果Token与Redis中存储的两个Token都不匹配，抛出异常
                throw new RuntimeException("Invalid token");
            }


            // Token有效，允许请求继续执行
            // 把业务数据存储到ThreadLocal中
            ThreadLocalUtil.set(claims);
            return true;
        } catch (Exception e) {
            // 捕获异常并设置HTTP响应状态码为401
            response.setStatus(401);
            // 不放行
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        try {
            // 清除ThreadLocal中的数据
            ThreadLocalUtil.remove();
        } catch (Exception e) {
            // 可以进行一些异常处理
        }
    }
}