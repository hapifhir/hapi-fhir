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
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

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
	 * @param theValueSetId The ValueSet resource ID (optional, for instance-level operation)
	 * @param theValueSetUrl The ValueSet canonical URL
	 * @param theValueSetVersion The ValueSet version
	 * @param theCode The code to validate
	 * @param theSystem The code system URL
	 * @param theSystemVersion The code system version
	 * @param theDisplay The display value to validate
	 * @param theCoding A Coding containing system, code, and display
	 * @param theCodeableConcept A CodeableConcept containing one or more codings
	 * @param theRequestDetails The request details
	 * @return The validation result
	 */
	CodeValidationResult validateCodeAgainstValueSet(
			IIdType theValueSetId,
			IPrimitiveType<String> theValueSetUrl,
			IPrimitiveType<String> theValueSetVersion,
			IPrimitiveType<String> theCode,
			IPrimitiveType<String> theSystem,
			IPrimitiveType<String> theSystemVersion,
			IPrimitiveType<String> theDisplay,
			IBaseCoding theCoding,
			IBaseDatatype theCodeableConcept,
			RequestDetails theRequestDetails);

	/**
	 * Validates a code against a CodeSystem.
	 *
	 * @param theCodeSystemId The CodeSystem resource ID (optional, for instance-level operation)
	 * @param theCodeSystemUrl The CodeSystem canonical URL
	 * @param theVersion The CodeSystem version
	 * @param theCode The code to validate
	 * @param theDisplay The display value to validate
	 * @param theCoding A Coding containing system, code, and display
	 * @param theCodeableConcept A CodeableConcept containing one or more codings
	 * @param theRequestDetails The request details
	 * @return The validation result
	 */
	CodeValidationResult validateCodeAgainstCodeSystem(
			IIdType theCodeSystemId,
			IPrimitiveType<String> theCodeSystemUrl,
			IPrimitiveType<String> theVersion,
			IPrimitiveType<String> theCode,
			IPrimitiveType<String> theDisplay,
			IBaseCoding theCoding,
			IBaseDatatype theCodeableConcept,
			RequestDetails theRequestDetails);
}
