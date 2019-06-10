package com.kmutt.sit.batch.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class DemoTaskTwo implements Tasklet {

	private static Logger logger = LoggerFactory.getLogger(DemoTaskTwo.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
		
        logger.info("DemoTaskTwo start..");
        
        for (int i = 0; i < 10; ++i) {
            logger.info("DemoTaskTwo: {}", i);
        }

        logger.info("DemoTaskTwo finished..");
		
		return RepeatStatus.FINISHED;
	}
	
}
