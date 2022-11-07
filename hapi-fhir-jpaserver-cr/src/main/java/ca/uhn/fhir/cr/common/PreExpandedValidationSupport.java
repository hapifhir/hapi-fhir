package ca.uhn.fhir.cr.common;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.context.support.ValueSetExpansionOptions;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PreExpandedValidationSupport implements IValidationSupport {
	private FhirContext fhirContext;

	public PreExpandedValidationSupport(FhirContext fhirContext) {
		this.fhirContext = fhirContext;
	}

	@Override
	public ValueSetExpansionOutcome expandValueSet(ValidationSupportContext theValidationSupportContext,
			@Nullable ValueSetExpansionOptions theExpansionOptions, @Nonnull IBaseResource theValueSetToExpand) {
		Validate.notNull(theValueSetToExpand, "theValueSetToExpand must not be null or blank");

		if (!getFhirContext().getResourceDefinition("ValueSet").getChildByName("expansion").getAccessor()
				.getValues(theValueSetToExpand).isEmpty()) {
			return new ValueSetExpansionOutcome(theValueSetToExpand);
		} else {
			return IValidationSupport.super.expandValueSet(theValidationSupportContext, theExpansionOptions,
					theValueSetToExpand);
		}
	}

	@Override
	public FhirContext getFhirContext() {
		return this.fhirContext;
	}

}
