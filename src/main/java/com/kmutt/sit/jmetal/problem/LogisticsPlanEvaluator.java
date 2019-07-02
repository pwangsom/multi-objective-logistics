package com.kmutt.sit.jmetal.problem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uma.jmetal.solution.IntegerSolution;

import com.kmutt.sit.jmetal.runner.LogisticsNsgaIIIHelper;
import com.kmutt.sit.jpa.entities.DhlRoute;
import com.kmutt.sit.jpa.entities.DhlShipment;

import lombok.Getter;
import lombok.Setter;


public class LogisticsPlanEvaluator {

	private Logger logger = LoggerFactory.getLogger(LogisticsPlanEvaluator.class);
	
	private List<IntegerChromosome> solutionList;
	private List<Integer> vehicleList;
	private Map<Integer, VehicleShipment> vehicleShipmentsMapping;
	
	@Getter
	private Integer noOfCar = 0;
	@Getter
	private Integer utilization = 0;
	@Getter
	private Integer effortScore = 0;
	
	private IntegerSolution solution;
	private LogisticsNsgaIIIHelper helper;
	
	
	public LogisticsPlanEvaluator(IntegerSolution solution, LogisticsNsgaIIIHelper helper) {
		this.solutionList = new ArrayList<IntegerChromosome>();
		this.vehicleList = new ArrayList<Integer>();
		this.vehicleShipmentsMapping = new HashMap<Integer, VehicleShipment>();
		this.solution = solution;
		this.helper = helper;
	}
	
	public void evaluate() {
		converToSolutionList();
		determineNoOfCar();
		mapShipmentsToVehicle();
	}
	
	private void mapShipmentsToVehicle() {
		
		int[] util = {0};
		int[] score = {0};
		
		int[] eachVehicleUtil = {0};
		int[] eachVehicleScore = {0};
		
		vehicleList.stream().forEach(vid -> {
			
			eachVehicleUtil[0] = 0;
			eachVehicleScore[0] = 0;
			
			// Finding route coresponding with chromosome id
			DhlRoute route = helper.getRouteList().stream().filter(r -> r.getChromosomeId() == vid).collect(Collectors.toList()).get(0);
			
			List<IntegerChromosome> findingIndexOfVehicleId = solutionList.stream().filter(item -> item.chromosomeValue == vid).collect(Collectors.toList());
			
			List<DhlShipment> shipmentOfEachVehicleIdList = new ArrayList<DhlShipment>();
			
			findingIndexOfVehicleId.stream().forEach(item -> {
				shipmentOfEachVehicleIdList.add(helper.getShipmentList().get(item.getChromosomeIndex()));
			});

			eachVehicleUtil[0] = determineUtilizationOfEachVehicle(route, shipmentOfEachVehicleIdList.size());
			eachVehicleScore[0] = determineEffortScoreOfEachVehicle(route, shipmentOfEachVehicleIdList);
			
			VehicleShipment vehicleShipment = new VehicleShipment();
			vehicleShipment.setRoute(route);
			vehicleShipment.setShipmentsInVehicle(shipmentOfEachVehicleIdList);
			vehicleShipment.setUtilizationOfVehicle(eachVehicleUtil[0]);
			vehicleShipment.setEffortScoreOfVehicle(eachVehicleScore[0]);
			
			vehicleShipmentsMapping.put(vid, vehicleShipment);
			
			util[0] = util[0] + eachVehicleUtil[0];
			score[0] = score[0] + eachVehicleScore[0];
		});
		
		utilization = util[0];
		effortScore = score[0];
	}
	
	private Integer determineUtilizationOfEachVehicle(DhlRoute route, Integer noOfShipment) {
		Integer util = 0;
		
		Integer minUtil = (int) Math.round(route.getMaxUtilization() * helper.getMinUtilization());
		
		if(noOfShipment < minUtil) {
			util = minUtil - noOfShipment;
		} else if(noOfShipment > route.getMaxUtilization()) {
			util = (route.getMaxUtilization() - noOfShipment) * 2;
		}

		return util;
	}
	
	private Integer determineEffortScoreOfEachVehicle(DhlRoute route, List<DhlShipment> shipmentOfEachVehicleIdList) {
		int[] score = {0};
		
		// Accomulate effort score of each vehicle
		shipmentOfEachVehicleIdList.stream().forEach(shipment -> {
			if(shipment.getAreaCode() != 0) {
				score[0] = score[0] + helper.getScoreMapping().get(shipment.getAreaCode() + "-" + route.getRoute()); 
			} else {
				score[0] = score[0] + helper.getNotfoundScore();
			}
		});
		
		return score[0];
	}
	
	private void determineNoOfCar() {
		noOfCar = vehicleList.size();
	}
	
	private void converToSolutionList() {
		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			IntegerChromosome item = new IntegerChromosome();
			item.setChromosomeIndex(i);
			item.setChromosomeValue(solution.getVariableValue(i));
			
			solutionList.add(item);			
		}
		
		vehicleList = solutionList.stream().map(item -> item.getChromosomeValue()).distinct().sorted().collect(Collectors.toList());
	}
	
	@Getter
	@Setter
	public class VehicleShipment{
		private DhlRoute route = new DhlRoute();
		private List<DhlShipment> shipmentsInVehicle = new ArrayList<DhlShipment>();
		private Integer utilizationOfVehicle = 0;
		private Integer effortScoreOfVehicle = 0;
	}
	
	@Getter
	@Setter
	public class IntegerChromosome{
		private Integer chromosomeIndex;
		private Integer chromosomeValue;
	}
}
