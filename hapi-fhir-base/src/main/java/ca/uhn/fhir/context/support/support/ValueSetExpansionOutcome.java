package ca.uhn.fhir.context.support.support;

import org.hl7.fhir.instance.model.api.IBaseResource;

public class ValueSetExpansionOutcome {

	private final IBaseResource myValueSet;
	private final String myError;

	public ValueSetExpansionOutcome(IBaseResource theValueSet, String theError) {
		myValueSet = theValueSet;
		myError = theError;
	}

	public ValueSetExpansionOutcome(IBaseResource theValueSet) {
		myValueSet = theValueSet;
		myError = null;
	}

	public String getError() {
		return myError;
	}

	public IBaseResource getValueSet() {
		return myValueSet;
	}
}
