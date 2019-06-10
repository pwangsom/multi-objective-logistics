package com.kmutt.sit.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kmutt.sit.batch.tasks.DemoTaskOne;
import com.kmutt.sit.batch.tasks.DemoTaskTwo;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	
    @Autowired
    private JobBuilderFactory jobs;
 
    @Autowired
    private StepBuilderFactory steps;
     
    @Bean
    public Step stepOne(){
        return steps.get("stepOne")
                .tasklet(new DemoTaskOne())
                .build();
    }
     
    @Bean
    public Step stepTwo(){
        return steps.get("stepTwo")
                .tasklet(new DemoTaskTwo())
                .build();
    }  
     
    @Bean
    public Job demoJob(){
        return jobs.get("demoJob")
                .incrementer(new RunIdIncrementer())
                .start(stepOne())
                .next(stepTwo())
                .build();
    }

}
