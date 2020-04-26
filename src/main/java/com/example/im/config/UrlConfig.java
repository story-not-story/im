package com.example.im.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author HuJun
 * @date 2020/3/21 8:42 下午
 */
@Component
@Data
@ConfigurationProperties(prefix = "url")
public class UrlConfig {
    private String host;
}

