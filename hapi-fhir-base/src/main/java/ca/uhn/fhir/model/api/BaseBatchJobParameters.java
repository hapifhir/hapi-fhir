package ca.uhn.fhir.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class BaseBatchJobParameters implements IModelJson {
	/**
	 * A serializable map of key-value pairs that can be
	 * added to any extending job.
	 */
	@JsonProperty("additionalData")
	private Map<String, Object> myData;

	public Map<String, Object> getAdditionalData() {
		if (myData == null) {
			myData = new HashMap<>();
		}
		return myData;
	}

	public void setAdditionalData(String theKey, Object theValue) {
		getAdditionalData().put(theKey, theValue);
	}
}
