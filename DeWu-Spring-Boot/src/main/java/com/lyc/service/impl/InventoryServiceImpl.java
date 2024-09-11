package com.lyc.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lyc.entity.Inventory;
import com.lyc.entity.InventoryResponse;
import com.lyc.mapper.DataMapper;
import com.lyc.mapper.InventoryMapper;
import com.lyc.service.InventoryService;
import com.lyc.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private  DataMapper dataMapper;

    @Override
    @Transactional
    public String addInventory(List<Integer> skuIdList) {
        // 获取userId
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        // 获取当前时间作为批次号
        LocalDateTime now = LocalDateTime.now();
        LocalDate dataNow = LocalDate.now();
        String batchNumber = dataNow+userId.toString();
        // 插入库存表

        // 使用Stream API处理每个SKU ID
        List<Inventory> inventories = skuIdList.stream()
                .map(skuId -> {
                    Inventory inventory = new Inventory();
                    inventory.setSkuId(skuId);
                    inventory.setInboundTime(now);
                    inventory.setBatchNumber(batchNumber);
                    inventory.setUserId(userId);
                    inventory.setStatus(0);
                    return inventory;
                })
                .collect(Collectors.toList());

        inventoryMapper.insert(inventories);

        return null;
    }

    @Override
    @Transactional
    public Integer outBoundBySkuId(List<Integer> skuIdList) {
        // 获取userId
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        // 使用 Map 来统计每个 SKU 的出现次数
        Map<Integer, Integer> skuCountMap = new HashMap<>();

        for (Integer skuId : skuIdList) {
            skuCountMap.put(skuId, skuCountMap.getOrDefault(skuId, 0) + 1);
        }

        for (Map.Entry<Integer, Integer> entry : skuCountMap.entrySet()) {
            Integer skuId = entry.getKey();
            Integer count = entry.getValue();

            // 查询库存中该 SKU 的库存
            Long inventoryCount = inventoryMapper.selectCount(new QueryWrapper<Inventory>().eq("sku_id", skuId));

            if (count>inventoryCount || inventoryCount == 0){
                // 如果库存不足，抛出异常，这将导致事务回滚
                throw new RuntimeException("库存不足，SKU ID: " + skuId);
            }
            // 调用 outbound 方法进行更新
            inventoryMapper.outbound(skuId, count, userId, LocalDateTime.now());
        }

        return null;
    }


    @Override
    public List<InventoryResponse> getInventory() {
        // 获取userId
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        // status 0入库，1为出库
        return inventoryMapper.getInventorySummary(userId,0);
    }

    @Override
    public IPage<Inventory> getInventoryDetail(Integer page, Integer pageSize, String articleNumber) {
        // 获取 userId
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");

        // 获取数据列表
        List<Inventory> inventoryList = inventoryMapper.getInventoryDetail(userId, articleNumber,null);
        inventoryList.stream().map(inventory -> {
            inventory.setSize(dataMapper.selectById(inventory.getSkuId()).getSize());
            return inventory;
        });

        // 创建分页对象并设置记录
        Page<Inventory> pageRequest = new Page<>(page, pageSize);
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, inventoryList.size());
        pageRequest.setRecords(inventoryList.subList(start, end));
        pageRequest.setTotal(inventoryList.size());
        pageRequest.setPages((long) Math.ceil((double) inventoryList.size() / pageSize));
        System.out.println(pageRequest.getPages());
        // 返回分页对象
        return pageRequest;
    }




}
