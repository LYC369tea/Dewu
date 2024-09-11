package com.lyc.service.impl;

import com.lyc.entity.Shoe;
import com.lyc.service.ImageRecognizeService;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ImageContent;
import dev.langchain4j.data.message.TextContent;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.output.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ImageRecognizeServiceImpl implements ImageRecognizeService {

    @Autowired
    private ChatLanguageModel chatLanguageModel;

    @Override
    public Shoe recognizeImage(String smallFile, String zxingBarcode) {
        Shoe imageShoe = null;
        String message = "请帮我识别图片中的货号，尺码大小(EUR)和条码信息,按照以下格式发给我,货号:'',尺码大小(EUR):'',条码信息:''";
        UserMessage userMessage = UserMessage.from(
                TextContent.from(message),
                ImageContent.from(smallFile)
        );

        Response<AiMessage> response = null;
        int retryCount = 0;
        boolean success = false;

        while (retryCount < 3 && !success) {
            try {
                response = chatLanguageModel.generate(userMessage);

                success = true;
            } catch (Exception e) {
                retryCount++;
                if (retryCount >= 3) {
                    throw e; // 超过重试次数后抛出异常
                }
            }
        }
        System.out.println(response.toString());

        imageShoe = extractShoeInfo(response.content().text(),zxingBarcode);
        if (imageShoe == null) {
            return null;
        }
        return imageShoe;
    }


    private Shoe extractShoeInfo(String aiResponse,String zxingBarcode) {
        Shoe shoe = new Shoe();

        // 提取并设置货号
        shoe.setArticleNumber(extractInfo(aiResponse, "货号[：:](.*?)(?:\\n|$)"));

        // 提取并设置尺码大小
        String sizeStr = extractInfo(aiResponse, "尺码大小\\(EUR\\)[：:](.*?)(?:\\n|$)");

        shoe.setSize(sizeStr);
        System.out.println(zxingBarcode);
        // 提取并设置条码信息
        String gptBarcode = extractInfo(aiResponse, "条码信息[：:](.*?)(?:\\n|$)");
        if (zxingBarcode == null ){

            shoe.setCustomCode(gptBarcode);
        }else {
            shoe.setCustomCode(zxingBarcode);
        }


        return shoe;
    }

    private String extractInfo(String text, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group(1).trim().replaceAll("['']", "") : "";
    }
}
