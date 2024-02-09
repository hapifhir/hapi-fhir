package ca.uhn.fhir.rest.server.interceptor.validation.address.impl;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidationResult;
import ca.uhn.fhir.rest.server.interceptor.validation.address.IAddressValidator;
import ca.uhn.fhir.util.ExtensionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.r4.model.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Properties;

import static ca.uhn.fhir.rest.server.interceptor.validation.address.impl.LoquateAddressValidator.PROPERTY_GEOCODE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.fail;

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
		"        \"AQI\": \"D\",\n" +
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

	private static final String RESPONSE_VALID_ADDRESS_W_GEO = "[\n" +
		"  {\n" +
		"    \"Input\": {\n" +
		"      \"Address\": \"\"\n" +
		"    },\n" +
		"    \"Matches\": [\n" +
		"      {\n" +
		"        \"AQI\": \"A\",\n" +
		"        \"AVC\": \"V44-I44-P6-100\",\n" +
		"        \"GeoAccuracy\": \"Z1\",\n" +
		"        \"Address\": \"My Valid Address\",\n" +
		"        \"Latitude\": \"-32.94217742803439\",\n" +
		"        \"Longitude\": \"-60.640132034941836\"\n" +
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
	public void testGetText() {
		ObjectNode node = new ObjectNode(null, new HashMap<>());
		node.set("text1", new TextNode("This,Is,Text"));
		node.set("text2", new TextNode("This Is-Text,"));
		node.set("text3", new TextNode("This Is-Text  with Invalid Formatting"));

		assertThat(myValidator.standardize(myValidator.getString(node, "text1"))).isEqualTo("This, Is, Text");
		assertThat(myValidator.standardize(myValidator.getString(node, "text2"))).isEqualTo("This Is-Text,");
		assertThat(myValidator.standardize(myValidator.getString(node, "text3"))).isEqualTo("This Is-Text, with Invalid Formatting");
	}

	@Test
	public void testEndpointOverride() {
		assertThat(myValidator.getApiEndpoint()).isEqualTo(LoquateAddressValidator.DEFAULT_DATA_CLEANSE_ENDPOINT);

		myProperties = new Properties();
		myProperties.setProperty(LoquateAddressValidator.PROPERTY_SERVICE_KEY, "MY_KEY");
		myProperties.setProperty(LoquateAddressValidator.PROPERTY_SERVICE_ENDPOINT, "HTTP://MY_ENDPOINT/LOQUATE");
		myValidator = new LoquateAddressValidator(myProperties);

		assertThat(myValidator.getApiEndpoint()).isEqualTo("HTTP://MY_ENDPOINT/LOQUATE");
	}

	@Test
	public void testInvalidInit() {
		try {
			new LoquateAddressValidator(new Properties());
			fail("");		} catch (Exception e) {
		}
	}

	@Test
	public void testInvalidAddressValidationResponse() throws Exception {
		try {
			AddressValidationResult res = myValidator.getValidationResult(new AddressValidationResult(),
				new ObjectMapper().readTree(RESPONSE_INVALID), ourCtx);
			fail("");		} catch (Exception e) {
		}
	}

	@Test
	public void testRequestBody() {
		try {
			assertThat(clear(myValidator.getRequestBody(ourCtx, getAddress()))).isEqualTo(clear(REQUEST));
		} catch (JsonProcessingException e) {
			fail("");		}
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
			fail("");		}

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
		assertThat(res.isValid()).isFalse();

		res = myValidator.getValidationResult(new AddressValidationResult(),
			new ObjectMapper().readTree(RESPONSE_VALID_ADDRESS), ourCtx);
		assertThat(res.isValid()).isTrue();
		assertThat(res.getValidatedAddressString()).isEqualTo("My Valid Address");
	}

	@Test
	public void testSuccessfulResponsesWithGeocodeAndQuality() throws Exception {
		myValidator.getProperties().setProperty(PROPERTY_GEOCODE, "true");
		AddressValidationResult res = myValidator.getValidationResult(new AddressValidationResult(),
			new ObjectMapper().readTree(RESPONSE_VALID_ADDRESS_W_GEO), ourCtx);
		assertThat(res.isValid()).isTrue();

		IBase address = res.getValidatedAddress();
		IBaseExtension geocode = ExtensionUtil.getExtensionByUrl(address, IAddressValidator.FHIR_GEOCODE_EXTENSION_URL);
		assertThat(geocode).isNotNull();
		assertThat(geocode.getExtension()).hasSize(2);
		assertThat(((IBaseExtension) geocode.getExtension().get(0)).getUrl()).isEqualTo("latitude");
		assertThat(((IBaseExtension) geocode.getExtension().get(1)).getUrl()).isEqualTo("longitude");

		IBaseExtension quality = ExtensionUtil.getExtensionByUrl(address, IAddressValidator.ADDRESS_QUALITY_EXTENSION_URL);
		assertThat(quality).isNotNull();
		assertThat(quality.getValue().toString()).isEqualTo("A");

		IBaseExtension verificationCode = ExtensionUtil.getExtensionByUrl(address, IAddressValidator.ADDRESS_VERIFICATION_CODE_EXTENSION_URL);
		assertThat(verificationCode).isNotNull();
		assertThat(verificationCode.getValue().toString()).isEqualTo("V44-I44-P6-100");

		IBaseExtension geoAccuracy = ExtensionUtil.getExtensionByUrl(address, IAddressValidator.ADDRESS_GEO_ACCURACY_EXTENSION_URL);
		assertThat(geoAccuracy).isNotNull();
		assertThat(geoAccuracy.getValue().toString()).isEqualTo("Z1");
	}

	@Test
	public void testErrorResponses() throws Exception {
		assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
			myValidator.getValidationResult(new AddressValidationResult(),
				new ObjectMapper().readTree(RESPONSE_INVALID_KEY), ourCtx);
		});
	}

}
