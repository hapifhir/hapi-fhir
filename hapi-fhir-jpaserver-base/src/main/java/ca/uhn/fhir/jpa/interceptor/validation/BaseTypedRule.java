package ca.uhn.fhir.jpa.interceptor.validation;

import org.apache.commons.lang3.Validate;

abstract class BaseTypedRule {

	private final String myResourceType;

	protected BaseTypedRule(String theResourceType) {
		Validate.notBlank(theResourceType);
		myResourceType = theResourceType;
	}
}
