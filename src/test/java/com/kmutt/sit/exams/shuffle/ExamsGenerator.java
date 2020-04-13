package com.kmutt.sit.exams.shuffle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kmutt.sit.utilities.JavaUtils;

public class ExamsGenerator {
	
	private static Logger logger = LoggerFactory.getLogger(ExamsGenerator.class);
	
	private List<List<Integer>> examColCopyRow = new ArrayList<List<Integer>>();
	private List<List<Integer>> copyColExamRow = new ArrayList<List<Integer>>();
	private Integer noOfCopies;
	private Integer noOfExams;
	
	public ExamsGenerator(Integer noOfStudents, Integer noOfExams) {
		this.noOfCopies = noOfStudents;
		this.noOfExams = noOfExams;
	}
	
	public List<List<Integer>> generate(){				
		initialFirstCopy();
		generateRemainingCopies();
		
		return copyColExamRow;
	}
	
	private void generateRemainingCopies() {
		
		for(int i = 0; i < noOfExams; i++) {			
			List<Integer> copyColTemp = new ArrayList<Integer>();
			copyColTemp.add(examColCopyRow.get(0).get(i));
			copyColExamRow.add(copyColTemp);			
		}
		
		int max_tries = 10;
		
		for(int i = 1; i < noOfCopies; i++) {
			
			List<Integer> examColTemp = new ArrayList<Integer>();
			logger.info("Processing copy -> " + i);
			
			for(int j = 0; j < noOfExams; j++) {
				
				Integer rand = 0;
				int tries = 0;
				
				do {
					
					rand = JavaUtils.getRandomNumberInRange(1, noOfExams);
					tries++;
					
				} while((copyColExamRow.get(j).contains(rand) || examColTemp.contains(rand)) && tries < max_tries);
				
				if(tries == max_tries) {				
					
					rand = 1;
					
					while(examColTemp.contains(rand) && rand <= noOfExams){
						rand++;
					}
				}
								 
				examColTemp.add(rand);
				copyColExamRow.get(j).add(rand);
							
				logger.info("Gen exams for " + j + " th is -> " + rand + "; tries -> " + tries);
			}
			
			examColCopyRow.add(examColTemp);
			
		}
		
	}
	
	private void initialFirstCopy() {
		List<Integer> first = Arrays.asList(new Integer[noOfExams]);
		
		int[] c = {1};
		
		IntStream.range(0, noOfExams).forEach(i ->{
			first.set(i, c[0]);
			c[0]++;
		});
		
		examColCopyRow.add(first);
		
		logger.info(first.toString());
	}

}
