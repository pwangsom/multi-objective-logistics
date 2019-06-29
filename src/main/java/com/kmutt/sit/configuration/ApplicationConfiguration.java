package com.kmutt.sit.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.kmutt.sit.optimization.OptimizationManager;

@Configuration
@ComponentScan("com.kmutt.sit")
public class ApplicationConfiguration {

	@Bean
	public OptimizationManager optimizationManager() {
		return new OptimizationManager();		
	}
	
}
