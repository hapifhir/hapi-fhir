package ca.uhn.fhir.rest.server.interceptor.validation.address.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidationException;
import ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidationResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hl7.fhir.r4.model.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
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

class MelissaAddressValidatorTest {

	private static final String RESPONSE_INVALID_ADDRESS = "{\n" +
		"  \"Version\": \"3.0.1.160\",\n" +
		"  \"TransmissionReference\": \"1\",\n" +
		"  \"TransmissionResults\": \"\",\n" +
		"  \"TotalRecords\": \"1\",\n" +
		"  \"Records\": [\n" +
		"    {\n" +
		"      \"RecordID\": \"1\",\n" +
		"      \"Results\": \"AC01,AC12,AE02,AV12,GE02\",\n" +
		"      \"FormattedAddress\": \"100 Main Street\",\n" +
		"      \"Organization\": \"\",\n" +
		"      \"AddressLine1\": \"100 Main Street\"\n" +
		"    }\n" +
		"  ]\n" +
		"}";

	private static final String RESPONSE_VALID_ADDRESS = "{\n" +
		"  \"Version\": \"3.0.1.160\",\n" +
		"  \"TransmissionReference\": \"1\",\n" +
		"  \"TransmissionResults\": \"\",\n" +
		"  \"TotalRecords\": \"1\",\n" +
		"  \"Records\": [\n" +
		"    {\n" +
		"      \"RecordID\": \"1\",\n" +
		"      \"Results\": \"AC01,AV24,GS05\",\n" +
		"      \"FormattedAddress\": \"100 Main St W;Hamilton ON  L8P 1H6\"\n" +
		"   }\n" +
		"  ]\n" +
		"}";

	private static final String RESPONSE_INVALID_KEY = "{\n" +
		"  \"Version\": \"3.0.1.160\",\n" +
		"  \"TransmissionReference\": \"1\",\n" +
		"  \"TransmissionResults\": \"GE05\",\n" +
		"  \"TotalRecords\": \"0\"\n" +
		"}";

	private static FhirContext ourContext = FhirContext.forR4();

	private MelissaAddressValidator myValidator;

	@BeforeEach
	public void init() {
		Properties props = new Properties();
		props.setProperty(MelissaAddressValidator.PROPERTY_SERVICE_KEY, "MY_KEY");
		myValidator = new MelissaAddressValidator(props);

	}

	@Test
	public void testRequestBody() {
		Map<String, String> params = myValidator.getRequestParams(getAddress());

		assertEquals("Line 1, Line 2", params.get("a1"));
		assertEquals("City, POSTAL", params.get("a2"));
		assertEquals("Country", params.get("ctry"));
		assertEquals("MY_KEY", params.get("id"));
		assertEquals("json", params.get("format"));
		assertTrue(params.containsKey("t"));
	}

	@Test
	public void testServiceCalled() {
		Address address = getAddress();

		final RestTemplate template = mock(RestTemplate.class);

		Properties props = new Properties();
		props.setProperty(BaseRestfulValidator.PROPERTY_SERVICE_KEY, "MY_KEY");
		MelissaAddressValidator val = new MelissaAddressValidator(props) {
			@Override
			protected RestTemplate newTemplate() {
				return template;
			}
		};

		try {
			val.getResponseEntity(address, ourContext);
		} catch (Exception e) {
			fail();
		}

		verify(template, times(1)).getForEntity(any(String.class), eq(String.class), any(Map.class));
	}

	private Address getAddress() {
		Address address = new Address();
		address.addLine("Line 1").addLine("Line 2").setCity("City").setPostalCode("POSTAL").setCountry("Country");
		return address;
	}

	@Test
	public void testSuccessfulResponses() throws Exception {
		AddressValidationResult res = myValidator.getValidationResult(new AddressValidationResult(),
			new ObjectMapper().readTree(RESPONSE_INVALID_ADDRESS), ourContext);
		assertFalse(res.isValid());

		res = myValidator.getValidationResult(new AddressValidationResult(),
			new ObjectMapper().readTree(RESPONSE_VALID_ADDRESS), ourContext);
		assertTrue(res.isValid());
		assertEquals("100 Main St W;Hamilton ON  L8P 1H6", res.getValidatedAddressString());
	}

	@Test
	public void testErrorResponses() throws Exception {
		assertThrows(AddressValidationException.class, () -> {
			myValidator.getValidationResult(new AddressValidationResult(),
				new ObjectMapper().readTree(RESPONSE_INVALID_KEY), ourContext);
		});
	}

}
