package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This class provides an implementation of IValidationSupport
 * interface which is used for validation of terminology services.
 */
public class PreExpandedValidationSupport implements IValidationSupport {
	private final FhirContext myFhirContext;

	public PreExpandedValidationSupport(FhirContext theFhirContext) {
		this.myFhirContext = theFhirContext;
	}

	@Override
	public ValueSetExpansionOutcome expandValueSet(
			ValidationSupportContext theValidationSupportContext,
			@Nullable ValueSetExpansionOptions theExpansionOptions,
			@Nonnull IBaseResource theValueSetToExpand) {
		Validate.notNull(theValueSetToExpand, "theValueSetToExpand must not be null or blank");

		if (!getFhirContext()
				.getResourceDefinition("ValueSet")
				.getChildByName("expansion")
				.getAccessor()
				.getValues(theValueSetToExpand)
				.isEmpty()) {
			return new ValueSetExpansionOutcome(theValueSetToExpand);
		} else {
			return IValidationSupport.super.expandValueSet(
					theValidationSupportContext, theExpansionOptions, theValueSetToExpand);
		}
	}

	@Override
	public FhirContext getFhirContext() {
		return this.myFhirContext;
	}
}
