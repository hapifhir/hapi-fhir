package ca.uhn.fhir.empi.rules;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class EmpiRulesJson implements IModelJson, Iterable<EmpiFieldMatchJson> {
	@JsonProperty("test")
	String myTest = "test";
	@JsonProperty("matchFields")
	List<EmpiFieldMatchJson> myMatchFieldJsonList = new ArrayList<>();
	@JsonProperty("weightMap")
	Map<Set<String>, Double> myWeightMap = new HashMap<>();

	transient Map<Long, Double> myVectorWeights = new HashMap<>();

	public void addMatchField(EmpiFieldMatchJson theMatchRuleName) {
		myMatchFieldJsonList.add(theMatchRuleName);
	}

	public int size() {
		return myMatchFieldJsonList.size();
	}

	public EmpiFieldMatchJson get(int theIndex) {
		return myMatchFieldJsonList.get(theIndex);
	}

	@NotNull
	@Override
	public Iterator<EmpiFieldMatchJson> iterator() {
		return myMatchFieldJsonList.iterator();
	}

	@Override
	public void forEach(Consumer<? super EmpiFieldMatchJson> action) {
		myMatchFieldJsonList.forEach(action);
	}

	@Override
	public Spliterator<EmpiFieldMatchJson> spliterator() {
		return myMatchFieldJsonList.spliterator();
	}

	public double getWeight(Long theMatchVector) {
		Double result = myVectorWeights.get(theMatchVector);
		return MoreObjects.firstNonNull(result, 0.0);
	}

	public void putWeight(Set<String> theFieldMatchNames, double theWeight) {
		myWeightMap.put(theFieldMatchNames, theWeight);

		long vector = getVector(theFieldMatchNames);
		myVectorWeights.put(vector, theWeight);
	}

	private long getVector(Set<String> theFieldMatchNames) {
		long retval = 0;
		for (String fieldMatchName : theFieldMatchNames) {
			int index = getFieldMatchIndex(fieldMatchName);
			if (index == -1) {
				// FIXME KHS test
				throw new IllegalArgumentException("There is no matchField with name " + fieldMatchName);
			}
			retval |= (1 << index);
		}
		return retval;
	}

	private int getFieldMatchIndex(final String theFieldMatchName) {
		for (int i = 0; i < myMatchFieldJsonList.size(); ++i) {
			if (myMatchFieldJsonList.get(i).getName().equals(theFieldMatchName)) {
				return i;
			}
		}
		return -1;
	}
}
