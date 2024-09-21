package com.sky.properties;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "local")
public class LocalFileProperties {
    private boolean enable;
    private String basePath;
    private String baseUrl;
}
