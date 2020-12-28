package ca.uhn.fhir.jpa.interceptor.validation;

import ca.uhn.fhir.context.FhirContext;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;

abstract class BaseTypedRule implements IRepositoryValidatingRule {

	private final String myResourceType;
	private final FhirContext myFhirContext;

	protected BaseTypedRule(FhirContext theFhirContext, String theResourceType) {
		Validate.notNull(theFhirContext);
		Validate.notBlank(theResourceType);
		myFhirContext = theFhirContext;
		myResourceType = theResourceType;
	}

	@Nonnull
	@Override
	public String getResourceType() {
		return myResourceType;
	}

	protected FhirContext getFhirContext() {
		return myFhirContext;
	}
}
