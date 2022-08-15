package ca.uhn.fhir.rest.server.interceptor.validation.address;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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
	 * URL for validation results that should be placed on addresses. Extension with boolean value "true" indicates there there is an address validation error.
	 */
	public static final String ADDRESS_VALIDATION_EXTENSION_URL = "http://hapifhir.org/StructureDefinition/ext-validation-address-has-error";

	/**
	 * URL for an optional address quality extensions that may be added to addresses.
	 */
	public static final String ADDRESS_QUALITY_EXTENSION_URL = "http://hapifhir.org/StructureDefinition/ext-validation-address-quality";

	/**
	 * URL for an optional geocoding accuracy extensions that may be added to addresses.
	 */
	public static final String ADDRESS_GEO_ACCURACY_EXTENSION_URL = "http://hapifhir.org/StructureDefinition/ext-validation-address-geo-accuracy";

	/**
	 * URL for an optional address verification extensions that may be added to addresses.
	 */
	public static final String ADDRESS_VERIFICATION_CODE_EXTENSION_URL = "http://hapifhir.org/StructureDefinition/ext-validation-address-verification";

	/**
	 * URL for an optional FHIR geolocation extension.
	 */
	public static final String FHIR_GEOCODE_EXTENSION_URL = "http://hl7.org/fhir/StructureDefinition/geolocation";

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
