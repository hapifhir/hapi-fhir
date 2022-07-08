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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidationResult;
import ca.uhn.fhir.rest.server.interceptor.validation.helpers.AddressHelper;
import ca.uhn.fhir.util.ExtensionUtil;
import ca.uhn.fhir.util.TerserUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.entity.ContentType;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ca.uhn.fhir.rest.server.interceptor.validation.address.IAddressValidator.ADDRESS_QUALITY_EXTENSION_URL;
import static ca.uhn.fhir.rest.server.interceptor.validation.address.IAddressValidator.ADDRESS_VERIFICATION_CODE_EXTENSION_URL;

/**
 * For more details regarind the API refer to
 * <a href="https://www.loqate.com/resources/support/cleanse-api/international-batch-cleanse/">
 * https://www.loqate.com/resources/support/cleanse-api/international-batch-cleanse/
 * </a>
 */
public class LoquateAddressValidator extends BaseRestfulValidator {

	private static final Logger ourLog = LoggerFactory.getLogger(LoquateAddressValidator.class);

	public static final String PROPERTY_GEOCODE = "service.geocode";
	public static final String LOQUATE_AQI = "AQI";
	public static final String LOQUATE_AVC = "AVC";
	public static final String LOQUATE_GEO_ACCURACY = "GeoAccuracy";

	protected static final String[] DUPLICATE_FIELDS_IN_ADDRESS_LINES = {"Locality", "AdministrativeArea", "PostalCode"};
	protected static final String DEFAULT_DATA_CLEANSE_ENDPOINT = "https://api.addressy.com/Cleansing/International/Batch/v1.00/json4.ws";
	protected static final int MAX_ADDRESS_LINES = 8;

	private Pattern myCommaPattern = Pattern.compile("\\,(\\S)");

	public LoquateAddressValidator(Properties theProperties) {
		super(theProperties);
		Validate.isTrue(theProperties.containsKey(PROPERTY_SERVICE_KEY) || theProperties.containsKey(PROPERTY_SERVICE_ENDPOINT),
			"Expected service key or custom service endpoint in the configuration, but got " + theProperties);
	}

	@Override
	protected AddressValidationResult getValidationResult(AddressValidationResult theResult, JsonNode response, FhirContext theFhirContext) {
		Validate.isTrue(response.isArray() && response.size() >= 1, "Invalid response - expected to get an array of validated addresses");

		JsonNode firstMatch = response.get(0);
		Validate.isTrue(firstMatch.has("Matches"), "Invalid response - matches are unavailable");

		JsonNode matches = firstMatch.get("Matches");
		Validate.isTrue(matches.isArray(), "Invalid response - expected to get a validated match in the response");

		JsonNode match = matches.get(0);
		return toAddressValidationResult(theResult, match, theFhirContext);
	}

	private AddressValidationResult toAddressValidationResult(AddressValidationResult theResult, JsonNode theMatch, FhirContext theFhirContext) {
		theResult.setValid(isValid(theMatch));

		ourLog.debug("Address validation flag {}", theResult.isValid());
		JsonNode addressNode = theMatch.get("Address");
		if (addressNode != null) {
			theResult.setValidatedAddressString(addressNode.asText());
		}

		ourLog.debug("Validated address string {}", theResult.getValidatedAddressString());
		theResult.setValidatedAddress(toAddress(theMatch, theFhirContext));
		return theResult;
	}

	protected boolean isValid(JsonNode theMatch) {
		String addressQualityIndex = getField(theMatch, LOQUATE_AQI);
		return "A".equals(addressQualityIndex) || "B".equals(addressQualityIndex) || "C".equals(addressQualityIndex);
	}

	private String getField(JsonNode theMatch, String theFieldName) {
		String field = null;
		if (theMatch.has(theFieldName)) {
			field = theMatch.get(theFieldName).asText();
		}
		ourLog.debug("Found {}={}", theFieldName, field);
		return field;
	}

	protected IBase toAddress(JsonNode match, FhirContext theFhirContext) {
		IBase addressBase = theFhirContext.getElementDefinition("Address").newInstance();

		AddressHelper helper = new AddressHelper(theFhirContext, addressBase);
		helper.setText(standardize(getString(match, "Address")));

		String str = getString(match, "Address1");
		if (str != null) {
			helper.addLine(str);
		}

		if (isGeocodeEnabled()) {
			toGeolocation(match, helper, theFhirContext);
		}

		removeDuplicateAddressLines(match, helper);

		helper.setCity(getString(match, "Locality"));
		helper.setState(getString(match, "AdministrativeArea"));
		helper.setPostalCode(getString(match, "PostalCode"));
		helper.setCountry(getString(match, "CountryName"));

		addExtension(match, LOQUATE_AQI, ADDRESS_QUALITY_EXTENSION_URL, helper, theFhirContext);
		addExtension(match, LOQUATE_AVC, ADDRESS_VERIFICATION_CODE_EXTENSION_URL, helper, theFhirContext);
		addExtension(match, LOQUATE_GEO_ACCURACY, ADDRESS_GEO_ACCURACY_EXTENSION_URL, helper, theFhirContext);

		return helper.getAddress();
	}

