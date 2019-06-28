package com.kmutt.sit.jemetal.runner;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import com.kmutt.sit.jemetal.problem.DemoIntegerProblem;

public class DemoNsgaIIIIntegerRunner extends AbstractAlgorithmRunner {

	private Logger logger = LoggerFactory.getLogger(DemoNsgaIIIIntegerRunner.class);
	
	private Problem<IntegerSolution> problem;
	private Algorithm<List<IntegerSolution>> algorithm;
	private CrossoverOperator<IntegerSolution> crossover;
	private MutationOperator<IntegerSolution> mutation;
	private SelectionOperator<List<IntegerSolution>, IntegerSolution> selection;
	private AlgorithmRunner algorithmRunner;
	
	private int maxIteration = 300;
	
	public DemoNsgaIIIIntegerRunner(int maxIteration) {
		this();
		this.maxIteration = maxIteration;
	}
	
	public DemoNsgaIIIIntegerRunner() {
		
		problem = new DemoIntegerProblem();
		
	    double crossoverProbability = 0.9 ;
	    double crossoverDistributionIndex = 30.0 ;
		crossover = new IntegerSBXCrossover(crossoverProbability, crossoverDistributionIndex);

	    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
	    double mutationDistributionIndex = 20.0 ;
		mutation = new IntegerPolynomialMutation(mutationProbability, mutationDistributionIndex);

		selection = new BinaryTournamentSelection<IntegerSolution>();		
	}
	
	public void execute() {
		
	    algorithm = new NSGAIIIBuilder<>(problem)
	            .setCrossoverOperator(crossover)
	            .setMutationOperator(mutation)
	            .setSelectionOperator(selection)
	            .setMaxIterations(this.maxIteration)
	            .build() ;

	    algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
	    
	    List<IntegerSolution> population = algorithm.getResult();
	    long computingTime = algorithmRunner.getComputingTime();
	    
	    new SolutionListOutput(population)
	        .setSeparator("\t")
	        .setVarFileOutputContext(new DefaultFileOutputContext("VAR.tsv"))
	        .setFunFileOutputContext(new DefaultFileOutputContext("FUN.tsv"))
	        .print() ;

		logger.info("Total execution time: " + computingTime + "ms");
		logger.info("Objectives values have been written to file FUN.tsv");
		logger.info("Variables values have been written to file VAR.tsv");	
		
	}

}
