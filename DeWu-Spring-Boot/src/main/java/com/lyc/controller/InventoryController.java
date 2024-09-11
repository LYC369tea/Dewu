package com.lyc.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lyc.entity.Inventory;
import com.lyc.entity.InventoryResponse;
import com.lyc.entity.Result;
import com.lyc.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add")
    public Result<String> addShoe(@RequestBody List<Integer> skuIdList) {
        String result = inventoryService.addInventory(skuIdList);
        if (result == null) {
            return Result.success();
        }
        return Result.error(result);
    }

    @PostMapping("/out")
    public Result<String>  outBound(@RequestBody List<Integer> skuIdList){
        System.out.println("outBound");
        inventoryService.outBoundBySkuId(skuIdList);
        return Result.success();
    }

    @GetMapping ("getDetail")
    public Result<IPage<Inventory>> getInventoryDetail(@RequestParam Integer page, @RequestParam Integer pageSize, @RequestParam String articleNumber) {

        IPage<Inventory> inventoryPage = inventoryService.getInventoryDetail(page, pageSize, articleNumber);
        return Result.success(inventoryPage);
    }


    @GetMapping("/get")
    public Result<List<InventoryResponse>> getInventory() {
        return Result.success(inventoryService.getInventory());
    }

    @PostMapping("/update")
    public Result<String> updateInventory(@RequestBody List<Inventory> inventoryList) {
        inventoryService.updateBatchById(inventoryList);
        return Result.success();
    }

    @PostMapping("/delete")
    public Result<String> deleteInventory(@RequestBody List<Integer> inventoryIdList) {
        inventoryService.removeBatchByIds(inventoryIdList);
        return Result.success();
    }

}
