package ca.uhn.fhir.rest.server.interceptor.validation.address.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidationException;
import ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidationResult;
import ca.uhn.fhir.rest.server.interceptor.validation.helpers.AddressHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.entity.ContentType;
import org.hl7.fhir.instance.model.api.IBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import javax.annotation.Nullable;
import java.util.Properties;

/**
 * For more details regarind the API refer to
 * <a href="https://www.loqate.com/resources/support/cleanse-api/international-batch-cleanse/">
 * https://www.loqate.com/resources/support/cleanse-api/international-batch-cleanse/
 * </a>
 */
public class LoquateAddressValidator extends BaseRestfulValidator {

	private static final Logger ourLog = LoggerFactory.getLogger(LoquateAddressValidator.class);

	private static final String[] DUPLICATE_FIELDS_IN_ADDRESS_LINES = {"Locality", "AdministrativeArea", "PostalCode"};

	private static final String DATA_CLEANSE_ENDPOINT = "https://api.addressy.com/Cleansing/International/Batch/v1.00/json4.ws";
	private static final int MAX_ADDRESS_LINES = 8;

	public LoquateAddressValidator(Properties theProperties) {
		super(theProperties);
		if (!theProperties.containsKey(PROPERTY_SERVICE_KEY)) {
			throw new IllegalArgumentException(String.format("Missing service key defined as %s", PROPERTY_SERVICE_KEY));
		}
	}

	@Override
	protected AddressValidationResult getValidationResult(AddressValidationResult theResult, JsonNode response, FhirContext theFhirContext) {
		if (!response.isArray() || response.size() < 1) {
			throw new AddressValidationException("Invalid response - expected to get an array of validated addresses");
		}

		JsonNode firstMatch = response.get(0);
		if (!firstMatch.has("Matches")) {
			throw new AddressValidationException("Invalid response - matches are unavailable");
		}

		JsonNode matches = firstMatch.get("Matches");
		if (!matches.isArray()) {
			throw new AddressValidationException("Invalid response - expected to get a validated match in the response");
		}

		JsonNode match = matches.get(0);
		return toAddressValidationResult(theResult, match, theFhirContext);
	}

	private AddressValidationResult toAddressValidationResult(AddressValidationResult theResult, JsonNode theMatch, FhirContext theFhirContext) {
		theResult.setValid(isValid(theMatch));

		ourLog.debug("Address validation flag {}", theResult.isValid());
		theResult.setValidatedAddressString(theMatch.get("Address").asText());

		ourLog.debug("Validated address string {}", theResult.getValidatedAddressString());
		theResult.setValidatedAddress(toAddress(theMatch, theFhirContext));
		return theResult;
	}

	protected boolean isValid(JsonNode theMatch) {
		String addressQualityIndex = null;
		if (theMatch.has("AQI")) {
			addressQualityIndex = theMatch.get("AQI").asText();
		}

		ourLog.debug("Address quality index {}", addressQualityIndex);
		return "A".equals(addressQualityIndex) || "B".equals(addressQualityIndex);
	}

	protected IBase toAddress(JsonNode match, FhirContext theFhirContext) {
		IBase addressBase = theFhirContext.getElementDefinition("Address").newInstance();

		AddressHelper helper = new AddressHelper(addressBase, theFhirContext);
		helper.setText(getString(match, "Address"));

		String str = getString(match, "Address1");
		if (str != null) {
			helper.addLine(str);
		}

		removeDuplicateAddressLines(match, helper);

		helper.setCity(getString(match, "Locality"));
		helper.setState(getString(match, "AdministrativeArea"));
		helper.setPostalCode(getString(match, "PostalCode"));
		helper.setCountry(getString(match, "CountryName"));

		return helper.getAddress();
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
			theAddressLine = theAddressLine.replaceAll(theMatch.get(s).asText(""), "");
		}
		return theAddressLine.trim().isEmpty();
	}

	@Nullable
	private String getString(JsonNode theNode, String theField) {
		if (!theNode.has(theField)) {
			return null;
		}

		JsonNode field = theNode.get(theField);
		if (field.asText().isEmpty()) {
			return null;
		}
		return theNode.get(theField).asText();
	}

	@Override
	protected ResponseEntity<String> getResponseEntity(IBase theAddress, FhirContext theFhirContext) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
		headers.set(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
		headers.set(HttpHeaders.USER_AGENT, "SmileCDR");

		String requestBody = getRequestBody(theFhirContext, theAddress);
		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
		return newTemplate().postForEntity(DATA_CLEANSE_ENDPOINT, request, String.class);
	}

	protected String getRequestBody(FhirContext theFhirContext, IBase... theAddresses) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode rootNode = mapper.createObjectNode();
		rootNode.put("Key", getApiKey());
		rootNode.put("Geocode", false);

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
		AddressHelper helper = new AddressHelper(theAddress, theFhirContext);
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
}
