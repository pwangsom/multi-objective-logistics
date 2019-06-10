package com.kmutt.sit.batch.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class DemoTaskOne implements Tasklet {	

	private static Logger LOG = LoggerFactory.getLogger(DemoTaskOne.class);

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub
		
        LOG.info("DemoTaskOne start..");
        
        for (int i = 0; i < 10; ++i) {
            LOG.info("DemoTaskOne: {}", i);
        }

        LOG.info("DemoTaskOne finished..");
		
		return RepeatStatus.FINISHED;
	}

}
