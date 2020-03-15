package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import org.apache.commons.lang3.Validate;

public abstract class BaseValidationSupport implements IValidationSupport {
	protected final FhirContext myCtx;

	/**
	 * Constructor
	 */
	public BaseValidationSupport(FhirContext theFhirContext) {
		Validate.notNull(theFhirContext, "theFhirContext must not be null");
		myCtx = theFhirContext;
	}

	@Override
	public FhirContext getFhirContext() {
		return myCtx;
	}
}
