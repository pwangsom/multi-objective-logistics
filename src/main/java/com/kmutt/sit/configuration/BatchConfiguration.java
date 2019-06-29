package com.kmutt.sit.configuration;

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

import com.kmutt.sit.batch.tasks.DatabaseReader;
import com.kmutt.sit.batch.tasks.LogisticsOptimizer;
import com.kmutt.sit.batch.tasks.PropertiesReader;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
	private static Logger logger = LoggerFactory.getLogger(BatchConfiguration.class);
	
    @Autowired
    private JobBuilderFactory jobs;
 
    @Autowired
    private StepBuilderFactory steps;
    
    @Autowired
    private PropertiesReader propertiesReader;
    

    @Autowired
    private DatabaseReader databaseReader;
     
    @Bean
    public Job processJob(){
    	logger.info("processJob(): ...");
    	
        return jobs.get("processJob")
                .incrementer(new RunIdIncrementer())
                .start(readProperties())
                .next(retrivePlayers())
                .next(optimizeShipmentLogistics())
                .build();
    }
    
    @Bean
    public Step readProperties(){    	
        return steps.get("Step-01")
                .tasklet(propertiesReader)
                .build();
    }
     
    @Bean
    public Step retrivePlayers(){    	
        return steps.get("Step-02")
                .tasklet(databaseReader)
                .build();
    }  

    @Bean
    public Step optimizeShipmentLogistics(){    	
        return steps.get("Step-03")
                .tasklet(new LogisticsOptimizer())
                .build();
    }  
    
}