package com.kmutt.sit.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import lombok.Getter;

@Getter
@Configuration
@PropertySource("classpath:default.properties")
public class DefaultProperties {
	
    @Value("${app.profile}")
    private String profile;
    
    @Value("${app.develop}")
    private String develop;
    
    @Bean
    public DefaultProperties defaultProps() {
    	DefaultProperties defaultProps = new DefaultProperties();    	
    	return defaultProps;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholder() {
        return new PropertySourcesPlaceholderConfigurer();
    }
	
}
