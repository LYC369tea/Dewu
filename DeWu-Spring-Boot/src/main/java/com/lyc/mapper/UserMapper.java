package com.lyc.mapper;

import com.lyc.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyc
 * @since 2024-07-24
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {


    void register(String username, String password, String email);


}
