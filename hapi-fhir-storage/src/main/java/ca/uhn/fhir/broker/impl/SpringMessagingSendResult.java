package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.ISendResult;

public class SpringMessagingSendResult implements ISendResult {
	private final boolean mySuccessful;

	public SpringMessagingSendResult(boolean theSuccessful) {
		mySuccessful = theSuccessful;
	}

	public boolean isSuccessful() {
		return mySuccessful;
	}
}
