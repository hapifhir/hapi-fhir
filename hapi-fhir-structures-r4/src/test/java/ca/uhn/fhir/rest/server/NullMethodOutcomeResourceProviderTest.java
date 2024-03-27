package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Patch;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.PatchTypeEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.apache.http.HttpStatus;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class NullMethodOutcomeResourceProviderTest {

	public static final String TEST_PATIENT_ID = "Patient/123";

	@RegisterExtension
	private RestfulServerExtension myServer = new RestfulServerExtension(FhirVersionEnum.R4)
		 .registerProvider(new NullMethodOutcomePatientProvider());

	private IGenericClient myClient;
	private Patient myPatient;

	@BeforeEach
	public void before() {
		myPatient = new Patient();
		myClient = myServer.getFhirClient();
	}

	@Test
	public void testCreate_withNullMethodOutcome_throwsException() {
		try {
			myClient.create().resource(myPatient).execute();
			fail();
		} catch (InternalErrorException e){
			assertTrue(e.getMessage().contains("HTTP 500 Server Error: HAPI-0368"));
		}
	}

	@Test
	public void testUpdate_withNullMethodOutcome_returnsHttp200() {
		myPatient.setId(TEST_PATIENT_ID);
		MethodOutcome outcome = myClient.update().resource(myPatient).execute();
		assertEquals(HttpStatus.SC_OK, outcome.getResponseStatusCode());
	}

	@Test
	public void testPatch_withNullMethodOutcome_returnsHttp200() {
		MethodOutcome outcome = myClient.patch().withFhirPatch(new Parameters()).withId(TEST_PATIENT_ID).execute();
		assertEquals(HttpStatus.SC_OK, outcome.getResponseStatusCode());
	}

	@Test
	public void testValidate_withNullMethodOutcome_throwsException() {
		try {
			myClient.validate().resource(myPatient).execute();
			fail();
		} catch (ResourceNotFoundException e){
			// This fails with HAPI-0436 because the MethodOutcome of the @Validate method is used
			// to build an IBundleProvider with a OperationOutcome resource (which will be null from the provider below).
			// See OperationMethodBinding#invokeServer()
			assertTrue(e.getMessage().contains("HTTP 404 Not Found: HAPI-0436"));
		}
	}

	@Test
	public void testDelete_withNullMethodOutcome_throwsException() {
		try {
			myPatient.setId(TEST_PATIENT_ID);
			myClient.delete().resource(myPatient).execute();
			fail();
		} catch (InternalErrorException e){
			assertTrue(e.getMessage().contains("HTTP 500 Server Error: HAPI-0368"));
		}
	}

	public static class NullMethodOutcomePatientProvider implements IResourceProvider {

		@Create
		public MethodOutcome create(@ResourceParam Patient thePatient) {
			return null;
		}

		@Update
		public MethodOutcome update(@IdParam IdType theId, @ResourceParam Patient thePatient) {
			return null;
		}

		@Patch
		public MethodOutcome patch(@IdParam IdType theId, @ResourceParam String theBody, PatchTypeEnum thePatchType){
			return null;
		}

		@Delete
		public MethodOutcome delete(@IdParam IdType theId) {
			return null;
		}

		@Validate
		public MethodOutcome validate(@ResourceParam Patient thePatient) {
			return null;
		}

		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}
	}
}
