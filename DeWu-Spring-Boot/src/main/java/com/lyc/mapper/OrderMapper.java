package com.lyc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyc.entity.Inventory;
import com.lyc.entity.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

    List<Order> getOrderList(String startTime, String endTime, String articleNumber, String size, Integer userId);
}
