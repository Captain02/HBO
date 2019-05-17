package io.renren.common.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;

@Configuration
public class PathConfig {



    @Bean
    MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        String property = System.getProperty("user.dir")+"/file";
        String path = "F:\\tomcat\\HBO";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        factory.setLocation(path);
        return factory.createMultipartConfig();
    }

}
