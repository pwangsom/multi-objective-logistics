package com.kmutt.sit.exams.shuffle;

import java.util.ArrayList;
import java.util.List;

public class Shuffle {

	public static void main(String[] args) {
		// TODO Auto-generated method stub		
		
		Integer noOfStudents = 12;
		Integer noOfExams = 50;
		
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		
		ExamsGenerator gen = new ExamsGenerator(noOfStudents, noOfExams);
		result = gen.generate();

		System.out.println("");
		System.out.println("Final result =============");
		
		for(int i = 0; i < result.size(); i++) {
			System.out.println(result.get(i).toString());
		}
	}
}
