package com.lyc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyc.entity.Inventory;
import com.lyc.entity.InventoryResponse;
import com.lyc.entity.Shoe;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface InventoryMapper extends BaseMapper<Inventory> {

    List<InventoryResponse> getInventorySummary(Integer userId , Integer status);

    List<Inventory> selectByArticleNumber(Integer userId, String articleNumber);

    List<Inventory> getInventoryDetail(Integer userId, String articleNumber,Integer skuId);


    void outbound(Integer skuId, Integer count, Integer userId, LocalDateTime now);

}
