package ca.uhn.fhir.empi.rules;

import javax.annotation.Nonnull;

public class MatchFieldVectorHelper {

	private MatchFieldVectorHelper() {
	}

	public static long getVector(EmpiRulesJson theEmpiRulesJson, String theFieldMatchNames) {
		long retval = 0;
		for (String fieldMatchName : splitFieldMatchNames(theFieldMatchNames)) {
			int index = getFieldMatchIndex(theEmpiRulesJson ,fieldMatchName);
			if (index == -1) {
				throw new IllegalArgumentException("There is no matchField with name " + fieldMatchName);
			}
			retval |= (1 << index);
		}
		return retval;
	}

	@Nonnull
	static String[] splitFieldMatchNames(String theFieldMatchNames) {
		return theFieldMatchNames.split(",\\s*");
	}

	static private int getFieldMatchIndex(EmpiRulesJson theEmpiRulesJson, final String theFieldMatchName) {
		for (int i = 0; i < theEmpiRulesJson.size(); ++i) {
			if (theEmpiRulesJson.get(i).getName().equals(theFieldMatchName)) {
				return i;
			}
		}
		return -1;
	}
}
