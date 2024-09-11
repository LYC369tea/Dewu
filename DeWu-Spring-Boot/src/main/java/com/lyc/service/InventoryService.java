package com.lyc.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyc.entity.Inventory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lyc.entity.InventoryResponse;
import com.lyc.entity.Result;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lyc
 * @since 2024-07-24
 */
public interface InventoryService extends IService<Inventory> {


    String addInventory(List<Integer> skuIdList);
    
    List<InventoryResponse> getInventory();

    IPage<Inventory> getInventoryDetail(Integer page, Integer pageSize, String articleNumber);

    Integer outBoundBySkuId(List<Integer> skuIdList);

}
