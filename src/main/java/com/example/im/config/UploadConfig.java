package com.example.im.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

/**
 * @author HuJun
 * @date 2020/5/30 6:05 下午
 */
@Configuration
public class UploadConfig {
    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大 5m 可以使用读取配置
        factory.setMaxFileSize(DataSize.ofMegabytes(5)); //KB,MB
        /// 设置总上传数据总大小 50m
        factory.setMaxRequestSize(DataSize.ofMegabytes(50));
        return factory.createMultipartConfig();
    }
}
