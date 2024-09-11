package com.lyc.service;

import com.lyc.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lyc
 * @since 2024-07-24
 */
public interface UserService extends IService<User> {
    void register(String username, String encryptPassword, String email);


    void updataAppKeyAndSecret(String appKey, String secret);
}
