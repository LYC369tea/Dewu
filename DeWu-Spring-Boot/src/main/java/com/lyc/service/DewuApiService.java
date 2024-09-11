package com.lyc.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.lyc.entity.Order;
import com.lyc.entity.Shoe;

import java.time.LocalDateTime;
import java.util.List;

public interface DewuApiService extends IService<Shoe> {
    String ApiTest();
    Shoe batchArticleNumber(String huohao, String size, String barcode);

    String addCustomCode(String articleNumber, String size, String customCode);


    String saveAllSkusByArticleNumber(String articleNumber);


    Shoe getSkuByBarcode(String barcode);

    String getLowestPriceByArticleNumber(Integer skuId);

    List<Order> getRealTime(String startTime, String endTime);
}
