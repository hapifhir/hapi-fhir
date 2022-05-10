package ca.uhn.fhir.rest.server.interceptor.validation.address;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.validation.address.impl.LoquateAddressValidator;
import org.checkerframework.checker.units.qual.A;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

import static ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidatingInterceptor.ADDRESS_VALIDATION_DISABLED_HEADER;
import static ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidatingInterceptor.PROPERTY_EXTENSION_URL;
import static ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidatingInterceptor.PROPERTY_VALIDATOR_CLASS;
import static ca.uhn.fhir.rest.server.interceptor.validation.address.IAddressValidator.ADDRESS_VALIDATION_EXTENSION_URL;
import static ca.uhn.fhir.rest.server.interceptor.validation.address.impl.BaseRestfulValidator.PROPERTY_SERVICE_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AddressValidatingInterceptorTest {

	private static FhirContext ourCtx = FhirContext.forR4();

	private AddressValidatingInterceptor myInterceptor;

	private IAddressValidator myValidator;

	private RequestDetails myRequestDetails;

	@Test
	@Disabled
	public void testValidationCallAgainstLiveLoquateEndpoint() {
		Properties config = new Properties();
		config.setProperty(PROPERTY_VALIDATOR_CLASS, LoquateAddressValidator.class.getCanonicalName());
		config.setProperty(PROPERTY_SERVICE_KEY, "KR26-JA29-HB16-PA11"); // Replace with a real key when testing
		AddressValidatingInterceptor interceptor = new AddressValidatingInterceptor(config);

		Address address = new Address();
		address.setUse(Address.AddressUse.WORK);
		address.addLine("100 Somewhere");
		address.setCity("Burloak");
		address.setPostalCode("A0A0A0");
		address.setCountry("Canada");
		interceptor.validateAddress(address, ourCtx);

		assertTrue(address.hasExtension());
		assertEquals("true", address.getExtensionFirstRep().getValueAsPrimitive().getValueAsString());
		assertEquals("E",
			address.getExtensionByUrl(IAddressValidator.ADDRESS_QUALITY_EXTENSION_URL).getValueAsPrimitive().getValueAsString());

		assertEquals("100 Somewhere, Burloak", address.getText());
		assertEquals(1, address.getLine().size());
		assertEquals("100 Somewhere", address.getLine().get(0).getValueAsString());
		assertEquals("Burloak", address.getCity());
		assertEquals("A0A0A0", address.getPostalCode());
		assertEquals("Canada", address.getCountry());
	}

	@Test
	void start() throws Exception {
		AddressValidatingInterceptor interceptor = new AddressValidatingInterceptor(new Properties());
		assertNull(interceptor.getAddressValidator());

		Properties props = new Properties();
		props.setProperty(PROPERTY_VALIDATOR_CLASS, "RandomService");
		try {
			new AddressValidatingInterceptor(props);
			fail();
		} catch (Exception e) {
			// expected
		}

		props.setProperty(PROPERTY_VALIDATOR_CLASS, TestAddressValidator.class.getName());
		interceptor = new AddressValidatingInterceptor(props);
		assertNotNull(interceptor.getAddressValidator());
	}

	@Test
	public void testEmptyRequest() {
		try {
			myInterceptor.handleRequest(null, null);
		} catch (Exception ex) {
			fail();
		}

		try {
			myInterceptor.setAddressValidator(null);
			myInterceptor.handleRequest(null, null);
		} catch (Exception ex) {
			fail();
		}
	}

	@BeforeEach
	void setup() {
		myValidator = mock(IAddressValidator.class);
		when(myValidator.isValid(any(), any())).thenReturn(mock(AddressValidationResult.class));

		myRequestDetails = mock(RequestDetails.class);
		when(myRequestDetails.getFhirContext()).thenReturn(ourCtx);

		Properties properties = getProperties();
		myInterceptor = new AddressValidatingInterceptor(properties);
		myInterceptor.setAddressValidator(myValidator);
	}

	@Nonnull
	private Properties getProperties() {
		Properties properties = new Properties();
		properties.setProperty(PROPERTY_VALIDATOR_CLASS, TestAddressValidator.class.getName());
		return properties;
	}

	@Test
	public void testDisablingValidationViaHeader() {
		when(myRequestDetails.getHeaders(eq(ADDRESS_VALIDATION_DISABLED_HEADER))).thenReturn(Arrays.asList(new String[]{"True"}));

		Person p = new Person();
		AddressValidatingInterceptor spy = Mockito.spy(myInterceptor);
		spy.resourcePreCreate(myRequestDetails, p);

		Mockito.verify(spy, times(0)).validateAddress(any(), any());
	}

	@Test
	public void testValidationServiceError() {
		myValidator = mock(IAddressValidator.class);
		when(myValidator.isValid(any(), any())).thenThrow(new RuntimeException());
		myInterceptor.setAddressValidator(myValidator);

		Address address = new Address();
		myInterceptor.validateAddress(address, ourCtx);
		Extension ext = assertValidationErrorExtension(address);
		assertTrue(ext.hasExtension());
		assertEquals("error", ext.getExtensionFirstRep().getUrl());
	}

	@Test
	public void testValidationWithCustomUrl() {
		myInterceptor.getProperties().setProperty(PROPERTY_EXTENSION_URL, "MY_URL");
		Address address = new Address();
		address.setCity("City");
		address.addLine("Line");
		AddressValidationResult res = new AddressValidationResult();
		res.setValidatedAddressString("City, Line");
		res.setValidatedAddress(address);
		when(myValidator.isValid(any(), any())).thenReturn(res);

		Address addressToValidate = new Address();
		myInterceptor.validateAddress(addressToValidate, ourCtx);

		assertNotNull(res.toString());
		assertTrue(addressToValidate.hasExtension());
		assertNotNull(addressToValidate.getExtensionByUrl("MY_URL"));
		assertFalse(address.hasExtension());
		assertEquals(address.getCity(), addressToValidate.getCity());
		assertTrue(address.getLine().get(0).equalsDeep(addressToValidate.getLine().get(0)));
	}

	@Test
	void validate() {
		Address address = new Address();
		address.addLine("Line");
		address.setCity("City");

		myInterceptor.validateAddress(address, ourCtx);
		assertValidationErrorValue(address, "true");
	}

	private Extension assertValidationErrorExtension(Address theAddress) {
		assertTrue(theAddress.hasExtension());
		assertEquals(1, theAddress.getExtension().size());
		assertEquals(IAddressValidator.ADDRESS_VALIDATION_EXTENSION_URL, theAddress.getExtensionFirstRep().getUrl());
		return theAddress.getExtensionFirstRep();
	}

	private void assertValidationErrorValue(Address theAddress, String theValidationResult) {
		Extension ext = assertValidationErrorExtension(theAddress);
		assertEquals(theValidationResult, ext.getValueAsPrimitive().getValueAsString());
	}

	@Test
	void validateOnCreate() {
		Address address = new Address();
		address.addLine("Line");
		address.setCity("City");

		Person person = new Person();
		person.addAddress(address);

		myInterceptor.resourcePreCreate(myRequestDetails, person);

		assertValidationErrorValue(person.getAddressFirstRep(), "true");
	}

	@Test
	void validateOnUpdate() {
		Address validAddress = new Address();
		validAddress.addLine("Line");
		validAddress.setCity("City");
		validAddress.addExtension(IAddressValidator.ADDRESS_VALIDATION_EXTENSION_URL, new StringType("false"));

		Address notValidatedAddress = new Address();
		notValidatedAddress.addLine("Line 2");
		notValidatedAddress.setCity("City 2");

		Person person = new Person();
		person.addAddress(validAddress);
		person.addAddress(notValidatedAddress);

		myInterceptor.resourcePreUpdate(myRequestDetails, null, person);

		verify(myValidator, times(1)).isValid(any(), any());
		assertValidationErrorValue(person.getAddress().get(0), "false");
		assertValidationErrorValue(person.getAddress().get(1), "true");
	}

	@Test
	void validateOnValidInvalid() {
		Address address = new Address();
		address.addLine("Line");
		address.setCity("City");

		Person person = new Person();
		person.addAddress(address);

		AddressValidationResult validationResult = new AddressValidationResult();
		validationResult.setValid(true);
		when(myValidator.isValid(eq(address), any())).thenReturn(validationResult);
		myInterceptor.resourcePreUpdate(myRequestDetails, null, person);

		assertValidationErrorValue(person.getAddress().get(0), "false");

		when(myValidator.isValid(eq(address), any())).thenThrow(new RuntimeException());

		myInterceptor.resourcePreUpdate(myRequestDetails, null, person);

		Extension ext = assertValidationErrorExtension(address);
		assertNotNull(ext);
		assertNull(ext.getValue());
		assertTrue(ext.hasExtension());

	}

	public static class TestAddressValidator implements IAddressValidator {
		@Override
		public AddressValidationResult isValid(IBase theAddress, FhirContext theFhirContext) throws AddressValidationException {
			return null;
		}
	}
}