	private void addExtension(JsonNode theMatch, String theMatchField, String theExtUrl, AddressHelper theHelper, FhirContext theFhirContext) {
		String addressQuality = getField(theMatch, theMatchField);
		if (StringUtils.isEmpty(addressQuality)) {
			ourLog.debug("{} is not found in {}", theMatchField, theMatch);
			return;
		}

		IBase address = theHelper.getAddress();
		ExtensionUtil.clearExtensionsByUrl(address, theExtUrl);

		IBaseExtension addressQualityExt = ExtensionUtil.addExtension(address, theExtUrl);
		addressQualityExt.setValue(TerserUtil.newElement(theFhirContext, "string", addressQuality));
	}

	private void toGeolocation(JsonNode theMatch, AddressHelper theHelper, FhirContext theFhirContext) {
		if (!theMatch.has("Latitude") || !theMatch.has("Longitude")) {
			ourLog.warn("Geocode is not provided in JSON {}", theMatch);
			return;
		}

		IBase address = theHelper.getAddress();
		ExtensionUtil.clearExtensionsByUrl(address, FHIR_GEOCODE_EXTENSION_URL);
		IBaseExtension geolocation = ExtensionUtil.addExtension(address, FHIR_GEOCODE_EXTENSION_URL);

		IBaseExtension latitude = ExtensionUtil.addExtension(geolocation, "latitude");
		latitude.setValue(TerserUtil.newElement(theFhirContext, "decimal",
			BigDecimal.valueOf(theMatch.get("Latitude").asDouble())));

		IBaseExtension longitude = ExtensionUtil.addExtension(geolocation, "longitude");
		longitude.setValue(TerserUtil.newElement(theFhirContext, "decimal",
			BigDecimal.valueOf(theMatch.get("Longitude").asDouble())));
	}

	private void removeDuplicateAddressLines(JsonNode match, AddressHelper address) {
		int lineCount = 1;
		String addressLine = null;
		while ((addressLine = getString(match, "Address" + ++lineCount)) != null) {
			if (isDuplicate(addressLine, match)) {
				continue;
			}
			address.addLine(addressLine);
		}
	}

	private boolean isDuplicate(String theAddressLine, JsonNode theMatch) {
		for (String s : DUPLICATE_FIELDS_IN_ADDRESS_LINES) {
			JsonNode node = theMatch.get(s);
			if (node == null) {
				continue;
			}
			theAddressLine = theAddressLine.replaceAll(node.asText(), "");
		}
		return theAddressLine.trim().isEmpty();
	}

	@Nullable
	protected String getString(JsonNode theNode, String theField) {
		if (!theNode.has(theField)) {
			return null;
		}

		JsonNode field = theNode.get(theField);
		if (field.asText().isEmpty()) {
			return null;
		}

		String text = theNode.get(theField).asText();
		if (StringUtils.isEmpty(text)) {
			return "";
		}
		return text;
	}

	protected String standardize(String theText) {
		if (StringUtils.isEmpty(theText)) {
			return "";
		}

		theText = theText.replaceAll("\\s\\s", ", ");
		Matcher m = myCommaPattern.matcher(theText);
		if (m.find()) {
			theText = m.replaceAll(", $1");
		}
		return theText.trim();
	}

	@Override
	protected ResponseEntity<String> getResponseEntity(IBase theAddress, FhirContext theFhirContext) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
		headers.set(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		headers.set(HttpHeaders.USER_AGENT, "SmileCDR");

		String requestBody = getRequestBody(theFhirContext, theAddress);
		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
		return newTemplate().postForEntity(getApiEndpoint(), request, String.class);
	}

	@Override
	protected String getApiEndpoint() {
		String endpoint = super.getApiEndpoint();
		return StringUtils.isEmpty(endpoint) ? DEFAULT_DATA_CLEANSE_ENDPOINT : endpoint;
	}

	protected String getRequestBody(FhirContext theFhirContext, IBase... theAddresses) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();
		if (!StringUtils.isEmpty(getApiKey())) {
			rootNode.put("Key", getApiKey());
		}
		rootNode.put("Geocode", isGeocodeEnabled());

		ArrayNode addressesArrayNode = mapper.createArrayNode();
		int i = 0;
		for (IBase address : theAddresses) {
			ourLog.debug("Converting {} out of {} addresses", i++, theAddresses.length);
			ObjectNode addressNode = toJsonNode(address, mapper, theFhirContext);
			addressesArrayNode.add(addressNode);
		}
		rootNode.set("Addresses", addressesArrayNode);
		return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(rootNode);
	}

	protected ObjectNode toJsonNode(IBase theAddress, ObjectMapper mapper, FhirContext theFhirContext) {
		AddressHelper helper = new AddressHelper(theFhirContext, theAddress);
		ObjectNode addressNode = mapper.createObjectNode();

		int count = 1;
		for (String s : helper.getMultiple("line")) {
			addressNode.put("Address" + count, s);
			count++;

			if (count > MAX_ADDRESS_LINES) {
				break;
			}
		}
		addressNode.put("Locality", helper.getCity());
		addressNode.put("PostalCode", helper.getPostalCode());
		addressNode.put("Country", helper.getCountry());
		return addressNode;
	}

	protected boolean isGeocodeEnabled() {
		if (!getProperties().containsKey(PROPERTY_GEOCODE)) {
			return false;
		}
		return Boolean.parseBoolean(getProperties().getProperty(PROPERTY_GEOCODE));
	}
}
