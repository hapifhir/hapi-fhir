package ca.uhn.fhir.rest.server.interceptor.validation.address.impl;

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
import ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidationException;
import ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidationResult;
import ca.uhn.fhir.rest.server.interceptor.validation.helpers.AddressHelper;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBase;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public class MelissaAddressValidator extends BaseRestfulValidator {

	public static final String GLOBAL_ADDRESS_VALIDATION_ENDPOINT = "https://address.melissadata.net/v3/WEB/GlobalAddress/doGlobalAddress" +
		"?id={id}&a1={a1}&a2={a2}&ctry={ctry}&format={format}";

	public MelissaAddressValidator(Properties theProperties) {
		super(theProperties);
	}

	@Override
	protected AddressValidationResult getValidationResult(AddressValidationResult theResult, JsonNode theResponse, FhirContext theFhirContext) {
		Response response = new Response(theResponse);
		theResult.setValid(response.isValidAddress());
		theResult.setValidatedAddressString(response.getAddress());
		return theResult;
	}

	@Override
	protected ResponseEntity<String> getResponseEntity(IBase theAddress, FhirContext theFhirContext) throws Exception {
		Map<String, String> requestParams = getRequestParams(theAddress);
		return newTemplate().getForEntity(GLOBAL_ADDRESS_VALIDATION_ENDPOINT, String.class, requestParams);
	}

	protected Map<String, String> getRequestParams(IBase theAddress) {
		AddressHelper helper = new AddressHelper(theAddress, null);

		Map<String, String> requestParams = new HashMap<>();
		requestParams.put("t", UUID.randomUUID().toString());
		requestParams.put("id", getApiKey());
		requestParams.put("a1", helper.getLine());
		requestParams.put("a2", helper.getParts());
		requestParams.put("ctry", helper.getCountry());
		requestParams.put("format", "json");
		return requestParams;
	}

	private static class Response {
		private JsonNode root;
		private JsonNode records;
		private JsonNode results;

		private List<String> addressErrors = new ArrayList<>();
		private List<String> addressChange = new ArrayList<>();
		private List<String> geocodeStatus = new ArrayList<>();
		private List<String> geocodeError = new ArrayList<>();
		private List<String> addressVerification = new ArrayList<>();

		public Response(JsonNode theRoot) {
			root = theRoot;

			// see codes here - http://wiki.melissadata.com/index.php?title=Result_Codes
			String transmissionResults = root.get("TransmissionResults").asText();
			if (!StringUtils.isEmpty(transmissionResults)) {
				geocodeError.add(transmissionResults);
				throw new AddressValidationException(String.format("Transmission result %s indicate an error with the request - please check API_KEY", transmissionResults));
			}

			int recordCount = root.get("TotalRecords").asInt();
			if (recordCount < 1) {
				throw new AddressValidationException("Expected at least one record in the address validation response");
			}

			// get first match
			records = root.get("Records").get(0);
			results = records.get("Results");

			// full list of response codes is available here
			// http://wiki.melissadata.com/index.php?title=Result_Code_Details#Global_Address_Verification
			for (String s : results.asText().split(",")) {
				if (s.startsWith("AE")) {
					addressErrors.add(s);
				} else if (s.startsWith("AC")) {
					addressChange.add(s);
				} else if (s.startsWith("GS")) {
					geocodeStatus.add(s);
				} else if (s.startsWith("GE")) {
					geocodeError.add(s);
				} else if (s.startsWith("AV")) {
					addressVerification.add(s);
				}
			}
		}

		public boolean isValidAddress() {
			if (!geocodeError.isEmpty()) {
				return false;
			}
			return addressErrors.isEmpty() && (geocodeStatus.contains("GS05") || geocodeStatus.contains("GS06"));
		}

		public String getAddress() {
			if (records == null) {
				return "";
			}
			if (!records.has("FormattedAddress")) {
				return "";
			}
			return records.get("FormattedAddress").asText("");
		}
	}
}
