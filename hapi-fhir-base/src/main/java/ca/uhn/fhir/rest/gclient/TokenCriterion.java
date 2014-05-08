package ca.uhn.fhir.rest.gclient;

import org.apache.commons.lang3.StringUtils;

class TokenCriterion implements ICriterion, ICriterionInternal {

	private String myValue;
	private String myName;

	public TokenCriterion(String theName, String theSystem, String theCode) {
		myName = theName;
		if (StringUtils.isNotBlank(theSystem)) {
			myValue = theSystem + "|" + StringUtils.defaultString(theCode);
		} else {
			myValue = StringUtils.defaultString(theCode);
		}
	}

	@Override
	public String getParameterValue() {
		return myValue;
	}

	@Override
	public String getParameterName() {
		return myName;
	}

}