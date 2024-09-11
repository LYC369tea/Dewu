package com.lyc.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lyc.config.DewuConfig;
import com.lyc.entity.Order;
import com.lyc.entity.Result;
import com.lyc.entity.Shoe;
import com.lyc.entity.User;
import com.lyc.service.DewuApiService;
import com.lyc.service.OrderService;
import com.lyc.service.UserService;
import com.lyc.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private DewuApiService dewuApiService;

    @Autowired
    private UserService userService;

    @Autowired
    private DewuConfig dewuConfig;

    // 获取实时对账单
    @GetMapping("/getRealTime")
    public Result<String> getRealTime(@RequestParam String startTime, @RequestParam String endTime) {
        // 获取 userId
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        User user = userService.getById(userId);
        if (user.getAppKey() == null && user.getSecret() == null){
            return Result.error("请先完善appKey和secret");
        }
        if (!dewuConfig.getConfig().getAppKey().equals(user.getAppKey()) ||
                !dewuConfig.getConfig().getSecret().equals(user.getSecret())) {
            dewuConfig.setConfig(user.getAppKey(), user.getSecret());
            String result = dewuApiService.ApiTest();
            if (result != null) {
                return Result.error(result+"请重新检查appKey和secret或者IP白名单");
            }

        }

        List<Order> orderList = dewuApiService.getRealTime(startTime,endTime);
        orderService.saveOrUpdateBatch(orderList);

        return Result.success(orderList.size() + "条数据保存成功");
    }

    // 获取订单
    @GetMapping("/getOrder")
    public Result<IPage<Order>> getOrder(
            @RequestParam(required = false) String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String articleNumber,
            @RequestParam(required = false) String size,
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize) {


        // 调用服务层方法
        IPage<Order> orderPage = orderService.getOrder(page, pageSize, startTime, endTime, articleNumber, size);

        // 返回结果
        return Result.success(orderPage);
    }
}

