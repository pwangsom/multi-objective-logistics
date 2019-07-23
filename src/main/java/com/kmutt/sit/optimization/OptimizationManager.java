package com.kmutt.sit.optimization;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.SolutionListUtils;
import org.uma.jmetal.util.front.Front;
import org.uma.jmetal.util.front.imp.ArrayFront;
import org.uma.jmetal.util.front.util.FrontNormalizer;
import org.uma.jmetal.util.point.util.PointSolution;

import com.kmutt.sit.jmetal.runner.LogisticsNsgaIIIHelper;
import com.kmutt.sit.jmetal.runner.LogisticsNsgaIIIIntegerRunner;
import com.kmutt.sit.jpa.entities.DhlRoute;
import com.kmutt.sit.jpa.entities.DhlShipment;
import com.kmutt.sit.jpa.entities.LogisticsJob;
import com.kmutt.sit.jpa.entities.LogisticsJobProblem;
import com.kmutt.sit.jpa.entities.LogisticsJobResult;
import com.kmutt.sit.utilities.JavaUtils;

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
        saveLogisticsJob();
        
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
		
        List<IntegerSolution> solutions = new ArrayList<IntegerSolution>();
		
        // Allocate each run
        for(int i = 1; i <= helper.getMaxRun(); i++) {
        	
        	String runInfo = String.format("[Job ID: %s, Shipment Date: %s, Vehicle: %s, Run No: %d, Max Run: %d]", 
        					jobId, helper.getShipmentDate(), helper.getVehicleType(), i, helper.getMaxRun());

            logger.debug(runInfo + ": Starting....");
        	
            helper.setCurrentRun(i);
        	
    		LogisticsNsgaIIIIntegerRunner runner = new LogisticsNsgaIIIIntegerRunner(helper);
    		runner.setRunnerParameter();
    		runner.execute();
    		
    		solutions.addAll(runner.getSolutions());

    		logger.debug(runInfo + ": Finished....");
        }
        
        List<IntegerSolution> paretoSet = SolutionListUtils.getNondominatedSolutions(solutions);
        Front referenceFront = new ArrayFront(paretoSet);
        FrontNormalizer frontNormalizer = new FrontNormalizer(referenceFront);            
        @SuppressWarnings("unchecked")
		List<PointSolution> normalizedParetoSet = (List<PointSolution>) frontNormalizer.normalize(paretoSet);
		
		// Insert table logistics_job_problem
        LogisticsJobProblem problem = saveLogisticsJobProblem(paretoSet.size());
        
        saveLogisticsJobResults(problem.getProblemId(), paretoSet, normalizedParetoSet) ;
	}
	
	private void saveLogisticsJobResults(Integer problemId, List<IntegerSolution> paretoSet) {
		
		List<LogisticsJobResult> results = new ArrayList<LogisticsJobResult>();
		
		
		IntStream.range(0, paretoSet.size()).forEach(i -> {
			LogisticsJobResult result = new LogisticsJobResult();
			result.setProblemId(problemId);
			result.setSolutionIndex(i);
			
			IntegerSolution paretoSolution = paretoSet.get(i);			
			String routeList = JavaUtils.removeStringOfList(getSolutionString(paretoSolution));
			result.setSolutionDetail(routeList);			
			result.setObjective1(BigDecimal.valueOf(paretoSolution.getObjective(0)));
			result.setObjective2(BigDecimal.valueOf(paretoSolution.getObjective(1)));
			result.setObjective3(BigDecimal.valueOf(paretoSolution.getObjective(2)));
			
			result.setNormalizedObjective1(BigDecimal.valueOf(0.0));
			result.setNormalizedObjective2(BigDecimal.valueOf(0.0));
			result.setNormalizedObjective3(BigDecimal.valueOf(0.0));
			

			results.add(result);
		});
		
		optimizationHelper.saveLogisticsJobResult(results);
	}
	
	private void saveLogisticsJobResults(Integer problemId, List<IntegerSolution> paretoSet, List<PointSolution> normalizedParetoSet) {
		
		List<LogisticsJobResult> results = new ArrayList<LogisticsJobResult>();
		
		
		IntStream.range(0, paretoSet.size()).forEach(i -> {
			LogisticsJobResult result = new LogisticsJobResult();
			result.setProblemId(problemId);
			result.setSolutionIndex(i);
			
			IntegerSolution paretoSolution = paretoSet.get(i);			
			String routeList = JavaUtils.removeStringOfList(getSolutionString(paretoSolution));
			result.setSolutionDetail(routeList);			
			result.setObjective1(BigDecimal.valueOf(paretoSolution.getObjective(0)));
			result.setObjective2(BigDecimal.valueOf(paretoSolution.getObjective(1)));
			result.setObjective3(BigDecimal.valueOf(paretoSolution.getObjective(2)));
			
			PointSolution normalizedParetoSolution = normalizedParetoSet.get(i);
			result.setNormalizedObjective1(BigDecimal.valueOf(normalizedParetoSolution.getObjective(0)));
			result.setNormalizedObjective2(BigDecimal.valueOf(normalizedParetoSolution.getObjective(1)));
			result.setNormalizedObjective3(BigDecimal.valueOf(normalizedParetoSolution.getObjective(2)));
			

			results.add(result);
		});
		
		optimizationHelper.saveLogisticsJobResult(results);
	}
	
	private LogisticsJobProblem saveLogisticsJobProblem(Integer noOfSolutions) {
		LogisticsJobProblem problem = new LogisticsJobProblem();
		problem.setJobId(jobId);
		problem.setShipmentDate(helper.getShipmentDate());
		problem.setVehicleType(helper.getVehicleType());
		
		String shipmentList = helper.getShipmentList().stream().map(s -> s.getShipmentKey()).collect(Collectors.toList()).toString();
		String routeList = helper.getRouteList().stream().map(r -> r.getChromosomeId()).collect(Collectors.toList()).toString();
		
		problem.setShipmentList(JavaUtils.removeStringOfList(shipmentList));
		problem.setRouteList(JavaUtils.removeStringOfList(routeList));
		problem.setNoOfSolutions(noOfSolutions);
		
		return optimizationHelper.saveLogisticsJobProblem(problem);
	}
	
	private void saveLogisticsJob() {
		LogisticsJob job = new LogisticsJob();
		job.setJobId(helper.getJobId());
		job.setVehicleConfig(optimizationHelper.getVehicleTypes());
		job.setMaxRun(helper.getMaxRun());
		job.setMaxIteration(helper.getMaxIteration());
		
		optimizationHelper.saveLogisticsJob(job);
	}
	
	private String getSolutionString(IntegerSolution solution) {
		
		List<Integer> list = new ArrayList<Integer>();
		
		for (int i = 0; i < solution.getNumberOfVariables(); i++) {			
			list.add(solution.getVariableValue(i));			
		}
		
		return list.toString();
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
