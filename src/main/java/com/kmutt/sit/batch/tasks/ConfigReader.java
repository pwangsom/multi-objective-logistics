package com.kmutt.sit.batch.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kmutt.sit.properties.ConfigProperties;

@Service
public class ConfigReader implements Tasklet {	

	private static Logger logger = LoggerFactory.getLogger(ConfigReader.class);
	
	@Autowired
	private ConfigProperties configs;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
		
        logger.info("DemoTaskOne start..");
        
        logger.info("");
        logger.info("University: " + configs.getUniversity());
        logger.info("School    : " + configs.getSchool());
        logger.info("");

        logger.info("DemoTaskOne finished..");
		
		return RepeatStatus.FINISHED;
	}

}
