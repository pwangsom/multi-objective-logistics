package com.kmutt.sit.jmetal.problem;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uma.jmetal.problem.impl.AbstractIntegerProblem;
import org.uma.jmetal.solution.IntegerSolution;

import com.kmutt.sit.utilities.JavaUtils;

public class DemoIntegerProblem extends AbstractIntegerProblem {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger logger = LoggerFactory.getLogger(DemoIntegerProblem.class);
	
	final private int NO_OBJECTIVES = 3;
	
	public DemoIntegerProblem() {
		
		// Length of chromosome is equal to a number of shipments
		setNumberOfVariables(20);

		List<Integer> lowerLimit = new ArrayList<>(getNumberOfVariables());
		List<Integer> upperLimit = new ArrayList<>(getNumberOfVariables());
		
		// Values in chromosome can be ranging from 0 to number of routes - 1
		int lower = 0;
		int upper = 10 - 1;

		for (int i = 0; i < getNumberOfVariables(); i++) {
			lowerLimit.add(lower);
			upperLimit.add(upper);
		}

		setLowerLimit(lowerLimit);
		setUpperLimit(upperLimit);
		
	}

	@Override
	public void evaluate(IntegerSolution solution) {
		// TODO Auto-generated method stub
		logger.debug("");
		logger.debug("Start: Evaluate");
		
		solution.setObjective(0, JavaUtils.getRandomNumberInRange(solution.getLowerBound(0), solution.getUpperBound(0)));
		solution.setObjective(1, JavaUtils.getRandomNumberInRange(solution.getLowerBound(0), solution.getUpperBound(0)));
		solution.setObjective(2, JavaUtils.getRandomNumberInRange(solution.getLowerBound(0), solution.getUpperBound(0)));
		
		logger.debug("");
		logger.debug("Finished: Evaluate");
	}

}
