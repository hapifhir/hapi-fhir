package ca.uhn.fhir.jpa.mdm.testmodels;

import java.util.HashMap;
import java.util.Map;

public class MDMState<T> {
	/**
	 * Collection of Parameter keys -> values to use
	 *
	 * Eg:
	 * P1 -> (some patient resource)
	 * PG -> (some golden patient resource)
	 */
	private Map<String, T> myParameterToValue;

	/**
	 * Initial state for test.
	 * Comma separated lines with:
	 * Left param value, MdmLinkSourceEnum value, MdmMatchResultEnum value, Right param value
	 *
	 * Each input line represents a link state
	 *
	 * Eg:
	 * PG1, MANUAL, MATCH, P1
	 * PG1, AUTO, POSSIBLE_MATCH, P2
	 */
	private String myInputState;

	/**
	 * Output state to verify at end of test.
	 * Same format as input
	 */
	private String myOutputState;

	public Map<String, T> getParameterToValue() {
		if (myParameterToValue == null) {
			myParameterToValue = new HashMap<>();
		}
		return myParameterToValue;
	}

	public MDMState<T> addParameter(String myParamKey, T myValue) {
		getParameterToValue().put(myParamKey, myValue);
		return this;
	}

	public void setParameterToValue(Map<String, T> theParameterToValue) {
		myParameterToValue = theParameterToValue;
	}

	public String getInputState() {
		return myInputState;
	}

	public MDMState<T> setInputState(String theInputState) {
		myInputState = theInputState;
		return this;
	}

	public String getOutputState() {
		return myOutputState;
	}

	public MDMState<T> setOutputState(String theOutputState) {
		myOutputState = theOutputState;
		return this;
	}
}
