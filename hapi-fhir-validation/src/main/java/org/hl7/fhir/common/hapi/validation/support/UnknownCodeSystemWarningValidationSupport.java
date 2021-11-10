package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import org.hl7.fhir.common.hapi.validation.validator.ValidatorWrapper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static final Logger ourLog = LoggerFactory.getLogger(UnknownCodeSystemWarningValidationSupport.class);

	public static final IssueSeverity DEFAULT_SEVERITY = IssueSeverity.ERROR;

	private IssueSeverity myNonExistentCodeSystemSeverity = DEFAULT_SEVERITY;

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

	@Override
	public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
		return canValidateCodeSystem(theValidationSupportContext, theSystem);
	}

	@Override
	public CodeValidationResult validateCode(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, String theValueSetUrl) {
		// filters out error/fatal
		if (!canValidateCodeSystem(theValidationSupportContext, theCodeSystem)) {
			return null;
		}

		CodeValidationResult result = new CodeValidationResult()
			.setSeverity(myNonExistentCodeSystemSeverity); // will be warning or info

		result.setMessage("No issues detected during validation");

		switch (myNonExistentCodeSystemSeverity) {
			case INFORMATION:
				// for warnings, we don't set the code
				// cause if we do, the severity is stripped out
				// (see VersionSpecificWorkerContextWrapper.convertValidationResult)
				result.setCode(theCode);
				break;
		}

		return result;
	}

	@Nullable
	@Override
	public CodeValidationResult validateCodeInValueSet(ValidationSupportContext theValidationSupportContext, ConceptValidationOptions theOptions, String theCodeSystem, String theCode, String theDisplay, @Nonnull IBaseResource theValueSet) {
		if (!canValidateCodeSystem(theValidationSupportContext, theCodeSystem)) {
			return null;
		}

		return new CodeValidationResult()
			.setCode(theCode)
			.setSeverity(IssueSeverity.INFORMATION)
			.setMessage("Code " + theCodeSystem + "#" + theCode + " was not checked because the CodeSystem is not available");
	}

	/**
	 * Returns true if non existent code systems will still validate.
	 * False if they will throw errors.
	 * @return
	 */
	private boolean allowNonExistentCodeSystems() {
		switch (myNonExistentCodeSystemSeverity) {
			case ERROR:
			case FATAL:
				return false;
			default:
				ourLog.info("Unknown issue severity " + myNonExistentCodeSystemSeverity.name()
					+ ". Treating as INFO/WARNING");
			case WARNING:
			case INFORMATION:
				return true;
		}
	}

	private boolean canValidateCodeSystem(ValidationSupportContext theValidationSupportContext,
													  String theCodeSystem) {
		if (!allowNonExistentCodeSystems()) {
			return false;
		}
		if (theCodeSystem == null) {
			return false;
		}
		IBaseResource codeSystem = theValidationSupportContext.getRootValidationSupport().fetchCodeSystem(theCodeSystem);
		if (codeSystem != null) {
			return false;
		}
		return true;
	}

	/**
	 * If set to allow, code system violations will be flagged with Warning by default.
	 * Use setNonExistentCodeSystemSeverity instead.
	 *
	 * @param theAllowNonExistentCodeSystem
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
	 * @param theSeverity
	 */
	public void setNonExistentCodeSystemSeverity(IssueSeverity theSeverity) {
		myNonExistentCodeSystemSeverity = theSeverity;
	}
}
