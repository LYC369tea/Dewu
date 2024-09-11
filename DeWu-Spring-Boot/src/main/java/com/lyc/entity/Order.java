package com.lyc.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("orders")
public class Order {
    @TableId(value = "order_no")
    private String orderNo;
    private String orderType;
    private String articleNumber;
    private String props;
    private BigDecimal skuPrice;
    private BigDecimal stmtFee;
    private LocalDateTime realStmtTime;
    private Integer skuId;
    private Integer userId;
}
