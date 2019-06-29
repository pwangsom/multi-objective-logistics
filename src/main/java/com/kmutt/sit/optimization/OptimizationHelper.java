package com.kmutt.sit.optimization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kmutt.sit.jpa.respositories.DhlShipmentRepository;

@Service
public class OptimizationHelper {

	private static Logger logger = LoggerFactory.getLogger(OptimizationHelper.class);
	
    @Value("${shipment.month}")
    private String shipmentMonth;
    
    @Value("${shipment.date}")
    private String shipmentDate;
    
    @Autowired
    private DhlShipmentRepository dhlShipmentRepository;

    private List<String> shipmentDateList;
    
    public OptimizationHelper() {
    	this.shipmentDateList = new ArrayList<String>();
    }
    
    public List<String> retrieveShipmentDateList(){
    	
    	// For all days in month
    	if(this.shipmentDate.contentEquals("00")) {
    		this.shipmentDateList.addAll(dhlShipmentRepository.findDistinctActDt());
    		
    	} // For multiple days
    	  else if(this.shipmentDate.length() > 2 && this.shipmentDate.contains(",")){
    		
    		String[] dates = this.shipmentDate.split(",");
    		
    		Arrays.asList(dates).stream().forEach(d ->{
    			this.shipmentDateList.add(this.shipmentMonth + d);
    		});
    		
    	} // For single day
    	  else if(this.shipmentDate.length() == 2) {
    		this.shipmentDateList.add(this.shipmentMonth + this.shipmentDate);
    	}
    	
    	Collections.sort(this.shipmentDateList);
    	
    	return this.shipmentDateList;
    }

}
