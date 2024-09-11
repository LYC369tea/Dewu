package com.lyc.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyc.entity.Order;
import com.lyc.entity.OrderExtendPage;
import com.lyc.mapper.OrderMapper;
import com.lyc.service.OrderService;
import com.lyc.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    private OrderMapper orderMapper; // 假设你有一个 OrderMapper 进行数据库操作

    @Override
    public IPage<Order> getOrder(Integer page, Integer pageSize, String startTime, String endTime, String articleNumber, String size) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        // 获取数据列表
        List<Order> orderList = orderMapper.getOrderList(startTime, endTime, articleNumber, size, userId);
        // 计算总收入
        BigDecimal totalIncome = orderList.stream().map(Order::getStmtFee).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 创建分页对象并设置记录
        OrderExtendPage<Order> pageRequest = new OrderExtendPage<>(page, pageSize);
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, orderList.size());
        pageRequest.setRecords(orderList.subList(start, end));
        pageRequest.setTotal(orderList.size());
        pageRequest.setTotalIncome(totalIncome);

        // 返回分页对象
        return pageRequest;
    }
}
