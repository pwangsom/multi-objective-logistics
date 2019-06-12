package com.kmutt.sit.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kmutt.sit.batch.tasks.ConfigReader;
import com.kmutt.sit.batch.tasks.DemoTaskTwo;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	private static Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);
	
    @Autowired
    private JobBuilderFactory jobs;
 
    @Autowired
    private StepBuilderFactory steps;
    
    @Autowired
    private ConfigReader configReader;
     
    @Bean
    public Job processJob(){
    	logger.info("processJob()");
    	
        return jobs.get("processJob")
                .incrementer(new RunIdIncrementer())
                .start(readConfigProperties())
                .next(stepTwo())
                .build();
    }
    
    @Bean
    public Step readConfigProperties(){
    	logger.info("readConfigProperties()");
    	
        return steps.get("Step-01")
                .tasklet(configReader)
                .build();
    }
     
    @Bean
    public Step stepTwo(){
    	logger.info("stepTwo()");
    	
        return steps.get("Step-01")
                .tasklet(new DemoTaskTwo())
                .build();
    }  

}
