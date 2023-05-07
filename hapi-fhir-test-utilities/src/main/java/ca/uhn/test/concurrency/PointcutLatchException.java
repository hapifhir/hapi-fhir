package ca.uhn.test.concurrency;

import ca.uhn.fhir.interceptor.api.HookParams;

class PointcutLatchException extends IllegalStateException {
	private static final long serialVersionUID = 1372636272233536829L;

	PointcutLatchException(String theMessage, String theName, HookParams theArgs) {
		super(theName + ": " + theMessage + " called with values: " + PointcutLatchSession.hookParamsToString(theArgs));
	}

	public PointcutLatchException(String theMessage, String theName) {
		super(theName + ": " + theMessage);
	}
}
