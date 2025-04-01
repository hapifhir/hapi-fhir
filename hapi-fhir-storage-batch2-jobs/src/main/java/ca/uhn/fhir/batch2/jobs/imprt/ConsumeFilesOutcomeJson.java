package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.model.api.StorageResponseCodeEnum;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConsumeFilesOutcomeJson implements IModelJson {

	@JsonProperty("sourceName")
	private String mySourceName;

	@JsonProperty("outcomeCount")
	private Map<StorageResponseCodeEnum, Integer> myOutcomeCount;

	@JsonProperty("errors")
	private List<String> myErrors;

	public String getSourceName() {
		return mySourceName;
	}

	public void setSourceName(String theSourceName) {
		mySourceName = theSourceName;
	}

	public Map<StorageResponseCodeEnum, Integer> getOutcomeCount() {
		if (myOutcomeCount == null) {
			myOutcomeCount = new HashMap<>();
		}
		return myOutcomeCount;
	}

	public List<String> getErrors() {
		if (myErrors == null) {
			myErrors = new ArrayList<>();
		}
		return myErrors;
	}

	public void addOutcome(StorageResponseCodeEnum theStorageResponseCode) {
		addOutcome(theStorageResponseCode, 1);
	}

	public void addOutcome(StorageResponseCodeEnum theStorageResponseCode, int theDelta) {
		Map<StorageResponseCodeEnum, Integer> outcomeCount = getOutcomeCount();
		Integer existing = outcomeCount.getOrDefault(theStorageResponseCode, 0);
		outcomeCount.put(theStorageResponseCode, existing + theDelta);
	}

	public void addError(String theError) {
		getErrors().add(theError);
	}

	/**
	 * Adds the given data into this instance
	 *
	 * @param theData The data to add
	 */
	public void add(ConsumeFilesOutcomeJson theData) {
		for (String error : theData.getErrors()) {
			addError(error);
		}
		for (Map.Entry<StorageResponseCodeEnum, Integer> outcomeCount : theData.getOutcomeCount().entrySet()) {
			addOutcome(outcomeCount.getKey(), outcomeCount.getValue());
		}
	}

	public boolean hasOutcomes() {
		return !getOutcomeCount().isEmpty();
	}

	public boolean hasErrors() {
		return !getErrors().isEmpty();
	}
}
