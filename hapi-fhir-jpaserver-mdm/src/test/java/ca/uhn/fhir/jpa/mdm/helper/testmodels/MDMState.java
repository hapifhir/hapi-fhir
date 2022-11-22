package ca.uhn.fhir.jpa.mdm.helper.testmodels;

import ca.uhn.fhir.jpa.entity.MdmLink;
import org.openjdk.jmh.util.HashMultimap;
import org.openjdk.jmh.util.Multimap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.fail;

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

	/**
	 * The actual outcome links.
	 * Resource (typically a golden resource) -> Link
	 */
	private Multimap<T, MdmLink> myActualOutcomeLinks;

	public Multimap<T, MdmLink> getActualOutcomeLinks() {
		if (myActualOutcomeLinks == null) {
			myActualOutcomeLinks = new HashMultimap<>();
		}
		return myActualOutcomeLinks;
	}

	public MDMState<T> addLinksForResource(T theResource, MdmLink... theLinks) {
		for (MdmLink link : theLinks) {
			getActualOutcomeLinks().put(theResource, link);
		}
		return this;
	}

	public void setActualOutcomeLinks(Multimap<T, MdmLink> theActualOutcomeLinks) {
		myActualOutcomeLinks = theActualOutcomeLinks;
	}

	public Map<String, T> getParameterToValue() {
		if (myParameterToValue == null) {
			myParameterToValue = new HashMap<>();
		}
		return myParameterToValue;
	}

	public T getParameter(String theKey) {
		return getParameterToValue().get(theKey);
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

	public String[] getParsedInputState() {
		return myInputState.split("\n");
	}

	public String[] getParsedOutputState() {
		return myOutputState.split("\n");
	}

	public MDMState<T> setOutputState(String theOutputState) {
		myOutputState = theOutputState;
		return this;
	}

	public static String[] parseState(String theStateString) {
		String[] state = theStateString.split(",");
		if (state.length != 4) {
			// we're using this exclusively in test area
			fail(
				String.format("%s must contain 4 arguments; found %d", theStateString, state.length)
			);
		}
		Arrays.stream(state).map(String::trim).toArray(unused -> state);
		return state;
	}
}
