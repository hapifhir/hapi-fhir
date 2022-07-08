package ca.uhn.fhir.rest.server.interceptor.validation.address.impl;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.interceptor.validation.address.IAddressValidator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidationException;
import ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidationResult;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

public abstract class BaseRestfulValidator implements IAddressValidator {

	public static final String PROPERTY_SERVICE_KEY = "service.key";
	public static final String PROPERTY_SERVICE_ENDPOINT = "service.endpoint";

	private static final Logger ourLog = LoggerFactory.getLogger(BaseRestfulValidator.class);

	private Properties myProperties;

	protected abstract AddressValidationResult getValidationResult(AddressValidationResult theResult, JsonNode response, FhirContext theFhirContext) throws Exception;

	protected abstract ResponseEntity<String> getResponseEntity(IBase theAddress, FhirContext theFhirContext) throws Exception;

	protected RestTemplate newTemplate() {
		return new RestTemplate();
	}

	public BaseRestfulValidator(Properties theProperties) {
		myProperties = theProperties;
	}

	@Override
	public AddressValidationResult isValid(IBase theAddress, FhirContext theFhirContext) throws AddressValidationException {
		ResponseEntity<String> entity;
		try {
			entity = getResponseEntity(theAddress, theFhirContext);
		} catch (Exception e) {
			throw new AddressValidationException(Msg.code(345) + "Unable to complete address validation web-service call", e);
		}

		if (isError(entity)) {
			throw new AddressValidationException(Msg.code(346) + String.format("Service returned an error code %s", entity.getStatusCode()));
		}

		String responseBody = entity.getBody();
		ourLog.debug("Validation service returned {}", responseBody);

		AddressValidationResult retVal = new AddressValidationResult();
		retVal.setRawResponse(responseBody);

		try {
			JsonNode response = new ObjectMapper().readTree(responseBody);
			ourLog.debug("Parsed address validator response {}", response);
			return getValidationResult(retVal, response, theFhirContext);
		} catch (Exception e) {
			throw new AddressValidationException(Msg.code(347) + "Unable to validate the address", e);
		}
	}

	protected boolean isError(ResponseEntity<String> entity) {
		return entity.getStatusCode().isError();
	}

	public Properties getProperties() {
		return myProperties;
	}

	public void setProperties(Properties theProperties) {
		myProperties = theProperties;
	}

	protected String getApiKey() {
		return getProperties().getProperty(PROPERTY_SERVICE_KEY);
	}

	protected String getApiEndpoint() {
		return getProperties().getProperty(PROPERTY_SERVICE_ENDPOINT);
	}
}
