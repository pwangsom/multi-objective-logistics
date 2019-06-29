package com.kmutt.sit.optimization;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.kmutt.sit.jpa.entities.DhlShipment;

import lombok.Setter;


@Controller
public class OptimizationManager {	

	private static Logger logger = LoggerFactory.getLogger(OptimizationManager.class);
	
	@Setter
	private String jobId;
	
	@Autowired
	private OptimizationHelper optimizationHelper;
	
	public void opitmize() {
		
        logger.info("OptimizationManager: start....."); 
        
        logger.info("Job ID: " + jobId);
        List<String> shipmentDateList = optimizationHelper.retrieveShipmentDateList();
        
        logger.info(shipmentDateList.toString());
        
        shipmentDateList.stream().forEach(date ->{
        	allocateDailyShipment(date);
        });

        logger.info("OptimizationManager: finished..");  
	}
	
	private void allocateDailyShipment(String shipmentDate) {

        logger.info("allocateDailyShipment: start....."); 
		
		List<DhlShipment> shipmentList = optimizationHelper.retrieveDailyShipment(shipmentDate);
		logger.debug("No. of Shipments: " + shipmentList.size());
		

        logger.info("allocateDailyShipment: finished..");  
	}
	
	
}
