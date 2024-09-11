package com.lyc.entity;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderExtendPage<Order> extends Page<Order> {
    private BigDecimal totalIncome;


    public OrderExtendPage(Integer page, Integer pageSize) {
        super(page, pageSize);
    }
}
