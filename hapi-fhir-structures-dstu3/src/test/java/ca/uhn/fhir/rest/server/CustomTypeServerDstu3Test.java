package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomTypeServerDstu3Test {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();

	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new PatientProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);


	@Test
	public void testCreateWithIdInBody() {

		Patient patient = new Patient();
		patient.setId("2");
		patient.addIdentifier().setValue("002");

		ourServer.fhirRequest("/Patient").post(patient).assertStatus(201);
	}

	@Test
	public void testCreateWithIdInUrl() {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpTestResponse response = ourServer.fhirRequest("/Patient/2")
			 .post(patient)
			 .assertStatus(400);

		String responseContent = response.getBody();
		OperationOutcome oo = ourCtx.newJsonParser().parseResource(OperationOutcome.class, responseContent);
		assertEquals("HAPI-0365: Can not create resource with ID \"2\", ID must not be supplied on a create (POST) operation (use an HTTP PUT / update operation if you wish to supply an ID)", oo.getIssue().get(0).getDiagnostics());
	}

	@Test
	public void testCreateWithIdInUrlForConditional() {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpTestResponse response = ourServer.fhirRequest("/Patient/2")
			 .withHeader(Constants.HEADER_IF_NONE_EXIST, "Patient?identifier=system%7C001")
			 .post(patient)
			 .assertStatus(400);

		String responseContent = response.getBody();
		OperationOutcome oo = ourCtx.newJsonParser().parseResource(OperationOutcome.class, responseContent);
		assertEquals("HAPI-0365: Can not create resource with ID \"2\", ID must not be supplied on a create (POST) operation (use an HTTP PUT / update operation if you wish to supply an ID)", oo.getIssue().get(0).getDiagnostics());
	}


	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class PatientProvider implements IResourceProvider {

		@Create()
		public MethodOutcome createPatient(@ResourceParam Patient thePatient, @ConditionalUrlParam String theConditional, @IdParam IdType theIdParam) {
			return new MethodOutcome(new IdType("Patient/001/_history/002"));
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Search
		public List<IResource> search(@OptionalParam(name = "foo") StringDt theString) {
			return new ArrayList<>();
		}
	}
}
