package ca.uhn.fhir.mdm.model;

import ca.uhn.fhir.mdm.api.MdmConstants;

/**
 * An enum to determine matches
 */
public enum MdmMatchAbortReason {
	TOO_MANY_CANDIDATES(MdmConstants.TOO_MANY_CANDIDATES),
	BLOCKED(MdmConstants.BLOCKED_VALUE);

	private final String myCodeToUse;

	MdmMatchAbortReason(String theCode) {
		myCodeToUse = theCode;
	}

	public String getCode() {
		return myCodeToUse;
	}
}
