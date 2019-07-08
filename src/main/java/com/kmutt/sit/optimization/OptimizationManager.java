package com.kmutt.sit.optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.kmutt.sit.jmetal.runner.LogisticsNsgaIIIHelper;
import com.kmutt.sit.jmetal.runner.LogisticsNsgaIIIIntegerRunner;
import com.kmutt.sit.jpa.entities.DhlRoute;
import com.kmutt.sit.jpa.entities.DhlShipment;
import com.kmutt.sit.jpa.entities.LogisticsJob;
import com.kmutt.sit.jpa.entities.LogisticsJobProblem;

import lombok.Setter;


@Controller
public class OptimizationManager {	

	private static Logger logger = LoggerFactory.getLogger(OptimizationManager.class);
	
	@Setter
	private String jobId;
	
	@Autowired
	private OptimizationHelper optimizationHelper;
	
	@Autowired
	private LogisticsNsgaIIIHelper helper;
	
	private List<DhlRoute> vanList;
	private List<DhlRoute> bikeList;
	private Map<String, Integer> scoreMapping;
	
	public OptimizationManager() {
		vanList = new ArrayList<DhlRoute>();
		bikeList = new ArrayList<DhlRoute>();
		scoreMapping = new HashMap<String, Integer>();
	}
	
	public void opitmize() {
		
        logger.info("OptimizationManager: Job ID: " + jobId + "\t start....."); 
                
        prepareInformation();
        
        // Insert table logistics_job
        initialLogisticsJob();
        
        List<String> shipmentDateList = optimizationHelper.retrieveShipmentDateList();
        
        logger.info(shipmentDateList.toString());
        
        Integer maxRun = helper.getMaxRun();
        
        // Operate each run
        for(int i = 1; i <= maxRun; i++) {        
            logger.info("Run No.: " + i + "\t starting....");
            
            helper.setCurrentRun(i);
        	
            // Operate shipments by date
            shipmentDateList.stream().forEach(date ->{
            	helper.setShipmentDate(date);        	

            	// There are two types of shipments per day; shipment for van and bike.
            	// Allocation shipment for van
            	if(optimizationHelper.getVehicleTypes().contains("Van")) {
                	allocateDailyShipmentForVan(date);
            	}
            	
            	// Allocation shipment for bike
            	if(optimizationHelper.getVehicleTypes().contains("Bike")) {
                	allocateDailyShipmentForBike(date);        		
            	}
            });
            
            logger.info("Run No.: " + i + "\t finished..");
        }

        logger.info("OptimizationManager:: Job ID: " + jobId + "\t finished..");  
	}
	
	private void allocateDailyShipmentForVan(String shipmentDate) {
        logger.info("allocateDailyShipmentForVan: start....."); 
		
		List<DhlShipment> shipmentList = optimizationHelper.retrieveDailyShipmentForVan(shipmentDate);		
		helper.setVehicleType("Van");
		helper.setShipmentList(shipmentList);
		helper.setRouteList(vanList);
		
		runNsgaIII();

        logger.info("allocateDailyShipmentForVan: finished..");  		
	}
	
	private void allocateDailyShipmentForBike(String shipmentDate) {
        logger.info("allocateDailyShipmentForBike: start....."); 
		
		List<DhlShipment> shipmentList = optimizationHelper.retrieveDailyShipmentForBike(shipmentDate);	
		helper.setVehicleType("Bike");
		helper.setShipmentList(shipmentList);
		helper.setRouteList(bikeList);
		
		runNsgaIII();

        logger.info("allocateDailyShipmentForBike: finished..");  
	}
	
	private void runNsgaIII() {
		
		if(logger.isDebugEnabled()) previewLogisticsOperate();
		
		LogisticsNsgaIIIIntegerRunner runner = new LogisticsNsgaIIIIntegerRunner(helper);
		runner.setRunnerParameter();
		runner.execute();
		
		// Insert table logistics_job_problem
		initialLogisticsJobProblem();		
	}
	
	private void previewLogisticsOperate() {		
		logger.debug("Job ID: " + helper.getJobId());
		logger.debug("Shipment Date: " + helper.getShipmentDate());
		logger.debug("Vehicle Type: " + helper.getVehicleType());
		logger.debug("No. of Shipments: " + helper.getShipmentList().size());
		logger.debug("No. of Available Routes: " + helper.getRouteList().size());
	}
	
	private void initialLogisticsJobProblem() {
		LogisticsJobProblem problem = new LogisticsJobProblem();
		problem.setJobId(helper.getJobId());
		problem.setShipmentDate(helper.getShipmentDate());
		problem.setVehicleType(helper.getVehicleType());
		
		String shipmentList = helper.getShipmentList().stream().map(s -> s.getShipmentKey()).collect(Collectors.toList()).toString();
		String routeList = helper.getRouteList().stream().map(r -> r.getChromosomeId()).collect(Collectors.toList()).toString();
		
		problem.setShipmentList(shipmentList);
		problem.setRouteList(routeList);
		
		helper.getLogisticsJobProblemRepository().save(problem);
	}
	
	private void initialLogisticsJob() {
		LogisticsJob job = new LogisticsJob();
		job.setJobId(helper.getJobId());
		job.setVehicleConfig(optimizationHelper.getVehicleTypes());
		job.setMaxRun(helper.getMaxRun());
		job.setMaxIteration(helper.getMaxIteration());
		
		helper.getLogisticsJobRepository().save(job);
	}
	
	private void prepareInformation() {		

		scoreMapping = optimizationHelper.retrieveAreaRouteScoreMap();
		helper.setJobId(jobId);
		helper.setScoreMapping(scoreMapping);
		
		vanList = optimizationHelper.retrieveRoutesOfVan();
		bikeList = optimizationHelper.retrieveRoutesOfBike();
		
		if(logger.isDebugEnabled()) {
			logger.debug("No. of Score Mapping: " + scoreMapping.size()); 

			logger.debug(""); 
			logger.debug("List of vans"); 
			vanList.stream().forEach(van -> {
				logger.debug(van.toString());
			});
			
			logger.debug(""); 
			logger.debug("List of bikes"); 
			bikeList.stream().forEach(bike -> {
				logger.debug(bike.toString());
			});
		}
	}
}
