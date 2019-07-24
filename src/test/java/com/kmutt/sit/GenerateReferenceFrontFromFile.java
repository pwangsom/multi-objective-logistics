package com.kmutt.sit;

import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.uma.jmetal.problem.impl.AbstractDoubleProblem;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.impl.DefaultDoubleSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchive;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

public class GenerateReferenceFrontFromFile {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		String inputFileName = "D:\\Users\\pwangsom\\git\\multi-objective-logistics\\files\\archived_solution\\input";
		String outputFileName = "D:\\Users\\pwangsom\\git\\multi-objective-logistics\\files\\archived_solution\\output\\OUT.dat";

		NonDominatedSolutionListArchive<DoubleSolution> archive = null;

		if (Files.isRegularFile(Paths.get(inputFileName))) {
			archive = readDataFromFile(inputFileName);

		} else if (Files.isDirectory(Paths.get(inputFileName))) {

			List<String> fileNameList = Files.list(Paths.get(inputFileName)).map(s -> s.toString()).collect(toList());

			archive = new NonDominatedSolutionListArchive<>();
			for (String fileName : fileNameList) {
				System.out.println(fileName);
				archive.join(readDataFromFile(fileName));
			}
		} else {
			throw new JMetalException("Error opening file/directory");
		}

		new SolutionListOutput(archive.getSolutionList()).setSeparator("\t")
				.setFunFileOutputContext(new DefaultFileOutputContext(outputFileName)).print();

	}

	@SuppressWarnings("resource")
	private static NonDominatedSolutionListArchive<DoubleSolution> readDataFromFile(String inputFileName) {
		Stream<String> lines;

		try {
			lines = Files.lines(Paths.get(inputFileName), Charset.defaultCharset());
		} catch (IOException e) {
			throw new JMetalException(e);
		}

		List<List<Double>> numbers = lines.map(line -> {
			String[] textNumbers = line.split(" ");
			List<Double> listOfNumbers = new ArrayList<>();
			for (String number : textNumbers) {
				listOfNumbers.add(Double.parseDouble(number));
			}

			return listOfNumbers;
		}).collect(toList());

		int numberOfObjectives = numbers.get(0).size();
		DummyProblem dummyProblem = new DummyProblem(numberOfObjectives);

		NonDominatedSolutionListArchive<DoubleSolution> archive;
		archive = new NonDominatedSolutionListArchive<>();

		numbers.stream().forEach(list -> {
			DoubleSolution solution = new DefaultDoubleSolution(dummyProblem);
			for (int i = 0; i < numberOfObjectives; i++) {
				solution.setObjective(i, list.get(i));
			}
			archive.add(solution);
		});

		return archive;
	}

	private static class DummyProblem extends AbstractDoubleProblem {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public DummyProblem(int numberOfObjectives) {
			this.setNumberOfObjectives(numberOfObjectives);
		}

		@Override
		public void evaluate(DoubleSolution solution) {

		}
	}
}
