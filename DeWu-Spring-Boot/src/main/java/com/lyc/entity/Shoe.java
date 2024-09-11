package com.lyc.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author lyc
 * @since 2024-07-24
 */
@Getter
@Setter
public class Shoe implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId("sku_id")
    private Integer skuId;

    private String articleNumber;

    private String otherNumbers;

    private String brandName;

    private String size;

    private String spuLogo;

    private String barcode;

    private String extraCode;

    private String customCode;
}
