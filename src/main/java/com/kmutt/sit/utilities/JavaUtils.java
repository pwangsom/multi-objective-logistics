package com.kmutt.sit.utilities;

import java.util.Random;

public class JavaUtils {

	public static boolean isNull(Object test) {
		if (test instanceof String) {
			return isNull((String) test);
		}

		return test == null;
	}

	public static boolean isNull(String test) {
		return test == null || test.length() == 0 || test.equalsIgnoreCase("null") || test.trim().length() == 0;
	}
	
	public static int getRandomNumberInRange(int min, int max) {
		if (min >= max) {
			throw new IllegalArgumentException("max must be greater than min");
		}

		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
	}
	
	
	public static boolean between(int i, int minValueInclusive, int maxValueInclusive) {
	    return (i >= minValueInclusive && i <= maxValueInclusive);
	}
}
