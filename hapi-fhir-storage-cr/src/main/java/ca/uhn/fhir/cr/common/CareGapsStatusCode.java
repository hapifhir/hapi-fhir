package ca.uhn.fhir.cr.common;

public enum CareGapsStatusCode {
	OPEN_GAP("open-gap"), CLOSED_GAP("closed-gap"), NOT_APPLICABLE("not-applicable");

	private final String myValue;

	CareGapsStatusCode(final String theValue) {
		myValue = theValue;
	}

	@Override
	public String toString() {
		return myValue;
	}

	public String toDisplayString() {
		if (myValue.equals("open-gap")) {
			return "Open Gap";
		}

		if (myValue.equals("closed-gap")) {
			return "Closed Gap";
		}

		if (myValue.equals("not-applicable")) {
			return "Not Applicable";
		}

		throw new IllegalArgumentException();
	}
}
