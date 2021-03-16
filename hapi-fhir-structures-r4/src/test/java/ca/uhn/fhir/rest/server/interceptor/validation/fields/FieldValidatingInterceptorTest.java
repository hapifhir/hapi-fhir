package ca.uhn.fhir.rest.server.interceptor.validation.fields;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static ca.uhn.fhir.rest.server.interceptor.s13n.StandardizingInterceptor.STANDARDIZATION_DISABLED_HEADER;
import static ca.uhn.fhir.rest.server.interceptor.validation.fields.FieldValidatingInterceptor.VALIDATION_DISABLED_HEADER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FieldValidatingInterceptorTest {

	private FhirContext myFhirContext = FhirContext.forR4();
	private FieldValidatingInterceptor myInterceptor = new FieldValidatingInterceptor();

	public RequestDetails newRequestDetails() {
		RequestDetails requestDetails = mock(RequestDetails.class);
		when(requestDetails.getFhirContext()).thenReturn(myFhirContext);
		return requestDetails;
	}

	@BeforeEach
	public void init() throws Exception {
		myInterceptor = new FieldValidatingInterceptor();
	}

	@Test
	public void testDisablingValidationViaHeader() {
		RequestDetails request = newRequestDetails();
		when(request.getHeaders(eq(VALIDATION_DISABLED_HEADER))).thenReturn(Arrays.asList(new String[]{"True"}));

		Person person = new Person();
		person.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL).setValue("EMAIL");

		myInterceptor.handleRequest(request, person);
		assertEquals("EMAIL", person.getTelecom().get(0).getValue());
	}

	@Test
	public void testEmailValidation() {
		Person person = new Person();
		person.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL).setValue("email@email.com");

		try {
			myInterceptor.handleRequest(newRequestDetails(), person);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testInvalidEmailValidation() {
		Person person = new Person();
		person.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL).setValue("@garbage");

		try {
			myInterceptor.handleRequest(newRequestDetails(), person);
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void testCustomInvalidValidation() {
		myInterceptor.getConfig().put("telecom.where(system='phone').value", "ClassThatDoesntExist");
		try {
			myInterceptor.handleRequest(newRequestDetails(), new Person());
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void testCustomValidation() {
		myInterceptor.getConfig().put("telecom.where(system='phone').value", EmptyValidator.class.getName());

		Person person = new Person();
		person.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL).setValue("email@email.com");

		try {
			myInterceptor.handleRequest(newRequestDetails(), person);
		} catch (Exception e) {
			fail();
		}

		person.addTelecom().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue("123456");
		try {
			myInterceptor.handleRequest(newRequestDetails(), person);
		} catch (Exception e) {
			fail();
		}

		person = new Person();
		person.addTelecom().setSystem(ContactPoint.ContactPointSystem.PHONE).setValue(" ");
		try {
			myInterceptor.handleRequest(newRequestDetails(), person);
			fail();
		} catch (Exception e) {
		}
	}

	public static class EmptyValidator implements IValidator {
		@Override
		public boolean isValid(String theString) {
			return !StringUtils.isBlank(theString);
		}
	}

}
