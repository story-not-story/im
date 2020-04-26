package com.example.im.config;

/**
 * @author HuJun
 * @date 2020/4/10 10:58 下午
 */
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${img.linux}")
    private String imgLinux;
    @Value("${img.windows}")
    private String imgWindows;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedHeaders("Content-Type", "Content-Length", "Authorization", "Accept", "X-Requested-With", "x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS").
                allowedOrigins("http://mango.nat100.top", "http://localhost:8080").allowCredentials(true).maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            registry.addResourceHandler("/img/**").addResourceLocations("file:" + imgWindows);
        } else {
            registry.addResourceHandler("/img/**").addResourceLocations("file:" + imgLinux);
        }
    }
}
