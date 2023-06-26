package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CaptureResourceSourceFromHeaderInterceptorTest {

	private static FhirContext ourCtx = FhirContext.forR4();
	@RegisterExtension
	public static RestfulServerExtension ourServerRule = new RestfulServerExtension(ourCtx);
	private CaptureResourceSourceFromHeaderInterceptor myInterceptor;
	@RegisterExtension
	public HashMapResourceProviderExtension<Patient> myPatientProviderRule = new HashMapResourceProviderExtension<>(ourServerRule, Patient.class);

	@BeforeEach
	public void before() {
		myInterceptor = new CaptureResourceSourceFromHeaderInterceptor(ourCtx);
		ourServerRule.getRestfulServer().registerInterceptor(myInterceptor);
	}

	@AfterEach
	public void after() {
		ourServerRule.getRestfulServer().unregisterInterceptor(myInterceptor);
	}

	@Test
	public void testCreateWithoutAnything() {
		Patient resource = new Patient();
		resource.setActive(true);

		ourServerRule.getFhirClient().create().resource(resource).execute();

		Patient stored = myPatientProviderRule.getStoredResources().get(0);
		assertNull(stored.getMeta().getSource());
	}

	@Test
	public void testCreateWithSource() {
		Patient resource = new Patient();
		resource.setActive(true);
		resource.getMeta().setSource("http://source");

		ourServerRule.getFhirClient().create().resource(resource).execute();

		Patient stored = myPatientProviderRule.getStoredResources().get(0);
		assertEquals("http://source", stored.getMeta().getSource());
	}

	@Test
	public void testCreateWithHeader() {
		Patient resource = new Patient();
		resource.setActive(true);

		ourServerRule
			.getFhirClient()
			.create()
			.resource(resource)
			.withAdditionalHeader(Constants.HEADER_REQUEST_SOURCE, "http://header")
			.execute();

		Patient stored = myPatientProviderRule.getStoredResources().get(0);
		assertEquals("http://header", stored.getMeta().getSource());
	}

	@Test
	public void testCreateWithBoth() {
		Patient resource = new Patient();
		resource.setActive(true);
		resource.getMeta().setSource("http://source");

		ourServerRule
			.getFhirClient()
			.create()
			.resource(resource)
			.withAdditionalHeader(Constants.HEADER_REQUEST_SOURCE, "http://header")
			.execute();

		Patient stored = myPatientProviderRule.getStoredResources().get(0);
		assertEquals("http://header", stored.getMeta().getSource());
	}

	@Test
	public void testNonCreateShouldntFail() {
		Bundle bundle = ourServerRule
			.getFhirClient()
			.search()
			.forResource(Patient.class)
			.returnBundle(Bundle.class)
			.execute();
		assertEquals(0, bundle.getEntry().size());
	}

}
