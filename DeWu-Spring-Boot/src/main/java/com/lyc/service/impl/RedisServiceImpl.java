package com.lyc.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyc.entity.Shoe;
import com.lyc.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ObjectMapper objectMapper; // 用于序列化和反序列化

    // 存储数据
    @Override
    public void saveShoeInfo(Shoe shoe, String barcode) {
        String key = barcode;
        String value = null; // 将对象序列化为 JSON 字符串
        try {
            value = objectMapper.writeValueAsString(shoe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        stringRedisTemplate.opsForValue().set(key, value, 1, TimeUnit.HOURS);
    }

    // 检索数据
    @Override
    public Shoe getShoeInfo(String barcode) {
        String value = stringRedisTemplate.opsForValue().get(barcode);
        if (value == null) {
            return null; // 或者抛出一个自定义异常，表示找不到对应的条形码
        }
        try {
            return objectMapper.readValue(value, Shoe.class); // 将 JSON 字符串反序列化为对象
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
