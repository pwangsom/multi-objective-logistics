package com.kmutt.sit.jemetal.runner;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.util.AbstractAlgorithmRunner;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

public class DemoNsgaIIIRunner extends AbstractAlgorithmRunner {
	

	private Logger logger = LoggerFactory.getLogger(DemoNsgaIIIRunner.class);
	
    private Problem<DoubleSolution> problem;
    private Algorithm<List<DoubleSolution>> algorithm;
    private CrossoverOperator<DoubleSolution> crossover;
    private MutationOperator<DoubleSolution> mutation;
    private SelectionOperator<List<DoubleSolution>, DoubleSolution> selection;
	private AlgorithmRunner algorithmRunner;
	
	private String problemName = "org.uma.jmetal.problem.multiobjective.dtlz.DTLZ1" ;
	
	public void setup() {
		
		problem = ProblemUtils.loadProblem(problemName);

	    double crossoverProbability = 0.9 ;
	    double crossoverDistributionIndex = 30.0 ;
		crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex);

	    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
	    double mutationDistributionIndex = 20.0 ;
		mutation = new PolynomialMutation(mutationProbability, mutationDistributionIndex);

		selection = new BinaryTournamentSelection<DoubleSolution>();
		
	}
	
	public void execute() {
		
	    algorithm = new NSGAIIIBuilder<>(problem)
	            .setCrossoverOperator(crossover)
	            .setMutationOperator(mutation)
	            .setSelectionOperator(selection)
	            .setMaxIterations(100)
	            .build() ;

	    algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
	    
	    List<DoubleSolution> population = algorithm.getResult();
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
