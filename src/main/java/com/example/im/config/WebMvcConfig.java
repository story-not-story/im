package com.example.im.config;

/**
 * @author HuJun
 * @date 2020/4/10 10:58 下午
 */
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${img.linux}")
    private String imgLinux;
    @Value("${img.windows}")
    private String imgWindows;
    @Value("${file.linux}")
    private String fileLinux;
    @Value("${file.windows}")
    private String fileWindows;

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedHeaders("Content-Type", "Content-Length", "Authorization", "Accept", "X-Requested-With", "x-forwarded-for", "Proxy-Client-IP", "WL-Proxy-Client-IP")
//                .allowedMethods("GET", "HEAD", "POST", "PUT", "PATCH", "DELETE", "OPTIONS").
//                allowedOrigins("http://mango.nat100.top", "http://localhost:8080").allowCredentials(true).maxAge(3600);
//    }

    public String getImgUrl() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return imgWindows;
        } else {
            return imgLinux;
        }
    }

    public String getFileUrl() {
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {
            return fileWindows;
        } else {
            return fileLinux;
        }
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("file:" + getImgUrl());
        registry.addResourceHandler("/file/**").addResourceLocations("file:" + getFileUrl());
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
