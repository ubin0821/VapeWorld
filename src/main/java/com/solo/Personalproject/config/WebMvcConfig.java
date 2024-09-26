package com.solo.Personalproject.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${uploadPath}")
    String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        registry.addResourceHandler("/images/**")
                // /images 로 시작하는 경우 uploadPath 에 설정한 폴더를 기준으로 파일을
                // 읽어오도록 설정
                .addResourceLocations(uploadPath);
        registry.addResourceHandler("/item/images/**")
                // /images 로 시작하는 경우 uploadPath 에 설정한 폴더를 기준으로 파일을
                // 읽어오도록 설정
                .addResourceLocations(uploadPath);
        registry.addResourceHandler("/item/**").addResourceLocations(uploadPath);
    }

}
