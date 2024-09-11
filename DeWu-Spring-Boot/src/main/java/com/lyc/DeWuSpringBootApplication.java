package com.lyc;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.TimeZone;

@SpringBootApplication
public class DeWuSpringBootApplication {
    @PostConstruct
    public void init() {
        // 设置默认时区
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
    }
    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(DeWuSpringBootApplication.class, args);


        // 打印数据库配置
        System.out.println("Database Username: " + context.getEnvironment().getProperty("spring.datasource.username"));
        System.out.println("Database Password: " + context.getEnvironment().getProperty("spring.datasource.password"));
    }


}
