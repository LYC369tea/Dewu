package com.lyc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dewu.sdk.base.OpenResult;
import com.dewu.sdk.base.PageResponse;
import com.dewu.sdk.bidding.req.LowestPriceReq;
import com.dewu.sdk.bidding.res.SkuLowestPriceRes;
import com.dewu.sdk.bill.req.RealTimeBillReq;
import com.dewu.sdk.bill.res.RealTimeBillResV2;
import com.dewu.sdk.factory.Factory;
import com.dewu.sdk.product.req.ArticleNumberListReq;
import com.dewu.sdk.product.res.BrandCategorySpuRes;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lyc.entity.Order;
import com.lyc.entity.Shoe;
import com.lyc.mapper.DataMapper;
import com.lyc.service.DewuApiService;
import com.lyc.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class DewuApiServiceImpl extends ServiceImpl<DataMapper,Shoe> implements DewuApiService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private DataMapper dataMapper;


    @Override
    public String ApiTest(){
        ArticleNumberListReq request = new ArticleNumberListReq();
        request.setArticle_numbers(Arrays.asList("F36199"));
        OpenResult<List<BrandCategorySpuRes>> result = Factory.Product.client().batch_article_numbers(request);
        if (result.getCode() != 200) return result.getMsg();
        return result.getData().toString();
    }


    @Override
    public Shoe batchArticleNumber(String huohao, String size, String barcode) {
        ArticleNumberListReq request = new ArticleNumberListReq();
        request.setArticle_numbers(Arrays.asList(huohao));

        OpenResult<List<BrandCategorySpuRes>> result = Factory.Product.client().batch_article_numbers(request);
        System.out.println(result.getMsg());
        System.out.println(result.getData());


        if (result.getCode() != 200) return null;

        Shoe selectedShoe = null;
        // 正则表达式匹配中文字符
        Pattern chinesePattern = Pattern.compile("[\\u4e00-\\u9fa5]");
        for (BrandCategorySpuRes spu : result.getData()) {
            // 如果含有中文就下一个spu
            if (spu.getBrand_name() !="New Balance"){
                if (chinesePattern.matcher(spu.getArticle_number()).find()) {
                    continue;
                }
            }
            for (BrandCategorySpuRes.Sku sku : spu.getSkus()) {
                try {
                    Map<String, String> properties = objectMapper.readValue(sku.getProperties(), new TypeReference<Map<String, String>>() {
                    });
                    String skuSize = extractSize(properties.get("尺码"));
                    if (skuSize != null && skuSize.equals(size)) {
                        selectedShoe = createShoe(spu, sku, skuSize, barcode);
                    }
                } catch (Exception e) {
                    return null;
                }
            }
            if (selectedShoe != null) {
                dataMapper.insertOrUpdate(selectedShoe);
                return selectedShoe;
            }
        }
        return null;
    }

    private String extractSize(String skuSize) {
        if (skuSize == null || skuSize.isEmpty()) return null;
        if (skuSize.contains(" ")) {
            skuSize = skuSize.split(" ")[1];
        }
        // 替换特定符号并排除所有中文字符和特定符号
        skuSize = skuSize.replace("⅓", "")
                .replace("⅔", ".5")
                .replace("½", ".5")
                .replace(".0", "")
                .replace("/", "")
                .replace(" ", "")
                .replaceAll("[\u4e00-\u9fa5]", "");

        return skuSize;
    }


    private Shoe createShoe(BrandCategorySpuRes spu, BrandCategorySpuRes.Sku sku, String skuSize, String barcode) {
        Shoe shoe = new Shoe();
        shoe.setSkuId(Math.toIntExact(sku.getSku_id()));
        shoe.setArticleNumber(spu.getArticle_number());
        shoe.setOtherNumbers(spu.getOther_numbers());
        shoe.setBrandName(spu.getBrand_name());
        shoe.setSize(skuSize);
        shoe.setSpuLogo(spu.getSpu_logo());
        shoe.setBarcode(sku.getBarcode());
        shoe.setExtraCode(sku.getExtra_code());

        if (barcode != null && dataMapper.selectByBarcode(barcode) == null) {
            if (shoe.getCustomCode() != null && dataMapper.selectById(shoe.getSkuId()).getCustomCode() != null) {
                shoe.setCustomCode(shoe.getCustomCode() + ',' + barcode);
            } else {
                shoe.setCustomCode(barcode);
            }
        }
        return shoe;
    }


    @Override
    public String addCustomCode(String articleNumber, String size, String customCode) {
        Shoe updateShoe = null;
        // 找出数据库的sku
        updateShoe = dataMapper.selectByArticleNumber(articleNumber, size);
        if (updateShoe == null) {
            updateShoe = batchArticleNumber(articleNumber, size, customCode);
            if (updateShoe == null) {
                return null;
            }
            return updateShoe.getCustomCode();
        } else {
            if (dataMapper.selectByBarcode(customCode) == null) {
                // 添加自定义条形码
                if (updateShoe.getCustomCode() != null && !updateShoe.getCustomCode().isEmpty()) {
                    updateShoe.setCustomCode(updateShoe.getCustomCode() + "," + customCode);
                    dataMapper.updateById(updateShoe);
                } else {
                    updateShoe.setCustomCode(customCode);
                    dataMapper.updateById(updateShoe);
                }
                return updateShoe.getCustomCode();
            } else {
                return "该条形码已经被占用";
            }

        }
    }


    @Override
    public String saveAllSkusByArticleNumber(String huohao) {
        ArticleNumberListReq request = new ArticleNumberListReq();
        request.setArticle_numbers(Arrays.asList(huohao));

        OpenResult<List<BrandCategorySpuRes>> result = Factory.Product.client().batch_article_numbers(request);
        System.out.println(result.getData());
        if (result.getCode() != 200) return result.getMsg();
        // 正则表达式匹配中文字符
        Pattern chinesePattern = Pattern.compile("[\\u4e00-\\u9fa5]");
        for (BrandCategorySpuRes spu : result.getData()) {
            // 如果含有中文就下一个spu
            if (chinesePattern.matcher(spu.getArticle_number()).find()) {
                continue;
            }
            for (BrandCategorySpuRes.Sku sku : spu.getSkus()) {
                try {
                    Map<String, String> properties = objectMapper.readValue(sku.getProperties(), new TypeReference<Map<String, String>>() {
                    });
                    String skuSize = extractSize(properties.get("尺码"));
                    Shoe shoe = createShoe(spu, sku, skuSize, null);
                    dataMapper.insertOrUpdate(shoe);

                } catch (Exception e) {
                    return null;
                }
            }
        }
        return null;
    }


    @Override
    public Shoe getSkuByBarcode(String barcode) {
        return dataMapper.selectByBarcode(barcode);
    }


    @Override
    public String getLowestPriceByArticleNumber(Integer skuId) {
        String price = null;
        LowestPriceReq lowestSellPriceReq = new LowestPriceReq();
        lowestSellPriceReq.setSku_id(Long.valueOf(skuId));


        OpenResult<SkuLowestPriceRes> result = Factory.Bidding.normalClient().normal_lowest_price(lowestSellPriceReq);
        System.out.println(result.getData());
        if (result != null && result.getCode() == 200) {


            if (result.getData() != null && result.getData().getItems() != null) {
                for (SkuLowestPriceRes.LowestPrice lowestPrice : result.getData().getItems()) {
                    price = lowestPrice.getLowest_price().toString();
                }
                return String.valueOf(Integer.parseInt(price) / 100);
            }
            return null;
        }
        return null;
    }


    @Override
    public List<Order> getRealTime(String startTime, String endTime) {
        Map<String, Object> map = ThreadLocalUtil.get();
        Integer userId = (Integer) map.get("id");
        // 创建一个空的订单列表
        List<Order> orderList = new ArrayList<>();
        // 创建一个日期格式化器
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // 将输入的开始和结束时间转换为LocalDate对象
        LocalDate startDate = LocalDate.parse(startTime, formatter);
        LocalDate endDate = LocalDate.parse(endTime, formatter);
        LocalDate tempEndDate;

        // 当开始日期在结束日期之前时，继续循环
        while (startDate.isBefore(endDate)) {
            // 将开始日期向后推30天
            tempEndDate = startDate.plusDays(30);
            // 如果临时结束日期在结束日期之后，则将临时结束日期设为结束日期
            if (tempEndDate.isAfter(endDate)) {
                tempEndDate = endDate;
            }

            // 初始化页码
            int pageNum = 1;
            List<Order> currentPageOrderList;
            do {
                // 创建一个新的请求对象
                RealTimeBillReq req = new RealTimeBillReq();
                // 设置每页的订单数量为30
                req.setPage_size(30);
                // 设置页码
                req.setPage_no(pageNum);
                // 设置查询的开始和结束时间
                req.setSettle_start_time(startDate.format(formatter));
                req.setSettle_end_time(tempEndDate.format(formatter));

                // 发送请求并获取结果
                OpenResult<PageResponse<RealTimeBillResV2>> result = Factory.Bill.client().realtime_list_v2(req);
                System.out.println(result.getMsg());
                System.out.println("第"+pageNum+"页: "+result.getData());
                // 将结果转换为订单列表
                currentPageOrderList = result.getData().getList().stream()
                        .map(realTimeBillResV2 -> {
                            Order order = new Order();
                            order.setOrderNo(realTimeBillResV2.getOrder_no());
                            order.setOrderType(realTimeBillResV2.getOrder_type());
                            order.setArticleNumber(realTimeBillResV2.getArticle_number().split(",")[0]);
                            order.setProps(extractSize((realTimeBillResV2.getProps())));
                            order.setSkuPrice(new BigDecimal(realTimeBillResV2.getSku_price()));
                            order.setStmtFee(new BigDecimal(realTimeBillResV2.getStmt_fee()));
                            DateTimeFormatter dateTimeFormatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            LocalDateTime realStmtTime = LocalDateTime.parse(realTimeBillResV2.getReal_stmt_time(), dateTimeFormatter);
                            order.setRealStmtTime(realStmtTime);
                            order.setSkuId(handleSkuId(realTimeBillResV2.getArticle_number(), extractSize(realTimeBillResV2.getProps())));
                            order.setUserId(userId);
                            return order;
                        })
                        .collect(Collectors.toList());

                // 将当前页的订单添加到订单列表中
                orderList.addAll(currentPageOrderList);
                // 页码加1
                pageNum++;
            } while (currentPageOrderList.size() >= 30);  // 如果当前页的订单数量达到30，则继续获取下一页

            // 将开始日期设置为临时结束日期的下一天，以避免重复获取订单
            startDate = tempEndDate.plusDays(1);
        }

        // 返回订单列表
        return orderList;
    }


    private Integer handleSkuId(String articleNumber, String extractSize) {
        Integer skuId = null;
        if (articleNumber.contains(","))  articleNumber = articleNumber.split(",")[0];
        // 从 shoe 获取 skuId
        Shoe datashoe = dataMapper.selectByArticleNumber(articleNumber, extractSize);
        if (datashoe != null) {
            skuId = datashoe.getSkuId();
        }

        // 如果 skuId 为空，则从 batchArticleNumber 获取
        if (skuId == null) {
            Shoe dwshoe = batchArticleNumber(articleNumber, extractSize, null);
            if (dwshoe != null) {
                skuId = dwshoe.getSkuId();
            }
        }
        // 如果还是为空，则可能已经下架返回 null
        if (skuId == null) {
            return 10086;
        }
        return skuId;
    }



}