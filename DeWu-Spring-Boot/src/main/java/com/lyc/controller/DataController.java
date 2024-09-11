package com.lyc.controller;


import com.lyc.entity.Result;
import com.lyc.entity.Shoe;
import com.lyc.service.DewuApiService;
import com.lyc.service.ImageRecognizeService;
import com.lyc.service.RedisService;
import com.lyc.utils.BarcodeReaderUtil;
import com.lyc.utils.ImageToBase64Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lyc
 * @since 2024-07-24
 */
@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    private ImageRecognizeService imageRecognizeService;

    @Autowired
    private DewuApiService dewuApiService;

    @Autowired
    private RedisService redisService;

    // DWU API接口测试
    @GetMapping("/test")
    public String test() {
        return dewuApiService.ApiTest();
    }

    // 上传图片识别功能
    @PostMapping("/upload")
    public Result<Shoe> imageRecognize(@RequestParam("file") MultipartFile file) {
        String smallFile = null;
        try {
            smallFile = ImageToBase64Converter.convertToBase64(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        /*String imageUrl = null;
        String path = "shoe_image/";
        // 转换jpg为png
        if (file.getOriginalFilename().endsWith(".jpg")) {
            try {
                file = ImageConverterUtil.convertJpgToPng(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        imageUrl = ossService.uploadImage(file, path);*/
        Shoe identifiedShoe = null;
        // 调用条形码识别
        String zxingBarcode = String.valueOf(BarcodeReaderUtil.decodeBarcodes(file)).replace("[","").replace("]","");
        if  ( zxingBarcode == null || zxingBarcode.equals("")) {
            identifiedShoe = imageRecognizeService.recognizeImage(smallFile,null);
        }else{
            // 调用图片识别服务
            identifiedShoe = imageRecognizeService.recognizeImage(smallFile,zxingBarcode);
        }

        System.out.println(identifiedShoe.getCustomCode());
        if (identifiedShoe == null) {
            return Result.error("图片识别失败");
        }


        // 将识别结果调用DeWu API接口并且将结果存入数据库
        Shoe saveShoe = dewuApiService.batchArticleNumber(identifiedShoe.getArticleNumber(), String.valueOf(identifiedShoe.getSize()), identifiedShoe.getCustomCode());
        if (saveShoe == null) {
            return Result.error("DeWu API接口调用失败 或者 存入数据库错误");
        }
        return Result.success(saveShoe);
    }

    // 添加自定义条形码功能
    @PostMapping("/addCustomCode")
    public Result<String> addCustomCode(
            @RequestParam("articleNumber") String articleNumber,
            @RequestParam("size") String size,
            @RequestParam("customCode") String customCode) {
        String result = dewuApiService.addCustomCode(articleNumber.toUpperCase(), size, customCode);
        if (result == null) {
            return Result.error("该商品已经下架或者不存在");
        }
        return Result.success(result);
    }

    // 根据货号添加spu下的所有skus功能
    @PostMapping("/addSku")
    public Result addSku(@RequestParam("articleNumber") String articleNumber) {

        String result = dewuApiService.saveAllSkusByArticleNumber(articleNumber.toUpperCase());
        if (result == null) {
            return Result.success();
        }
        return Result.error(result);
    }

    // 根据条形码查询sku
    @PostMapping("/getSkuByCustomCode")
    public Result<Shoe> getSkuByCustomCode(@RequestParam("barcode") String barcode) {
        Shoe redisShoe = redisService.getShoeInfo(barcode);
        if (redisShoe != null) {
            return Result.success(redisShoe);
        }
        Shoe returnShoe = dewuApiService.getSkuByBarcode(barcode);
        if (returnShoe == null) {
            return Result.error("数据库无该数据");
        }
        redisService.saveShoeInfo(returnShoe, barcode);
        return Result.success(returnShoe);
    }

    // 根据skuId查询当前出价
    @PostMapping("/getLowestPrice")
    public Result<String> getLowestPrice(@RequestParam("skuId") Integer skuId) {
        String result = dewuApiService.getLowestPriceByArticleNumber(skuId);
        if (result == null) {
            return Result.success("0");
        }
        return Result.success(result);
    }


}
