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
import com.kmutt.sit.batch.tasks.DatabaseReader;
import com.kmutt.sit.batch.tasks.GeneticAlgorithmProcessor;

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
    

    @Autowired
    private DatabaseReader databaseReader;
     
    @Bean
    public Job processJob(){
    	logger.info("processJob(): ...");
    	
        return jobs.get("processJob")
                .incrementer(new RunIdIncrementer())
                .start(readConfigProperties())
                .next(retrivePlayers())
                .next(processGeneticAlgorithm())
                .build();
    }
    
    @Bean
    public Step readConfigProperties(){
    	logger.info("readConfigProperties(): ...");
    	
        return steps.get("Step-01")
                .tasklet(configReader)
                .build();
    }
     
    @Bean
    public Step retrivePlayers(){
    	logger.info("retrivePlayers(): ...");
    	
        return steps.get("Step-02")
                .tasklet(databaseReader)
                .build();
    }  

    @Bean
    public Step processGeneticAlgorithm(){
    	logger.info("ProcessGeneticAlgorithm(): ...");
    	
        return steps.get("Step-03")
                .tasklet(new GeneticAlgorithmProcessor())
                .build();
    }  
    
}
