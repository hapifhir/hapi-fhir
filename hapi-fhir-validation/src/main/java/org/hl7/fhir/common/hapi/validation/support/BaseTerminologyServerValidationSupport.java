package org.hl7.fhir.common.hapi.validation.support;

import ca.uhn.fhir.context.FhirContext;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.Validate;

/**
 * Base class for {@link ca.uhn.fhir.context.support.IValidationSupport} implementations that
 * perform {@code $validate-code} semantics and therefore have an opinion about how a
 * code-vs-display mismatch should be reported. Holds the shared {@code display_mismatch_policy}
 * field so the setting only appears on validators that actually honour it.
 */
// Created by claude-opus-4-7
public abstract class BaseTerminologyServerValidationSupport extends BaseValidationSupport {

	private IssueSeverity myIssueSeverityForCodeDisplayMismatch = IssueSeverity.WARNING;

	protected BaseTerminologyServerValidationSupport(FhirContext theFhirContext) {
		super(theFhirContext);
	}

	/**
	 * This setting controls the validation issue severity to report when a code validation
	 * finds that the code is present in the given CodeSystem, but the display name being
	 * validated doesn't match the expected value(s). Defaults to
	 * {@link ca.uhn.fhir.context.support.IValidationSupport.IssueSeverity#WARNING}. Set this
	 * value to {@link ca.uhn.fhir.context.support.IValidationSupport.IssueSeverity#INFORMATION}
	 * if you don't want to see display name validation issues at all in resource validation
	 * outcomes.
	 *
	 * @since 7.0.0
	 */
	public IssueSeverity getIssueSeverityForCodeDisplayMismatch() {
		return myIssueSeverityForCodeDisplayMismatch;
	}

	/**
	 * This setting controls the validation issue severity to report when a code validation
	 * finds that the code is present in the given CodeSystem, but the display name being
	 * validated doesn't match the expected value(s). Defaults to
	 * {@link ca.uhn.fhir.context.support.IValidationSupport.IssueSeverity#WARNING}. Set this
	 * value to {@link ca.uhn.fhir.context.support.IValidationSupport.IssueSeverity#INFORMATION}
	 * if you don't want to see display name validation issues at all in resource validation
	 * outcomes.
	 *
	 * @param theIssueSeverityForCodeDisplayMismatch The severity. Must not be {@literal null}.
	 * @since 7.0.0
	 */
	public void setIssueSeverityForCodeDisplayMismatch(@Nonnull IssueSeverity theIssueSeverityForCodeDisplayMismatch) {
		Validate.notNull(
				theIssueSeverityForCodeDisplayMismatch, "theIssueSeverityForCodeDisplayMismatch must not be null");
		myIssueSeverityForCodeDisplayMismatch = theIssueSeverityForCodeDisplayMismatch;
	}
}
