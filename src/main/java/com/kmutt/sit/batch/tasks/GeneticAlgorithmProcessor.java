package com.kmutt.sit.batch.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import com.kmutt.sit.batch.jemetal.runner.DemoNsgaIIIRunner;

public class GeneticAlgorithmProcessor implements Tasklet {

	private static Logger logger = LoggerFactory.getLogger(GeneticAlgorithmProcessor.class);
	
	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		// TODO Auto-generated method stub

        logger.info("ProcessGeneticAlgorithm: start....."); 
        
        DemoNsgaIIIRunner runner = new DemoNsgaIIIRunner();
        runner.setup();
        runner.execute();
        
        logger.info("ProcessGeneticAlgorithm: finished..");  
		
		return RepeatStatus.FINISHED;
	}

}
