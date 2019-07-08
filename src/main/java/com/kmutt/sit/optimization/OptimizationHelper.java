package com.kmutt.sit.optimization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kmutt.sit.jpa.entities.DhlAreaRouteScore;
import com.kmutt.sit.jpa.entities.DhlRoute;
import com.kmutt.sit.jpa.entities.DhlShipment;
import com.kmutt.sit.jpa.entities.LogisticsJob;
import com.kmutt.sit.jpa.entities.LogisticsJobProblem;
import com.kmutt.sit.jpa.entities.LogisticsJobResult;
import com.kmutt.sit.jpa.respositories.DhlAreaRouteScoreRespository;
import com.kmutt.sit.jpa.respositories.DhlRouteRespository;
import com.kmutt.sit.jpa.respositories.DhlShipmentRepository;
import com.kmutt.sit.jpa.respositories.LogisticsJobProblemRepository;
import com.kmutt.sit.jpa.respositories.LogisticsJobRepository;
import com.kmutt.sit.jpa.respositories.LogisticsJobResultRepository;

import lombok.Getter;

@Service
public class OptimizationHelper {

	private static Logger logger = LoggerFactory.getLogger(OptimizationHelper.class);
	
    @Value("${shipment.month}")
    private String shipmentMonth;
    
    @Value("${shipment.date}")
    private String shipmentDate;
    
    @Getter
    @Value("${vehicle.types}")
    private String vehicleTypes;
    
    @Value("${vehicle.van}")
    private String vanType;
    
    @Value("${vehicle.bike}")
    private String bikeType;
    
    @Getter
    private List<String> vehicleTypeList;
    private List<String> vanTypes;
    private List<String> bikeTypes;
    
    @Autowired
    private DhlShipmentRepository dhlShipmentRepository;
    
    @Autowired
    private DhlAreaRouteScoreRespository dhlAreaRouteScoreRespository;
    
    @Autowired
    private DhlRouteRespository dhlRouteRespository;
    

    @Autowired
    private LogisticsJobRepository logisticsJobRepository;
    
    @Autowired
    private LogisticsJobProblemRepository logisticsJobProblemRepository;
    
    @Autowired
    private LogisticsJobResultRepository logisticsJobResultRepository;

    private List<String> shipmentDateList;
    
    public OptimizationHelper() {
    	this.shipmentDateList = new ArrayList<String>();
    }
    
    @PostConstruct
    private void postConstruct() {
    	this.vehicleTypeList = Arrays.asList(vehicleTypes.split(","));
    	this.vanTypes = Arrays.asList(vanType.split(","));
    	this.bikeTypes = Arrays.asList(bikeType.split(","));
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
    
    public void saveLogisticsJob(LogisticsJob job) {
    	logisticsJobRepository.save(job);
    }
    
    public LogisticsJobProblem saveLogisticsJobProblem(LogisticsJobProblem problem) {
    	LogisticsJobProblem persistProblem = logisticsJobProblemRepository.save(problem);
    	logisticsJobProblemRepository.flush();
    	return persistProblem;
    }
    
    @Transactional
    public void saveLogisticsJobResult(List<LogisticsJobResult> results) {    
    	logisticsJobResultRepository.saveAll(results);
    }
    
    public List<DhlShipment> retrieveDailyShipment(String shipmentDate){
    	return dhlShipmentRepository.findByActDt(shipmentDate);
    }
    
    public List<DhlShipment> retrieveDailyShipmentForVan(String shipmentDate){
    	return dhlShipmentRepository.findByActDtAndCycleOperateAndVehicleTypeIn(shipmentDate, "A", vanTypes);
    }
    
    public List<DhlShipment> retrieveDailyShipmentForBike(String shipmentDate){
    	return dhlShipmentRepository.findByActDtAndCycleOperateAndVehicleTypeIn(shipmentDate, "A", bikeTypes);
    }
    
    public List<DhlRoute> retrieveRoutesOfVan(){
    	return dhlRouteRespository.findByVehicleTypeInOrderByChromosomeIdAsc(vanTypes);
    }
    
    public List<DhlRoute> retrieveRoutesOfBike(){
    	return dhlRouteRespository.findByVehicleTypeInOrderByChromosomeIdAsc(bikeTypes);
    }
    
    public Map<String, Integer> retrieveAreaRouteScoreMap(){    	
    	Map<String, Integer> map = new HashMap<String, Integer>();
    	
    	List<DhlAreaRouteScore> scoreList = dhlAreaRouteScoreRespository.findAll();
    	
    	scoreList.stream().forEach(score ->{
    		map.put(score.getAreaCode() + "-" + score.getRoute(), score.getScore());
    	});
    	
    	return map;
    }

}
