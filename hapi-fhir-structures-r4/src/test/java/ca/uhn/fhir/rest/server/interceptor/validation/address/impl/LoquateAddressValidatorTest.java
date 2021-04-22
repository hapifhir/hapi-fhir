package ca.uhn.fhir.rest.server.interceptor.validation.address.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidationException;
import ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidationResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.r4.model.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class LoquateAddressValidatorTest {

	private static final String REQUEST = "{\n" +
		"  \"Key\" : \"MY_KEY\",\n" +
		"  \"Geocode\" : false,\n" +
		"  \"Addresses\" : [ {\n" +
		"    \"Address1\" : \"Line 1\",\n" +
		"    \"Address2\" : \"Line 2\",\n" +
		"    \"Locality\" : \"City\",\n" +
		"    \"PostalCode\" : \"POSTAL\",\n" +
		"    \"Country\" : \"Country\"\n" +
		"  } ]\n" +
		"}";

	private static final String RESPONSE_INVALID = "[\n" +
		"  {\n" +
		"    \"Input\": {\n" +
		"      \"Address\": \"\"\n" +
		"    }\n" +
		"  }\n" +
		"]";

	private static final String RESPONSE_INVALID_ADDRESS = "[\n" +
		"  {\n" +
		"    \"Input\": {\n" +
		"      \"Address\": \"\"\n" +
		"    },\n" +
		"    \"Matches\": [\n" +
		"      {\n" +
		"        \"AQI\": \"C\",\n" +
		"        \"Address\": \"\"\n" +
		"      }\n" +
		"    ]\n" +
		"  }\n" +
		"]";

	private static final String RESPONSE_VALID_ADDRESS = "[\n" +
		"  {\n" +
		"    \"Input\": {\n" +
		"      \"Address\": \"\"\n" +
		"    },\n" +
		"    \"Matches\": [\n" +
		"      {\n" +
		"        \"AQI\": \"A\",\n" +
		"        \"Address\": \"My Valid Address\"\n" +
		"      }\n" +
		"    ]\n" +
		"  }\n" +
		"]";

	private static final String RESPONSE_INVALID_KEY = "{\n" +
		"  \"Number\": 2,\n" +
		"  \"Description\": \"Unknown key\",\n" +
		"  \"Cause\": \"The key you are using to access the service was not found.\",\n" +
		"  \"Resolution\": \"Please check that the key is correct. It should be in the form AA11-AA11-AA11-AA11.\"\n" +
		"}";

	private static FhirContext ourCtx = FhirContext.forR4();

	private LoquateAddressValidator myValidator;

	private Properties myProperties;

	@BeforeEach
	public void initValidator() {
		myProperties = new Properties();
		myProperties.setProperty(LoquateAddressValidator.PROPERTY_SERVICE_KEY, "MY_KEY");
		myValidator = new LoquateAddressValidator(myProperties);
	}

	@Test
	public void testEndpointOverride() {
		assertEquals(LoquateAddressValidator.DEFAULT_DATA_CLEANSE_ENDPOINT, myValidator.getApiEndpoint());

		myProperties = new Properties();
		myProperties.setProperty(LoquateAddressValidator.PROPERTY_SERVICE_KEY, "MY_KEY");
		myProperties.setProperty(LoquateAddressValidator.PROPERTY_SERVICE_ENDPOINT, "HTTP://MY_ENDPOINT/LOQUATE");
		myValidator = new LoquateAddressValidator(myProperties);

		assertEquals("HTTP://MY_ENDPOINT/LOQUATE", myValidator.getApiEndpoint());
	}

	@Test
	public void testInvalidInit() {
		try {
			new LoquateAddressValidator(new Properties());
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void testInvalidAddressValidationResponse() throws Exception {
		try {
			AddressValidationResult res = myValidator.getValidationResult(new AddressValidationResult(),
				new ObjectMapper().readTree(RESPONSE_INVALID), ourCtx);
			fail();
		} catch (AddressValidationException e) {
		}
	}

	@Test
	public void testRequestBody() {
		try {
			assertEquals(clear(REQUEST), clear(myValidator.getRequestBody(ourCtx, getAddress())));
		} catch (JsonProcessingException e) {
			fail();
		}
	}

	private String clear(String theString) {
		theString = theString.replaceAll("\n", "");
		theString = theString.replaceAll("\r", "");
		return theString.trim();
	}

	@Test
	public void testServiceCalled() {
		Address address = getAddress();

		final RestTemplate template = mock(RestTemplate.class);

		LoquateAddressValidator val = new LoquateAddressValidator(myProperties) {
			@Override
			protected RestTemplate newTemplate() {
				return template;
			}
		};

		try {
			val.getResponseEntity(address, ourCtx);
		} catch (Exception e) {
			fail();
		}

		verify(template, times(1)).postForEntity(any(String.class), any(HttpEntity.class), eq(String.class));
	}

	private Address getAddress() {
		Address address = new Address();
		address.addLine("Line 1").addLine("Line 2").setCity("City").setPostalCode("POSTAL").setCountry("Country");
		return address;
	}

	@Test
	public void testSuccessfulResponses() throws Exception {
		AddressValidationResult res = myValidator.getValidationResult(new AddressValidationResult(),
			new ObjectMapper().readTree(RESPONSE_INVALID_ADDRESS), ourCtx);
		assertFalse(res.isValid());

		res = myValidator.getValidationResult(new AddressValidationResult(),
			new ObjectMapper().readTree(RESPONSE_VALID_ADDRESS), ourCtx);
		assertTrue(res.isValid());
		assertEquals("My Valid Address", res.getValidatedAddressString());
	}

	@Test
	public void testErrorResponses() throws Exception {
		assertThrows(AddressValidationException.class, () -> {
			myValidator.getValidationResult(new AddressValidationResult(),
				new ObjectMapper().readTree(RESPONSE_INVALID_KEY), ourCtx);
		});
	}

}
