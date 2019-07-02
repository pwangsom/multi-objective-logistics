package com.kmutt.sit.optimization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.kmutt.sit.jmetal.runner.LogisticsNsgaIIIHelper;
import com.kmutt.sit.jmetal.runner.LogisticsNsgaIIIIntegerRunner;
import com.kmutt.sit.jpa.entities.DhlRoute;
import com.kmutt.sit.jpa.entities.DhlShipment;

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
		
        logger.info("OptimizationManager: start....."); 
        
        logger.info("Job ID: " + jobId);
        
        prepareInformation();
        
        List<String> shipmentDateList = optimizationHelper.retrieveShipmentDateList();
        
        logger.info(shipmentDateList.toString());
        
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

        logger.info("OptimizationManager: finished..");  
	}
	
	private void allocateDailyShipmentForVan(String shipmentDate) {
        logger.info("allocateDailyShipmentForVan: start....."); 
		
		List<DhlShipment> shipmentList = optimizationHelper.retrieveDailyShipmentForVan(shipmentDate);		
		helper.setVehicleType("Van");
		helper.setShipmentList(shipmentList);
		helper.setRouteList(vanList);
		
		if(logger.isDebugEnabled()) previewLogisticsOperate();
		
		LogisticsNsgaIIIIntegerRunner runner = new LogisticsNsgaIIIIntegerRunner(helper);
		runner.setRunnerParameter();
		runner.execute();

        logger.info("allocateDailyShipmentForVan: finished..");  		
	}
	
	private void allocateDailyShipmentForBike(String shipmentDate) {
        logger.info("allocateDailyShipmentForBike: start....."); 
		
		List<DhlShipment> shipmentList = optimizationHelper.retrieveDailyShipmentForBike(shipmentDate);	
		helper.setVehicleType("Bike");
		helper.setShipmentList(shipmentList);
		helper.setRouteList(bikeList);
		
		if(logger.isDebugEnabled()) previewLogisticsOperate();
		
		LogisticsNsgaIIIIntegerRunner runner = new LogisticsNsgaIIIIntegerRunner(helper);
		runner.setRunnerParameter();
		runner.execute();

        logger.info("allocateDailyShipmentForBike: finished..");  
	}
	
	private void previewLogisticsOperate() {		
		logger.debug("Job ID: " + helper.getJobId());
		logger.debug("Shipment Date: " + helper.getShipmentDate());
		logger.debug("Vehicle Type: " + helper.getVehicleType());
		logger.debug("No. of Shipments: " + helper.getShipmentList().size());
		logger.debug("No. of Available Routes: " + helper.getRouteList().size());
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
