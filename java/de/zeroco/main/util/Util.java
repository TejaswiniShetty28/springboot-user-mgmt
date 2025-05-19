package de.zeroco.main.util;

import java.util.Collection;

public class Util {
	
	public static boolean isBlank(Object input) {
		if (input == null) {
			return true;
		} else if (input.getClass().isArray()) {
			return java.lang.reflect.Array.getLength(input) == 0;
		} else if (input instanceof Collection<?>) {
			return ((Collection<?>) input).isEmpty();
		} else {
			return input.toString().trim().isEmpty();
		}
	}
	
	public static boolean hasData(Object input) {
		return !isBlank(input);
	}

}
