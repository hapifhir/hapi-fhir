package ca.uhn.fhir.jpa.validation;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
