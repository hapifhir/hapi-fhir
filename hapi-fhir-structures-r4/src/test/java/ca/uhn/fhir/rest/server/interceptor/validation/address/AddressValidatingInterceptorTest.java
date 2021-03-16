package ca.uhn.fhir.rest.server.interceptor.validation.address;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.checkerframework.checker.units.qual.A;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.Address;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Properties;

import static ca.uhn.fhir.rest.server.interceptor.s13n.StandardizingInterceptor.STANDARDIZATION_DISABLED_HEADER;
import static ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidatingInterceptor.ADDRESS_VALIDATION_DISABLED_HEADER;
import static ca.uhn.fhir.rest.server.interceptor.validation.address.AddressValidatingInterceptor.PROPERTY_VALIDATOR_CLASS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AddressValidatingInterceptorTest {

	private static FhirContext ourCtx = FhirContext.forR4();

	private AddressValidatingInterceptor myInterceptor;

	private IAddressValidator myValidator;

	private RequestDetails myRequestDetails;

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
		assertValidated(address, "not-validated");
	}

	@Test
	void validate() {
		Address address = new Address();
		address.addLine("Line");
		address.setCity("City");

		myInterceptor.validateAddress(address, ourCtx);
		assertValidated(address, "invalid");
	}

	private void assertValidated(Address theAddress, String theValidationResult) {
		assertTrue(theAddress.hasExtension());
		assertEquals(1, theAddress.getExtension().size());
		assertEquals(IAddressValidator.ADDRESS_VALIDATION_EXTENSION_URL, theAddress.getExtensionFirstRep().getUrl());
		assertEquals(theValidationResult, theAddress.getExtensionFirstRep().getValueAsPrimitive().toString());
	}

	@Test
	void validateOnCreate() {
		Address address = new Address();
		address.addLine("Line");
		address.setCity("City");

		Person person = new Person();
		person.addAddress(address);

		myInterceptor.resourcePreCreate(myRequestDetails, person);

		assertValidated(person.getAddressFirstRep(), "invalid");
	}

	@Test
	void validateOnUpdate() {
		Address address = new Address();
		address.addLine("Line");
		address.setCity("City");
		address.addExtension(IAddressValidator.ADDRESS_VALIDATION_EXTENSION_URL, new StringType("..."));

		Address address2 = new Address();
		address2.addLine("Line 2");
		address2.setCity("City 2");

		Person person = new Person();
		person.addAddress(address);
		person.addAddress(address2);

		myInterceptor.resourcePreUpdate(myRequestDetails, null, person);

		verify(myValidator, times(1)).isValid(any(), any());
		assertValidated(person.getAddress().get(0), "...");
		assertValidated(person.getAddress().get(1), "invalid");
	}

	public static class TestAddressValidator implements IAddressValidator {
		@Override
		public AddressValidationResult isValid(IBase theAddress, FhirContext theFhirContext) throws AddressValidationException {
			return null;
		}
	}
}
