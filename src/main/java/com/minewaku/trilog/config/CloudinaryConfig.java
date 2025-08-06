package com.minewaku.trilog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.minewaku.trilog.config.properties.CloudinaryProperties;
import com.minewaku.trilog.util.LogUtil;

@Configuration
@EnableConfigurationProperties(CloudinaryProperties.class)
public class CloudinaryConfig {

    @Autowired
    private CloudinaryProperties cloudinaryProperties;

    @Bean
    public Cloudinary cloudinary() {
        try {
        	LogUtil.LOGGER.info("Initializing Cloudinary with:\n"
        			+ "cloud_name: {},\n"
        			+ "api_key: {},\n"
        			+ "api_serect: {},\n"
        			+ "secure: {}\n",
                cloudinaryProperties.getCloud_name(), 
                cloudinaryProperties.getApi_key(), 
                cloudinaryProperties.getApi_secret(),
                cloudinaryProperties.isSecure());
        	
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
