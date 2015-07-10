package ca.uhn.fhir.validation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum ResultSeverityEnum {

	/**
	 * The issue has no relation to the degree of success of the action
	 */
	INFORMATION("information"),
	
	/**
	 * The issue is not important enough to cause the action to fail, but may cause it to be performed suboptimally or in a way that is not as desired
	 */
	WARNING("warning"),
	
	/**
	 * The issue is sufficiently important to cause the action to fail
	 */
	ERROR("error"),

	/**
	 * The issue caused the action to fail, and no further checking could be performed
	 */
	FATAL("fatal");

	private static Map<String, ResultSeverityEnum> ourValues;
	private String myCode;

	private ResultSeverityEnum(String theCode) {
		myCode = theCode;
	}

	public String getCode() {
		return myCode;
	}

	public static ResultSeverityEnum fromCode(String theCode) {
		if (ourValues == null) {
			HashMap<String, ResultSeverityEnum> values = new HashMap<String, ResultSeverityEnum>();
			for (ResultSeverityEnum next : values()) {
				values.put(next.getCode(), next);
			}
			ourValues = Collections.unmodifiableMap(values);
		}
		return ourValues.get(theCode);
	}

}
