package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.fhirpath.BaseValidationTestWithInlineMocks;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.test.utilities.server.HashMapResourceProviderExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import jakarta.servlet.http.HttpServletResponse;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.C;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ResponseTerminologyDisplayPopulationInterceptorTest extends BaseValidationTestWithInlineMocks {

	private final FhirContext myCtx = FhirContext.forR4Cached();
	@Order(0)
	@RegisterExtension
	protected RestfulServerExtension myServerExtension = new RestfulServerExtension(myCtx)
		.registerProvider(new FakeCodeSystemLookupProvider());

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
		assertThat(p.getMaritalStatus().getCoding()).hasSize(1);
		assertEquals("Annulled", p.getMaritalStatus().getCoding().get(0).getDisplay());
	}

	@Test
	public void testPopulateCoding_Operation() {
		myServerExtension.getRestfulServer().registerInterceptor(new ResponseTerminologyDisplayPopulationInterceptor(myCtx.getValidationSupport()));

		// Test
		Parameters outcome = myClient
			.operation()
			.onType("CodeSystem")
			.named(JpaConstants.OPERATION_LOOKUP)
			.withNoParameters(Parameters.class)
			.execute();

		// Verify
		Coding value = (Coding) outcome.getParameter("coding").getValue();
		assertEquals("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus", value.getSystem());
		assertEquals("A", value.getCode());
		assertEquals("Annulled", value.getDisplay());
	}

	@Test
	public void testDontPopulateCodingIfLookupReturnsNull_Read() {
		myServerExtension.getRestfulServer().registerInterceptor(new ResponseTerminologyDisplayPopulationInterceptor(new NullableValidationSupport(myCtx)));

		Patient p = new Patient();
		p.getMaritalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus").setCode("zz");
		IIdType id = myClient.create().resource(p).execute().getId();

		p = myClient.read().resource(Patient.class).withId(id).execute();
		assertThat(p.getMaritalStatus().getCoding()).hasSize(1);
		assertNull(p.getMaritalStatus().getCoding().get(0).getDisplay());
	}

	@Test
	public void testPopulateCoding_Search() {
		myServerExtension.getRestfulServer().registerInterceptor(new ResponseTerminologyDisplayPopulationInterceptor(myCtx.getValidationSupport()));

		Patient p = new Patient();
		p.getMaritalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus").setCode("A");
		myClient.create().resource(p).execute();

		Bundle bundle = myClient.search().forResource(Patient.class).returnBundle(Bundle.class).execute();
		assertThat(bundle.getEntry()).hasSize(1);
		p = (Patient) bundle.getEntry().get(0).getResource();
		assertThat(p.getMaritalStatus().getCoding()).hasSize(1);
		assertEquals("Annulled", p.getMaritalStatus().getCoding().get(0).getDisplay());
	}

	@Test
	public void testDontPopulateCodingIfLookupReturnsNull_Search() {
		myServerExtension.getRestfulServer().registerInterceptor(new ResponseTerminologyDisplayPopulationInterceptor(new NullableValidationSupport(myCtx)));

		Patient p = new Patient();
		p.getMaritalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus").setCode("zz");
		myClient.create().resource(p).execute();

		Bundle bundle = myClient.search().forResource(Patient.class).returnBundle(Bundle.class).execute();
		assertThat(bundle.getEntry()).hasSize(1);
		p = (Patient) bundle.getEntry().get(0).getResource();
		assertThat(p.getMaritalStatus().getCoding()).hasSize(1);
		assertNull(p.getMaritalStatus().getCoding().get(0).getDisplay());
	}

	@Test
	public void testDontPopulateCodingIfAlreadyPopulated() {
		myServerExtension.getRestfulServer().registerInterceptor(new ResponseTerminologyDisplayPopulationInterceptor(myCtx.getValidationSupport()));

		Patient p = new Patient();
		p.getMaritalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus").setCode("A").setDisplay("FOO");
		IIdType id = myClient.create().resource(p).execute().getId();

		p = myClient.read().resource(Patient.class).withId(id).execute();
		assertThat(p.getMaritalStatus().getCoding()).hasSize(1);
		assertEquals("FOO", p.getMaritalStatus().getCoding().get(0).getDisplay());
	}

	@Test
	public void testDontPopulateCodingIfNoneFound() {
		myServerExtension.getRestfulServer().registerInterceptor(new ResponseTerminologyDisplayPopulationInterceptor(myCtx.getValidationSupport()));

		Patient p = new Patient();
		p.getMaritalStatus().addCoding().setSystem("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus").setCode("ZZZZZZ");
		IIdType id = myClient.create().resource(p).execute().getId();

		p = myClient.read().resource(Patient.class).withId(id).execute();
		assertThat(p.getMaritalStatus().getCoding()).hasSize(1);
		assertNull(p.getMaritalStatus().getCoding().get(0).getDisplay());
	}
	
	@Test
	public void testDontPopulateCodingIfNoneFound_Operation() {
		myServerExtension.getRestfulServer().registerInterceptor(new ResponseTerminologyDisplayPopulationInterceptor(myCtx.getValidationSupport()));
		
		// Test
		Parameters outcome = myClient
			.operation()
			.onType("Patient")
			.named("$operation-with-no-response")
			.withNoParameters(Parameters.class)
			.execute();
	}

	private static class NullableValidationSupport implements IValidationSupport {

		private static FhirContext myStaticCtx;

		NullableValidationSupport(FhirContext theCtx) {
			myStaticCtx = theCtx;
		}

		@Override
		public FhirContext getFhirContext() {
			return myStaticCtx;
		}

		@Override
		public boolean isCodeSystemSupported(ValidationSupportContext theValidationSupportContext, String theSystem) {
			return true;
		}
	}


	public static class FakeCodeSystemLookupProvider {

		@Operation(name = JpaConstants.OPERATION_LOOKUP, typeName = "CodeSystem")
		public Parameters lookup() {
			Parameters retVal = new Parameters();
			retVal.addParameter("coding", new Coding("http://terminology.hl7.org/CodeSystem/v3-MaritalStatus", "A", null));
			return retVal;
		}

		@Operation(name = "$operation-with-no-response", typeName = "Patient", manualResponse = true)
		public void operationWithNoResponse(HttpServletResponse theServletResponse) {
			theServletResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
		}

	}

}
