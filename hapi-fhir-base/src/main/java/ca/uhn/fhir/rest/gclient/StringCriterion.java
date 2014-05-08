package ca.uhn.fhir.rest.gclient;

class StringCriterion implements ICriterion, ICriterionInternal {

	private String myValue;
	private String myName;

	public StringCriterion(String theName, String theValue) {
		myValue = theValue;
		myName=theName;
	}

	@Override
	public String getParameterName() {
		return myName;
	}

	@Override
	public String getParameterValue() {
		return myValue;
	}

}