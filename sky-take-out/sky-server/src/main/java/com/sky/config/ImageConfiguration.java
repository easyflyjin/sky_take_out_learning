package com.sky.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ImageConfiguration implements WebMvcConfigurer{
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/staticResources/**") // 自定义URL访问前缀，和file配置一致
        .addResourceLocations("file:C:\\Users\\11031\\Desktop\\sky_take_out\\back\\sky-take-out\\sky-server\\src\\main\\resources\\staticResources\\");
    }
}
