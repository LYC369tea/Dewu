package com.lyc.entity;

import lombok.Data;


import java.math.BigDecimal;
import java.util.List;

@Data
public class InventoryResponse {
    private String articleNumber;
    private String otherNumbers;
    private String brandName;
    private String spuLogo;
    private BigDecimal avgCost;
    private List<SizeQuality> sizeQualityList;

}
