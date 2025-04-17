package com.minewaku.trilog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.minewaku.trilog.config.properties.CloudinaryProperties;

@Configuration
@EnableConfigurationProperties(CloudinaryProperties.class)
public class CloudinaryConfig {

    @Autowired
    private CloudinaryProperties cloudinaryProperties;

    @Bean CloudinaryProperties cloudinaryProperties() {
        return new CloudinaryProperties();
    }
    
    @Bean
    public Cloudinary cloudinary() {
        try {
            return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudinaryProperties.getCloud_name(),
                "api_key", cloudinaryProperties.getApi_key(),
                "api_secret", cloudinaryProperties.getApi_secret(),
                "secure", cloudinaryProperties.isSecure()));
        } catch(Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
