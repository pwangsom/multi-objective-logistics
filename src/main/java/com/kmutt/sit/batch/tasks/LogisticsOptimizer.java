package com.kmutt.sit.batch.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.kmutt.sit.jmetal.runner.DemoNsgaIIIRunner;

@Component
public class LogisticsOptimizer implements Tasklet {

	private static Logger logger = LoggerFactory.getLogger(LogisticsOptimizer.class);
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub

        logger.info("LogisticsOptimizer: start....."); 
        
        String jobId = chunkContext.getStepContext().getStepExecution().getJobParameters().getString("JobID");
        logger.info("Job ID: " + jobId);
        
        DemoNsgaIIIRunner runner = new DemoNsgaIIIRunner();
        runner.setup();
        runner.execute();
        
        logger.info("LogisticsOptimizer: finished..");  
		
		return RepeatStatus.FINISHED;
	}

}
