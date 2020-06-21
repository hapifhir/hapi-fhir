package ca.uhn.fhir.jpa.validation;

import org.hl7.fhir.r5.utils.IResourceValidator;
import org.thymeleaf.util.Validate;

import javax.annotation.Nonnull;

public class ValidationSettings {

	private IResourceValidator.ReferenceValidationPolicy myLocalReferenceValidationDefaultPolicy = IResourceValidator.ReferenceValidationPolicy.IGNORE;

	/**
	 * Supplies a default policy for validating local references. Default is {@literal IResourceValidator.ReferenceValidationPolicy.IGNORE}.
	 * <p>
	 * Note that this setting can have a measurable impact on validation performance, as it will cause reference targets
	 * to be resolved during validation. In other words, if a resource has a reference to (for example) "Patient/123", the
	 * resource with that ID will be loaded from the database during validation.
	 * </p>
	 *
	 * @since 5.1.0
	 */
	@Nonnull
	public IResourceValidator.ReferenceValidationPolicy getLocalReferenceValidationDefaultPolicy() {
		return myLocalReferenceValidationDefaultPolicy;
	}

	/**
	 * Supplies a default policy for validating local references. Default is {@literal IResourceValidator.ReferenceValidationPolicy.IGNORE}.
	 * <p>
	 * Note that this setting can have a measurable impact on validation performance, as it will cause reference targets
	 * to be resolved during validation. In other words, if a resource has a reference to (for example) "Patient/123", the
	 * resource with that ID will be loaded from the database during validation.
	 * </p>
	 *
	 * @since 5.1.0
	 */
	public void setLocalReferenceValidationDefaultPolicy(@Nonnull IResourceValidator.ReferenceValidationPolicy theLocalReferenceValidationDefaultPolicy) {
		Validate.notNull(theLocalReferenceValidationDefaultPolicy, "theLocalReferenceValidationDefaultPolicy must not be null");
		myLocalReferenceValidationDefaultPolicy = theLocalReferenceValidationDefaultPolicy;
	}
}
