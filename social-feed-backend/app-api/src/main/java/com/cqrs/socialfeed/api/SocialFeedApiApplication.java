package com.cqrs.socialfeed.api;

import com.cqrs.socialfeed.api.config.CustomKafkaProperties;
import com.cqrs.socialfeed.api.file.UploadProperties;
import com.cqrs.socialfeed.api.security.AuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = "com.cqrs")
@EnableConfigurationProperties({AuthProperties.class, UploadProperties.class, CustomKafkaProperties.class})
public class SocialFeedApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocialFeedApiApplication.class, args);
    }
}
