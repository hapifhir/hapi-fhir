package ca.uhn.fhir.jpa.mdm.helper.testmodels;

import ca.uhn.fhir.jpa.entity.MdmLink;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.testcontainers.shaded.com.google.common.collect.HashMultimap;
import org.testcontainers.shaded.com.google.common.collect.Multimap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MDMState<T, P extends IResourcePersistentId> {
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
	 * A list of link expressions for input state.
	 */
	private List<MdmTestLinkExpression> myInputLinks;

	/**
	 * Output state to verify at end of test.
	 * Same format as input
	 */
	private String myOutputState;

	/**
	 * A list of link expressions for output state.
	 */
	private List<MdmTestLinkExpression> myOutputLinks;

	/**
	 * The actual outcome links.
	 * Resource (typically a golden resource) -> Link
	 */
	private Multimap<T, MdmLink> myActualOutcomeLinks;

	/**
	 * Map of forcedId -> resource persistent id for each resource created
	 */
	private final Map<String, P> myForcedIdToPID = new HashMap<>();

	public void addPID(String theForcedId, P thePid) {
		assert !myForcedIdToPID.containsKey(theForcedId);
		myForcedIdToPID.put(theForcedId, thePid);
	}

	public P getPID(String theForcedId) {
		return myForcedIdToPID.get(theForcedId);
	}

	public Multimap<T, MdmLink> getActualOutcomeLinks() {
		if (myActualOutcomeLinks == null) {
			myActualOutcomeLinks = HashMultimap.create();
		}
		return myActualOutcomeLinks;
	}

	public MDMState<T, P> addLinksForResource(T theResource, MdmLink... theLinks) {
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

	public MDMState<T, P> addParameter(String myParamKey, T myValue) {
		getParameterToValue().put(myParamKey, myValue);
		return this;
	}

	public void setParameterToValue(Map<String, T> theParameterToValue) {
		myParameterToValue = theParameterToValue;
	}

	public String getInputState() {
		return myInputState;
	}

	public MDMState<T, P> setInputState(String theInputState) {
		myInputState = theInputState;
		return this;
	}

	public String getOutputState() {
		return myOutputState;
	}

	public List<MdmTestLinkExpression> getParsedInputState() {
		if (myInputLinks == null) {
			myInputLinks = new ArrayList<>();

			String[] inputState = myInputState.split("\n");
			for (String is : inputState) {
				myInputLinks.add(new MdmTestLinkExpression(is));
			}
		}
		return myInputLinks;
	}

	public List<MdmTestLinkExpression> getParsedOutputState() {
		if (myOutputLinks == null) {
			myOutputLinks = new ArrayList<>();
			String[] states = myOutputState.split("\n");
			for (String os : states) {
				myOutputLinks.add(new MdmTestLinkExpression(os));
			}
		}
		return myOutputLinks;
	}

	public MDMState<T, P> setOutputState(String theOutputState) {
		myOutputState = theOutputState;
		return this;
	}


}
