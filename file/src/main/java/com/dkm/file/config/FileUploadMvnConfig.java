package com.dkm.file.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;

/**
 * @Author: HuangJie
 * @Date: 2020/4/2 11:44
 * @Version: 1.0V
 */
@Configuration
public class FileUploadMvnConfig implements WebMvcConfigurer {
    @Value("${image.upload.controller.path}")
    private String imageUploadPath;
    @Value("${image.upload.localhost.path}")
    private String imageLocalPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(imageUploadPath+"**").addResourceLocations("file:"+ File.separator+imageLocalPath);
    }
}
