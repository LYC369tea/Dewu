package com.lyc.service.impl;

import com.lyc.entity.User;
import com.lyc.mapper.UserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyc.service.UserService;
import com.lyc.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyc
 * @since 2024-07-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void register(String username, String password, String email) {
        userMapper.register(username, password, email);
    }

    @Override
    public void updataAppKeyAndSecret(String appKey, String secret) {
        // 获取 userId
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
/*        userMapper.insertOrUpdate(appKey, secret);*/
    }


}
