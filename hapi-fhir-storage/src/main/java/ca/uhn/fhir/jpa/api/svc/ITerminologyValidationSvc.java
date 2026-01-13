/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.api.svc;

import ca.uhn.fhir.context.support.IValidationSupport.CodeValidationResult;

/**
 * Service for validating codes against ValueSets and CodeSystems.
 * <p>
 * This service provides a unified interface for code validation that handles both
 * remote terminology service delegation and local DAO layer validation.
 * </p>
 *
 * // Created by claude-opus-4-5-20251101
 */
public interface ITerminologyValidationSvc {

	/**
	 * Validates a code against a ValueSet.
	 *
	 * @param theRequest The validation request containing all parameters
	 * @return The validation result
	 */
	CodeValidationResult validateCodeAgainstValueSet(ValueSetValidationRequest theRequest);

	/**
	 * Validates a code against a CodeSystem.
	 *
	 * @param theRequest The validation request containing all parameters
	 * @return The validation result
	 */
	CodeValidationResult validateCodeAgainstCodeSystem(CodeSystemValidationRequest theRequest);
}
