package com.lazy.longtengzt.config;

import com.volcengine.ark.runtime.service.ArkService;
import java.util.concurrent.TimeUnit;
import lombok.Data;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: longtengzhitu-backend
 * @description: Ai 配置类
 * @author: Lazy
 * @create: 2025-07-01 14:13
 **/
@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
public class AiConfig {

    // ApiKey
    private String apiKey;

    /**
     * AI 请求客户端
     *
     * @return
     */
    @Bean
    public ArkService arkService() {
        ConnectionPool connectionPool = new ConnectionPool(5, 1, TimeUnit.SECONDS);
        Dispatcher dispatcher = new Dispatcher();
        return ArkService.builder().dispatcher(dispatcher).connectionPool(connectionPool)
            .baseUrl("https://ark.cn-beijing.volces.com/api/v3")
            .apiKey(apiKey)
            .build();
    }
}
