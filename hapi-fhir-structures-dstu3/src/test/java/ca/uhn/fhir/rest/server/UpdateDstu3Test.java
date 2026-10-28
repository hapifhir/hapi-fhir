package ca.uhn.fhir.rest.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.InstantType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateDstu3Test {
	private static String ourConditionalUrl;
	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	@RegisterExtension
	private static RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		.registerProvider(new PatientProvider());
	private static IdType ourId;
	private static InstantType ourSetLastUpdated;

	@BeforeEach
	public void before() {
		ourConditionalUrl = null;
		ourId = null;
		ourSetLastUpdated = null;
	}

	@Test
	public void testUpdateReturnsETagAndUpdate() {

		Patient patient = new Patient();
		patient.setId("123");
		patient.addIdentifier().setValue("002");
		ourSetLastUpdated = new InstantType("2002-04-22T11:22:33.022Z");

		HttpTestResponse response = ourServer.fhirRequest("/Patient/123")
				.put(ourCtx.newXmlParser().encodeResourceToString(patient), Constants.CT_FHIR_XML)
				.assertStatus(200)
				.assertMimeType(Constants.CT_FHIR_XML);

		assertThat(response.getBody()).isNotEmpty();

		Patient actualPatient = response.getBodyAsResource(Patient.class);
		assertEquals(patient.getIdElement().getIdPart(), actualPatient.getIdElement().getIdPart());
		assertEquals(patient.getIdentifier().get(0).getValue(), actualPatient.getIdentifier().get(0).getValue());

		assertThat(response.getHeader("location")).isNull();
		response.assertHeader(Constants.HEADER_CONTENT_LOCATION, ourServer.getBaseUrl() + "/Patient/123/_history/002");
		response.assertHeader(Constants.HEADER_ETAG_LC, "W/\"002\"");
		response.assertHeader(Constants.HEADER_LAST_MODIFIED_LOWERCASE, "Mon, 22 Apr 2002 11:22:33 GMT");

	}

	@Test
	public void testUpdateConditional() {

		Patient patient = new Patient();
		patient.setId("001");
		patient.addIdentifier().setValue("002");

		String responseContent = ourServer.fhirRequest("/Patient?_id=001")
			.put(ourCtx.newXmlParser().encodeResourceToString(patient), Constants.CT_FHIR_XML)
			.assertStatus(200)
			.getBody();

		assertThat(ourConditionalUrl).isEqualTo("Patient?_id=001");
		assertThat(ourId).isNull();

	}

	@Test
	public void testUpdateMissingIdInBody() {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		OperationOutcome oo = ourServer.fhirRequest("/Patient/001")
				.put(ourCtx.newXmlParser().encodeResourceToString(patient), Constants.CT_FHIR_XML)
				.assertStatus(400)
				.assertMimeType(Constants.CT_FHIR_XML)
				.getBodyAsResource(OperationOutcome.class);
		assertEquals(Msg.code(419) + "Can not update resource, resource body must contain an ID element for update (PUT) operation", oo.getIssue().get(0).getDiagnostics());
	}

	@Test
	public void testUpdateNormal() {

		Patient patient = new Patient();
		patient.setId("001");
		patient.addIdentifier().setValue("002");

		String responseContent = ourServer.fhirRequest("/Patient/001")
			.put(ourCtx.newXmlParser().encodeResourceToString(patient), Constants.CT_FHIR_XML)
			.assertStatus(200)
			.getBody();

		assertThat(ourConditionalUrl).isNull();
		assertThat(ourId.getValue()).isEqualTo("Patient/001");

	}

	@Test
	public void testUpdateWrongIdInBody() {

		Patient patient = new Patient();
		patient.setId("Patient/3/_history/4");
		patient.addIdentifier().setValue("002");

		ourServer.fhirRequest("/Patient/1/_history/2")
				.put(ourCtx.newXmlParser().encodeResourceToString(patient), Constants.CT_FHIR_XML)
				.assertStatus(400)
				.assertBodyContains("Resource body ID of &quot;3&quot; does not match");
	}

	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Update()
		public MethodOutcome updatePatient(@IdParam IdType theId, @ResourceParam Patient thePatient, @ConditionalUrlParam String theConditionalUrl) {
			ourId = theId;
			ourConditionalUrl = theConditionalUrl;
			IdType id = theId != null ? theId.withVersion(thePatient.getIdentifierFirstRep().getValue()) : new IdType("Patient/1");
			OperationOutcome oo = new OperationOutcome();
			oo.addIssue().setDiagnostics("OODETAILS");
			if (id.getValueAsString().contains("CREATE")) {
				return new MethodOutcome(id, oo, true);
			}

			thePatient.getMeta().setLastUpdatedElement(ourSetLastUpdated);

			MethodOutcome retVal = new MethodOutcome(id, oo);
			retVal.setResource(thePatient);
			return retVal;
		}

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}
}
