package com.kmutt.sit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DummyTest {
	

	private static Logger logger = LoggerFactory.getLogger(DummyTest.class);
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub		
		
		List<Integer> list = Arrays.asList(7, 18, 8, 1, 1, 2, 3, 3, 4, 5, 5, 18, 8);
		List<Integer> uniqueList = new ArrayList<Integer>();
		
		Integer i = Integer.valueOf(String.valueOf(list.stream().distinct().count()));
		
		logger.info(String.format("Count: %d", i));
		
		uniqueList = list.stream().distinct().sorted().collect(Collectors.toList());
		
		logger.info(uniqueList.toString());
		
		Integer max = 20;
		Double minRate = 0.826;		
		logger.info("max * minRate:= " + max * minRate);
		
		Integer minUtil = (int) Math.round(max * minRate);
		logger.info("minUtil:= " + minUtil);
	}
	
}
