package com.lyc.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Inventory {

    @TableId(type = IdType.AUTO)
    private Integer inventoryId;

    private Integer skuId;

    private String size;

    private String batchNumber;

    private BigDecimal cost;

    private LocalDateTime inboundTime;

    private LocalDateTime outboundTime;

    private Integer userId;

    private String notes;

    private Integer status;


}