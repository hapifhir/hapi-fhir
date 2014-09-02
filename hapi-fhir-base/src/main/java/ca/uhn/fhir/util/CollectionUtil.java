package ca.uhn.fhir.util;

import java.util.HashSet;
import java.util.Set;

public class CollectionUtil {

	public static <T> Set<T> newSet(T... theValues) {
		HashSet<T> retVal = new HashSet<T>();

		for (T t : theValues) {
			retVal.add(t);
		}
		return retVal;
	}

}
