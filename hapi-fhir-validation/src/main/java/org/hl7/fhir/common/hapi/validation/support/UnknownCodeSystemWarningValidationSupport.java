package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import org.hl7.fhir.exceptions.TerminologyServiceException;
import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * This validation support module may be placed at the end of a {@link ValidationSupportChain}
 * in order to configure the validator to generate a warning if a resource being validated
 * contains an unknown code system.
 *
 * Note that this module must also be activated by calling {@link #setAllowNonExistentCodeSystem(boolean)}
 * in order to specify that unknown code systems should be allowed.
 */
public class UnknownCodeSystemWarningValidationSupport extends BaseValidationSupport {
	public static final boolean ALLOW_NON_EXISTENT_CODE_SYSTEM_DEFAULT = false;

	private boolean myAllowNonExistentCodeSystem = ALLOW_NON_EXISTENT_CODE_SYSTEM_DEFAULT;

	/**
	 * Constructor
	 */
	public UnknownCodeSystemWarningValidationSupport(FhirContext theFhirContext) {
		super(theFhirContext);
	}

	@Override
	public boolean isValueSetSupported(ValidationSupportContext theValidationSupportContext, String theValueSetUrl) {
		return true;
	}

	@Nullable
	@Override
	public CodeValidationResult validateCodeInValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		if (theCodeSystem == null) {
			return null;
		}
		IBaseResource codeSystem = theValidationSupportContext.getRootValidationSupport().fetchCodeSystem(theCodeSystem);
		if (codeSystem != null) {
			return null;
		}

		String message = "Unknown code system: " + theCodeSystem;
		if (!myAllowNonExistentCodeSystem) {
			return new CodeValidationResult()
				.setSeverity(IssueSeverity.ERROR)
				.setMessage(message);
		}

		throw new TerminologyServiceException(message);
	}

	public void setAllowNonExistentCodeSystem(boolean theAllowNonExistentCodeSystem) {
		myAllowNonExistentCodeSystem = theAllowNonExistentCodeSystem;
	}
}
