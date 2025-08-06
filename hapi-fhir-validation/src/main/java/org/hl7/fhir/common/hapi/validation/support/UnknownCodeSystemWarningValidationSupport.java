package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.util.Logs;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;

/**
 * This validation support module may be placed at the end of a {@link ValidationSupportChain}
 * in order to configure the validator to generate a warning or an error if a resource being validated
 * contains an unknown code system.
 *
 * Note that this module must also be activated by calling {@link #setAllowNonExistentCodeSystem(boolean)}
 * in order to specify that unknown code systems should be allowed.
 */
public class UnknownCodeSystemWarningValidationSupport extends BaseValidationSupport {
	private static final Logger ourLog = Logs.getTerminologyTroubleshootingLog();

	public static final IssueSeverity DEFAULT_SEVERITY = IssueSeverity.ERROR;

	private IssueSeverity myNonExistentCodeSystemSeverity = DEFAULT_SEVERITY;

	/**
	 * Constructor
	 */
	public UnknownCodeSystemWarningValidationSupport(FhirContext theFhirContext) {
		super(theFhirContext);
	}

	@Override
	public String getName() {
		return getFhirContext().getVersion().getVersion() + " Unknown Code System Warning Validation Support";
	}

	@Override
	public boolean isValueSetSupported(ValidationSupportContext theValidationSupportContext, String theValueSetUrl) {
		return true;
	}

	@Override
	public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
		return canValidateCodeSystem(theValidationSupportContext, theSystem);
	}

	@Nullable
	@Override
	public LookupCodeResult lookupCode(
			ValidationSupportContext theValidationSupportContext, @Nonnull LookupCodeRequest theLookupCodeRequest) {
		if (canValidateCodeSystem(theValidationSupportContext, theLookupCodeRequest.getSystem())) {
			return new LookupCodeResult().setFound(true);
		}

		return null;
	}

	@Override
	public CodeValidationResult validateCode(
			@Nonnull ValidationSupportContext theValidationSupportContext,
			@Nonnull ConceptValidationOptions theOptions,
			String theCodeSystem,
			String theCode,
			String theDisplay,
			String theValueSetUrl) {
		if (!canValidateCodeSystem(theValidationSupportContext, theCodeSystem)) {
			return null;
		}

		CodeValidationResult result = new CodeValidationResult();
		// will be warning or info (error/fatal filtered out above)
		result.setSeverity(myNonExistentCodeSystemSeverity);
		String theMessage = "CodeSystem is unknown and can't be validated: " + theCodeSystem + " for '" + theCodeSystem
				+ "#" + theCode + "'";
		result.setMessage(theMessage);

		result.addIssue(new CodeValidationIssue(
				theMessage,
				myNonExistentCodeSystemSeverity,
				CodeValidationIssueCode.NOT_FOUND,
				CodeValidationIssueCoding.NOT_FOUND));

		return result;
	}

	@Nullable
	@Override
	public CodeValidationResult validateCodeInValueSet(
			ValidationSupportContext theValidationSupportContext,
			ConceptValidationOptions theOptions,
			String theCodeSystem,
			String theCode,
			String theDisplay,
			@Nonnull IBaseResource theValueSet) {
		if (!canValidateCodeSystem(theValidationSupportContext, theCodeSystem)) {
			return null;
		}

		return new CodeValidationResult()
				.setCode(theCode)
				.setSeverity(IssueSeverity.INFORMATION)
				.setMessage("Code " + theCodeSystem + "#" + theCode
						+ " was not checked because the CodeSystem is not available");
	}

	/**
	 * If a validation support can fetch the code system, returns false. Otherwise, returns true.
	 */
	public boolean canValidateCodeSystem(ValidationSupportContext theValidationSupportContext, String theCodeSystem) {
		if (theCodeSystem == null) {
			return false;
		}
		IBaseResource codeSystem =
				theValidationSupportContext.getRootValidationSupport().fetchCodeSystem(theCodeSystem);
		if (codeSystem != null) {
			return false;
		}
		return true;
	}

	/**
	 * If set to allow, code system violations will be flagged with Warning by default.
	 * Use setNonExistentCodeSystemSeverity instead.
	 */
	@Deprecated
	public void setAllowNonExistentCodeSystem(boolean theAllowNonExistentCodeSystem) {
		if (theAllowNonExistentCodeSystem) {
			setNonExistentCodeSystemSeverity(IssueSeverity.WARNING);
		} else {
			setNonExistentCodeSystemSeverity(IssueSeverity.ERROR);
		}
	}

	/**
	 * Sets the non-existent code system severity.
	 */
	public void setNonExistentCodeSystemSeverity(@Nonnull IssueSeverity theSeverity) {
		Validate.notNull(theSeverity, "theSeverity must not be null");
		myNonExistentCodeSystemSeverity = theSeverity;
	}
}
