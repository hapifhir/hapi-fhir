package ca.uhn.fhir.rest.server.interceptor.s13n.interceptors;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import ca.uhn.fhir.rest.server.interceptor.s13n.StandardizingInterceptor;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.r4.model.ContactPoint;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import static ca.uhn.fhir.rest.server.interceptor.s13n.StandardizingInterceptor.STANDARDIZATION_DISABLED_HEADER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class StandardizingInterceptorTest {

	private static final String CONFIG =
		"{\n" +
			"\t\"Person\" : {\n" +
			"\t\t\"Person.name.family\" : \"NAME_FAMILY\",\n" +
			"\t\t\"Person.name.given\" : \"NAME_GIVEN\",\n" +
			"\t\t\"Person.telecom.where(system='phone').value\" : \"PHONE\"\n" +
			"\t\t},\n" +
			"\t\"Patient\" : {\n" +
			"\t\t\"name.given\" : \"NAME_GIVEN\",\n" +
			"\t\t\"telecom.where(system='phone').value\" : \"PHONE\"\n" +
			"\t\t}\n" +
			"}";
	
	private static final String BAD_CONFIG = "{ \"Person\" : { \"Person.name.family\" : \"org.nonexistent.Standardizer\"}}";

	private static FhirContext ourCtx = FhirContext.forR4();

	private RequestDetails myRequestDetails;

	private StandardizingInterceptor myInterceptor = new StandardizingInterceptor();

	@BeforeEach
	public void init() throws Exception {
		myInterceptor = new StandardizingInterceptor(new ObjectMapper().readValue(CONFIG, Map.class));

		myRequestDetails = mock(RequestDetails.class);
		when(myRequestDetails.getFhirContext()).thenReturn(ourCtx);
	}

	@Test
	public void testNameStandardization() throws Exception {
		Person p = new Person();
		p.addName().setFamily("macdouglas1").addGiven("\nJoHn");
		p.addName().setFamily("o'BrIaN").addGiven("jIM\t");

		myInterceptor.resourcePreUpdate(myRequestDetails, null, p);

		assertEquals("John MacDouglas1", p.getName().get(0).getNameAsSingleString());
		assertEquals("Jim O'Brian", p.getName().get(1).getNameAsSingleString());
	}

	@Test
	public void testNullsWork() {
		try {
			myInterceptor.resourcePreCreate(myRequestDetails, null);
		} catch (Exception e) {
			fail();
		}
	}

	@Test
	public void testBadConfig() throws Exception {
		myInterceptor = new StandardizingInterceptor(new ObjectMapper().readValue(BAD_CONFIG, Map.class));

		try {
			myInterceptor.resourcePreCreate(myRequestDetails, new Person());
			fail();
		} catch (Exception e) {
		}
	}

	@Test
	public void testDisablingValidationViaHeader() {
		when(myRequestDetails.getHeaders(eq(STANDARDIZATION_DISABLED_HEADER))).thenReturn(Arrays.asList(new String[]{"True"}));

		Person p = new Person();
		p.addName().setFamily("non'normalized").addGiven("name");

		myInterceptor.resourcePreUpdate(myRequestDetails, null, p);

		assertEquals("name non'normalized", p.getName().get(0).getNameAsSingleString());
	}

	@Test
	public void testTelecomStandardization() throws Exception {
		Person p = new Person();
		p.addTelecom().setValue(" Email@email.com").setSystem(ContactPoint.ContactPointSystem.EMAIL);
		p.addTelecom().setValue("1234567890").setSystem(ContactPoint.ContactPointSystem.PHONE);

		myInterceptor.resourcePreUpdate(myRequestDetails, null, p);

		assertEquals(" Email@email.com", p.getTelecom().get(0).getValue(), "Expected email to remain the same");
		assertEquals("123-456-7890", p.getTelecom().get(1).getValue());
	}
}
