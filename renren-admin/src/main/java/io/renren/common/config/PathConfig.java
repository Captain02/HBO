package io.renren.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;

@Configuration
public class PathConfig {

    @Value("${fileUploudPath}")
    public String FILEUPLOUD;


    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        File file = new File(FILEUPLOUD);
        if (!file.exists()) {
            file.mkdirs();
        }
        factory.setLocation(FILEUPLOUD);
        return factory.createMultipartConfig();
    }

}
