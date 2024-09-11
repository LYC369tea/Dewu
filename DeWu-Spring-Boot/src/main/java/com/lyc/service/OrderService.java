package com.lyc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyc.entity.Inventory;
import com.lyc.entity.Order;

public interface OrderService extends IService<Order> {
    IPage<Order> getOrder(Integer page, Integer pageSize, String startTime, String endTime, String articleNumber, String size);
}
