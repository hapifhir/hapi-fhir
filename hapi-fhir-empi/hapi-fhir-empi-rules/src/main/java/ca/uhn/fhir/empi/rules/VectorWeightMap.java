package ca.uhn.fhir.empi.rules;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class VectorWeightMap {
	private final EmpiRulesJson myEmpiRulesJson;
	private Map<Long, Double> myMap = new HashMap<>();

	VectorWeightMap(EmpiRulesJson theEmpiRulesJson) {
		myEmpiRulesJson = theEmpiRulesJson;
		initMap();
	}

	private void initMap() {
		for (Map.Entry<String, Double> entry : myEmpiRulesJson.getWeightMap().entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	public Double get(Long theMatchVector) {
		return myMap.get(theMatchVector);
	}

	public void put(String theFieldMatchNames, double theWeight) {
		long vector = getVector(theFieldMatchNames);
		myMap.put(vector, theWeight);
	}

	public long getVector(String theFieldMatchNames) {
		long retval = 0;
		for (String fieldMatchName : splitFieldMatchNames(theFieldMatchNames)) {
			int index = getFieldMatchIndex(fieldMatchName);
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

	private int getFieldMatchIndex(final String theFieldMatchName) {
		for (int i = 0; i < myEmpiRulesJson.size(); ++i) {
			if (myEmpiRulesJson.get(i).getName().equals(theFieldMatchName)) {
				return i;
			}
		}
		return -1;
	}
}
