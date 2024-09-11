/*
package com.lyc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyc.entity.Inventory;
import com.lyc.entity.InventoryResponse;
import com.lyc.entity.Order;
import com.lyc.entity.Shoe;
import com.lyc.mapper.InventoryMapper;
import com.lyc.service.DewuApiService;
import com.lyc.service.ImageRecognizeService;
import com.lyc.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BatchArticleNumberTest {

    @Autowired
    private DewuApiService dewuApiService;

    @Autowired
    private ImageRecognizeService imageRecognizeService;

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private InventoryService inventoryService;

    @Test
    public void testFetchArticleNumber() {
        Shoe shoe = dewuApiService.batchArticleNumber("F12W232127FBK", "35.5", null);

        System.out.println(shoe.getSkuId());

    }
    @Test
    public void getOrderTest() {
        String startTime = "2024-07-01";
        String endTime = "2024-08-14";
        List<Order> orderList = dewuApiService.getRealTime(startTime,endTime);
        for (Order order : orderList) {
            System.out.println(order);
        }
        System.out.println(orderList.size());
    }

    @Test
    public void ServiceTest() {
        dewuApiService.batchArticleNumber("F12W232127FBK", "40", null);
    }

    @Test
    public void InvetoryTest() {
        List<Integer> list = Arrays.asList(
667271414,
                65412561,
                602322913,
                464802608,
                464802608,
                655972306,
                655972306,
                655972306,
                252624560,
                252624560,
                618143792,
                618143792,
                618143792,
                618143792,
                618143792,
                612545291,
                600028897,
                600028898,
                600028896,
                600028899,

                442944612,
                709746271);
        System.out.println(inventoryService.outBoundBySkuId(list));
    }




    // 你可以添加更多的测试方法来测试其他功能
}
*/
