package com.kmutt.sit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogisticsApplication implements CommandLineRunner {
	
	private static Logger logger = LoggerFactory.getLogger(LogisticsApplication.class);
	
    @Autowired
    JobLauncher jobLauncher;
     
    @Autowired
    Job job;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
        logger.info("STARTING THE APPLICATION");
        
        SpringApplication.run(LogisticsApplication.class, args);
        
        logger.info("APPLICATION FINISHED");
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
        logger.info("Job: running..");
        
        JobParameters params = new JobParametersBuilder()
                .addString("JobID", String.valueOf(System.currentTimeMillis()))
                .toJobParameters();
        
		jobLauncher.run(job, params);
		
        logger.info("Job: done..");
	}

}
