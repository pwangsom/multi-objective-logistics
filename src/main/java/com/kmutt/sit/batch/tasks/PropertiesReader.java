package com.kmutt.sit.batch.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import com.kmutt.sit.properties.DefaultProperties;

@Service
public class PropertiesReader implements Tasklet {	

	private static Logger logger = LoggerFactory.getLogger(PropertiesReader.class);
	
	@Autowired
	private DefaultProperties defaultProps;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
		
        logger.info("PropertiesReader: start..");
        
        String propertyFile = chunkContext.getStepContext().getStepExecution().getJobParameters().getString("PropertyFile");
        
        logger.info("");
        logger.info("Propety File: " + propertyFile);
        logger.info("Profile     : " + defaultProps.getProfile());
        logger.info("Developer   : " + defaultProps.getDevelop());
        logger.info("");

        logger.info("PropertiesReader: finished..");
		
		return RepeatStatus.FINISHED;
	}
	
	private PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer(String fileName) {
	    PropertySourcesPlaceholderConfigurer properties = new PropertySourcesPlaceholderConfigurer();
	    properties.setLocation(new FileSystemResource(fileName));
	    properties.setIgnoreResourceNotFound(false);
	    return properties;
	}

}
