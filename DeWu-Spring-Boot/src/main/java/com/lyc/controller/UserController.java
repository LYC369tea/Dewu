package com.lyc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyc.config.DewuConfig;
import com.lyc.entity.Result;
import com.lyc.entity.User;
import com.lyc.service.UserService;
import com.lyc.utils.BCryptUtil;
import com.lyc.utils.JwtUtil;
import com.lyc.utils.RSADecryptUtil;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lyc
 * @since 2024-07-24
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil; // 注入JwtUtil实例

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RSADecryptUtil rsaDecryptUtil;

    @Autowired
    private DewuConfig dewuConfig;

    // 注册功能
    @PostMapping("/0")
    public Result register(
            @NotEmpty @Pattern(regexp = "^\\S{4,16}$") String username,
            @NotEmpty @Pattern(regexp = "^\\S{8,32}$") String password,
            @NotEmpty @Email String email
    ) {
        // 检查用户名是否已存在
        User existingUserByUsername = userService.getOne(new QueryWrapper<User>().eq("username", username));
        if (existingUserByUsername != null) {
            return Result.error("用户名已使用，请换一个用户名");
        }

        // 检查邮箱是否已存在
        User existingUserByEmail = userService.getOne(new QueryWrapper<User>().eq("email", email));
        if (existingUserByEmail != null) {
            return Result.error("邮箱已被使用");
        }

        // 加密密码
        String encryptPassword  = BCryptUtil.encrypt(password);

        // 注册用户，使用加密后的密码
        userService.register(username, encryptPassword, email);

        return Result.success();
    }

    // 登录功能
    @PostMapping("/1")
    public Result login(String usernameOrEmail, String password) {
        User loginUser = userService.getOne(
                new QueryWrapper<User>()
                        .eq("username", usernameOrEmail)
                        .or()
                        .eq("email", usernameOrEmail)
        );
        if (loginUser == null) {
            return Result.error("该用户不存在");
        }
       /* String decryptedPassword = String.valueOf(rsaDecryptUtil.decrypt(password));*/
        if (BCryptUtil.checkPassword(password, loginUser.getPassword()))
        {

            // 密码匹配，生成 JWT 令牌
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", loginUser.getUserId());
            claims.put("username", loginUser.getUsername());
            String token = jwtUtil.genToken(claims);

            // 存储 Token 到 Redis
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            String baseKey = "Token:UserID:" + loginUser.getUserId(); // 基础键

            // 配置DW API 的appKey和secret
            String appKey = (loginUser != null) ? loginUser.getAppKey() : null;
            String secret = (loginUser != null) ? loginUser.getSecret() : null;
            dewuConfig.setConfig(appKey, secret);



            // 检查是否已经存在token
            String existingToken1 = operations.get(baseKey + ":1");
            String existingToken2 = operations.get(baseKey + ":2");

            if (existingToken1 == null) {
                // 如果第一个slot为空，存储在第一个slot
                operations.set(baseKey + ":1", token, 48, TimeUnit.HOURS);
            } else if (existingToken2 == null) {
                // 如果第二个slot为空，存储在第二个slot
                operations.set(baseKey + ":2", token, 48, TimeUnit.HOURS);
            } else {
                // 如果两个slot都已被占用，替换最旧的那个
                long ttl1 = stringRedisTemplate.getExpire(baseKey + ":1", TimeUnit.SECONDS);
                long ttl2 = stringRedisTemplate.getExpire(baseKey + ":2", TimeUnit.SECONDS);

                if (ttl1 <= ttl2) {
                    operations.set(baseKey + ":1", token, 48, TimeUnit.HOURS);
                } else {
                    operations.set(baseKey + ":2", token, 48, TimeUnit.HOURS);
                }
            }


            return Result.success(token);

        }

        // 密码不匹配
        return Result.error("密码错误");
    }

    // 退出登录功能
    @PostMapping("/2")
    public Result logout(Integer userId) {
        // 从 Redis 中删除 Token
        String baseKey = "Token:UserID:" + userId;
        stringRedisTemplate.delete(baseKey + ":1");
        stringRedisTemplate.delete(baseKey + ":2");
        return Result.success();
    }

    // 获取用户信息功能
    @PostMapping("/3")
    public Result getUserInfo(Integer userId) {
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error("该用户不存在");
        }
        return Result.success(user);
    }

    // 修改用户的AppKey和Secret功能
    @PostMapping("/4")
    public Result modifyAppKeyAndSecret(String appKey, String secret) {
        userService.updataAppKeyAndSecret(appKey, secret);
        return Result.success();
    }

    @GetMapping("/time")
    public Result getTime() {
        return Result.success(LocalDateTime.now());
    }

}
