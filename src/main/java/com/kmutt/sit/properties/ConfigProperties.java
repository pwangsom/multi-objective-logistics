package com.kmutt.sit.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource("classpath:config.properties")
public class ConfigProperties {
	
    @Value("${name.university}")
    private String university;
    
    @Value("${name.school}")
    private String school;

	public String getUniversity() {
		return university;
	}

	public String getSchool() {
		return school;
	}
    
    @Bean
    public ConfigProperties configs() {
    	ConfigProperties configs = new ConfigProperties();    	
    	return configs;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholder() {
        return new PropertySourcesPlaceholderConfigurer();
    }
	
}
