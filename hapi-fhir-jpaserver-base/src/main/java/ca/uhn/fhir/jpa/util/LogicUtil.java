package ca.uhn.fhir.jpa.util;

public class LogicUtil {

	public static boolean multiXor(boolean... theValues) {
		int count = 0;
		for (int i = 0; i < theValues.length; i++) {
			if (theValues[i]) {
				count++;
			}
		}
		return count == 1;
	}

}
