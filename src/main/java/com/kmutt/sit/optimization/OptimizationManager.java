package com.kmutt.sit.optimization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Controller
public class OptimizationManager {	

	private static Logger logger = LoggerFactory.getLogger(OptimizationManager.class);
	
	private String jobId;
	
	public void opitmize() {
		
        logger.info("OptimizationManager: start....."); 
        
        logger.info("Job ID: " + jobId);

        logger.info("OptimizationManager: finished..");  
	}
	
	
}
