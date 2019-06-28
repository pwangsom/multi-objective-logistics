package com.kmutt.sit;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogisticsApplication implements CommandLineRunner {
	
    @Autowired
    JobLauncher jobLauncher;
     
    @Autowired
    Job job;

	public static void main(String[] args) {
		// TODO Auto-generated method stub		
        SpringApplication.run(LogisticsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub        
		
		String pathFileName = "NULL";
		
        for(String arg:args) {
            System.out.println(arg);
            if(arg.contains("--file") && arg.split("=").length == 2) {
            	pathFileName = arg.split("=")[1];
            }
        }
        
        Map<String, JobParameter> params = new HashMap<String, JobParameter>();
        params.put("JobID", new JobParameter(String.valueOf(System.currentTimeMillis())));
        
        if(!pathFileName.equalsIgnoreCase("NULL")) {
        	params.put("PropertyFile", new JobParameter(pathFileName));
        }
        
        JobParameters parameters = new JobParameters(params);
		
		/*
		 * JobParameters parameters = new JobParametersBuilder() .addString("JobID",
		 * String.valueOf(System.currentTimeMillis())) .toJobParameters();
		 */
        
		jobLauncher.run(job, parameters);
	}

}
