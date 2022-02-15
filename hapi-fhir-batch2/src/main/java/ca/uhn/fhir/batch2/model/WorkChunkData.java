package ca.uhn.fhir.batch2.model;

import org.apache.commons.lang3.Validate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WorkChunkData {

	private Map<String, Object> myData;

	/**
	 * Constructor
	 */
	public WorkChunkData() {
		this(new HashMap<>());
	}

	/**
	 * Constructor
	 */
	public WorkChunkData(Map<String, Object> theData) {
		myData = theData;
	}

	public Map<String, Object> asMap() {
		return myData;
	}

	public void put(String theKey, String theValue) {
		Validate.notNull(theKey);
		myData.put(theKey, theValue);
	}

	public static WorkChunkData withData(String theKey, Object theValue) {
		return new WorkChunkData(Collections.singletonMap(theKey, theValue));
	}
}
