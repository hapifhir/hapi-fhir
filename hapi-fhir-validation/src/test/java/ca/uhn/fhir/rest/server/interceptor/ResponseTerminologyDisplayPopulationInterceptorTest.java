package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResponseTerminologyDisplayPopulationInterceptorTest {

	private final FhirContext myCtx = FhirContext.forCached(FhirVersionEnum.R4);
	@Order(0)
	@RegisterExtension
	protected RestfulServerExtension myServerExtension = new RestfulServerExtension(myCtx);
	@Order(1)
	@RegisterExtension
	protected HashMapResourceProviderExtension<Patient> myProviderPatientExtension = new HashMapResourceProviderExtension<>(myServerExtension, Patient.class);
	private IGenericClient myClient;

	@BeforeEach
	public void beforeEach() {
		myClient = myServerExtension.getFhirClient();
	}

	@AfterEach
	public void afterEach() {
		myServerExtension.getRestfulServer().getInterceptorService().unregisterAllInterceptors();
	}

	@Test
	public void testPopulateCoding_Read() {
		myServerExtension.getRestfulServer().registerInterceptor(new ResponseTerminologyDisplayPopulationInterceptor(myCtx.getValidationSupport()));

		Patient p = new Patient();
		p.getMaritalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus").setCode("A");
		IIdType id = myClient.create().resource(p).execute().getId();

		p = myClient.read().resource(Patient.class).withId(id).execute();
		assertEquals(1, p.getMaritalStatus().getCoding().size());
		assertEquals("Annulled", p.getMaritalStatus().getCoding().get(0).getDisplay());
	}

	@Test
	public void testPopulateCoding_Search() {
		myServerExtension.getRestfulServer().registerInterceptor(new ResponseTerminologyDisplayPopulationInterceptor(myCtx.getValidationSupport()));

		Patient p = new Patient();
		p.getMaritalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus").setCode("A");
		IIdType id = myClient.create().resource(p).execute().getId();

		Bundle bundle = myClient.search().forResource(Patient.class).returnBundle(Bundle.class).execute();
		assertEquals(1, bundle.getEntry().size());
		p = (Patient) bundle.getEntry().get(0).getResource();
		assertEquals(1, p.getMaritalStatus().getCoding().size());
		assertEquals("Annulled", p.getMaritalStatus().getCoding().get(0).getDisplay());
	}

	@Test
	public void testDontPopulateCodingIfAlreadyPopulated() {
		myServerExtension.getRestfulServer().registerInterceptor(new ResponseTerminologyDisplayPopulationInterceptor(myCtx.getValidationSupport()));

		Patient p = new Patient();
		p.getMaritalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus").setCode("A").setDisplay("FOO");
		IIdType id = myClient.create().resource(p).execute().getId();

		p = myClient.read().resource(Patient.class).withId(id).execute();
		assertEquals(1, p.getMaritalStatus().getCoding().size());
		assertEquals("FOO", p.getMaritalStatus().getCoding().get(0).getDisplay());
	}

	@Test
	public void testDontPopulateCodingIfNoneFound() {
		myServerExtension.getRestfulServer().registerInterceptor(new ResponseTerminologyDisplayPopulationInterceptor(myCtx.getValidationSupport()));

		Patient p = new Patient();
		p.getMaritalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus").setCode("ZZZZZZ");
		IIdType id = myClient.create().resource(p).execute().getId();

		p = myClient.read().resource(Patient.class).withId(id).execute();
		assertEquals(1, p.getMaritalStatus().getCoding().size());
		assertEquals(null, p.getMaritalStatus().getCoding().get(0).getDisplay());
	}

}
