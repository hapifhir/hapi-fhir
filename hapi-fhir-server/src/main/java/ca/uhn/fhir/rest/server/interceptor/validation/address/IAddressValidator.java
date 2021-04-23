package ca.uhn.fhir.rest.server.interceptor.validation.address;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.instance.model.api.IBase;

/**
 * Contract for validating addresses.
 */
public interface IAddressValidator {

	/**
	 * URL for validation results that should be placed on addresses
	 */
	public static final String ADDRESS_VALIDATION_EXTENSION_URL = "https://hapifhir.org/StructureDefinition/ext-validation-address-error";

	/**
	 * Extension value confirming that address can be considered valid (it exists and can be traced to the building)
	 */
	public static final String EXT_VALUE_VALID = "yes";

	/**
	 * Extension value confirming that address is invalid (doesn't exist)
	 */
	public static final String EXT_VALUE_INVALID = "no";

	/**
	 * Extension value indicating that address validation was attempted but could not complete successfully
	 */
	public static final String EXT_UNABLE_TO_VALIDATE = "not-validated";

	/**
	 * Validates address against a service
	 *
	 * @param theAddress     Address to be validated
	 * @param theFhirContext Current FHIR context
	 * @return Returns true in case address represents a valid
	 * @throws AddressValidationException AddressValidationException is thrown in case validation can not be completed successfully.
	 */
	AddressValidationResult isValid(IBase theAddress, FhirContext theFhirContext) throws AddressValidationException;

}
