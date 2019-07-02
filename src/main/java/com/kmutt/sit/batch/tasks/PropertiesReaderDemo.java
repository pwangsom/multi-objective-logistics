package com.kmutt.sit.batch.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PropertiesReaderDemo implements Tasklet {	

	private static Logger logger = LoggerFactory.getLogger(PropertiesReaderDemo.class);
	
    @Value("${app.profile}")
    private String profile;
    
    @Value("${app.develop}")
    private String develop;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
		
        logger.info("PropertiesReader: start..");
        
        logger.info("");
        logger.info("Profile      : " + profile);
        logger.info("Developer    : " + develop);
        logger.info("");

        logger.info("PropertiesReader: finished..");
		
		return RepeatStatus.FINISHED;
	}	
}
