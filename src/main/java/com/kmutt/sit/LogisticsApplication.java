package com.kmutt.sit;

import java.text.SimpleDateFormat;
import java.util.Date;

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
		
		Date dateTime = new Date();		
		String jobId = new SimpleDateFormat("yyMMddHHmmss").format(dateTime);
		
		JobParameters parameters = new JobParametersBuilder()
				.addString("JobID", jobId).toJobParameters();
        
		jobLauncher.run(job, parameters);
	}

}
