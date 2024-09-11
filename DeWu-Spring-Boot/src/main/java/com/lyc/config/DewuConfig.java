package com.lyc.config;

import com.dewu.sdk.base.BaseClient;
import com.dewu.sdk.base.constans.CommonConstants;
import com.dewu.sdk.factory.Factory;
import com.lyc.entity.Shoe;
import com.lyc.service.DewuApiService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DewuConfig {

    @Value("${dewu.default.appKey:defaultAppKey}")
    private String defaultAppKey;

    @Value("${dewu.default.secret:defaultSecret}")
    private String defaultSecret;

    private BaseClient.Config currentConfig;



    public void setConfig(String appKey, String secret) {
        System.out.println("appKey: " + appKey);
        System.out.println("secret: " + secret);

        BaseClient.Config config = new BaseClient.Config();
        config.setGatewayHost("https://openapi.dewu.com");
        config.setAppKey(appKey == null || appKey.trim().isEmpty() ? defaultAppKey : appKey);
        config.setSecret(secret == null || secret.trim().isEmpty() ? defaultSecret : secret);
        config.setProtocol(CommonConstants.HTTPS);

        Factory.setOptions(config);
        this.currentConfig = config; // Store the current configuration

    }

    public BaseClient.Config getConfig() {
        return this.currentConfig;
    }

    @PostConstruct
    public void init() {
        setConfig(null, null); // 调用配置方法，传入null以使用默认值
    }
}
