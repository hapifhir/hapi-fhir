package ca.uhn.fhir.empi.rules;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

public class EmpiRulesJson implements IModelJson, Iterable<EmpiFieldMatchJson> {
	@JsonProperty("matchFields")
	List<EmpiFieldMatchJson> myMatchFieldJsonList = new ArrayList<>();
	@JsonProperty("weightMap")
	Map<String, Double> myWeightMap = new HashMap<>();

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

	public double getWeight(Long theMatchVector) {
		Double result = myVectorWeights.get(theMatchVector);
		return MoreObjects.firstNonNull(result, 0.0);
	}

	public void putWeight(String theFieldMatchNames, double theWeight) {
		myWeightMap.put(theFieldMatchNames, theWeight);

		long vector = MatchFieldVectorHelper.getVector(this, theFieldMatchNames);
		myVectorWeights.put(vector, theWeight);
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
}
