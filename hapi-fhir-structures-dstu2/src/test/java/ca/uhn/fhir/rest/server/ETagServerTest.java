package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.test.utilities.HttpTestResponse;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ETagServerTest {

	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();
	private static IdDt ourLastId;

	private static Date ourLastModifiedDate;

	@RegisterExtension
	public static final RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.registerProvider(new PatientProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(100))
		.setDefaultPrettyPrint(false);

	@BeforeEach
	public void before() {
		ourLastId = null;
	}

	@Test
	public void testAutomaticNotModified() {
		ourLastModifiedDate = new InstantDt("2012-11-25T02:34:45.2222Z").getValue();

		ourServer.fhirRequest("/Patient/2")
			.withHeader(Constants.HEADER_IF_NONE_MATCH, "\"222\"")
			.get()
			.assertStatus(304);

	}

	@Test
	public void testETagHeader() {
		ourLastModifiedDate = new InstantDt("2012-11-25T02:34:45.2222Z").getValue();

		HttpTestResponse status = ourServer.fhirRequest("/Patient/2/_history/3").get().assertStatus(200);

		IdentifierDt dt = status.getBodyAsResource(Patient.class).getIdentifierFirstRep();
		assertEquals("2", dt.getSystemElement().getValueAsString());
		assertEquals("3", dt.getValue());

		status.assertHeader(Constants.HEADER_ETAG_LC, "W/\"222\"");
	}

	@Test
	public void testLastModifiedHeader() {
		ourLastModifiedDate = new InstantDt("2012-11-25T02:34:45.222Z").getValue();

		HttpTestResponse status = ourServer.fhirRequest("/Patient/2/_history/3").get().assertStatus(200);

		IdentifierDt dt = status.getBodyAsResource(Patient.class).getIdentifierFirstRep();
		assertEquals("2", dt.getSystemElement().getValueAsString());
		assertEquals("3", dt.getValue());

		status.assertHeader(Constants.HEADER_LAST_MODIFIED_LOWERCASE, "Sun, 25 Nov 2012 02:34:45 GMT");
	}

	@Test
	public void testUpdateWithIfMatch() {
		Patient p = new Patient();
		p.setId("2");
		p.addIdentifier().setSystem("urn:system").setValue("001");
		String resBody = ourCtx.newXmlParser().encodeResourceToString(p);

		ourServer.fhirRequest("/Patient/2")
			.withHeader(Constants.HEADER_IF_MATCH, "\"221\"")
			.put(resBody, Constants.CT_FHIR_XML)
			.assertStatus(200);
		assertEquals("Patient/2/_history/221", ourLastId.toUnqualified().getValue());
	}

	@Test
	public void testUpdateWithIfMatchPreconditionFailed() {
		Patient p = new Patient();
		p.setId("2");
		p.addIdentifier().setSystem("urn:system").setValue("001");
		String resBody = ourCtx.newXmlParser().encodeResourceToString(p);

		ourServer.fhirRequest("/Patient/2")
			.withHeader(Constants.HEADER_IF_MATCH, "\"222\"")
			.put(resBody, Constants.CT_FHIR_XML)
			.assertStatus(412);
		assertEquals("Patient/2/_history/222", ourLastId.toUnqualified().getValue());
	}

	@Test
	public void testUpdateWithNoVersion() {
		Patient p = new Patient();
		p.setId("2");
		p.addIdentifier().setSystem("urn:system").setValue("001");
		String resBody = ourCtx.newXmlParser().encodeResourceToString(p);

		ourServer.fhirRequest("/Patient/2").put(resBody, Constants.CT_FHIR_XML).assertStatus(200);

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class PatientProvider implements IResourceProvider {

		@Read(version = true)
		public Patient findPatient(@IdParam IdDt theId) {
			Patient patient = new Patient();
			ResourceMetadataKeyEnum.UPDATED.put(patient, new InstantDt(ourLastModifiedDate));
			patient.addIdentifier().setSystem(theId.getIdPart()).setValue(theId.getVersionIdPart());
			patient.setId(theId.withVersion("222"));
			return patient;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Update
		public MethodOutcome updatePatient(@IdParam IdDt theId, @ResourceParam Patient theResource) {
			ourLastId = theId;

			if ("222".equals(theId.getVersionIdPart())) {
				throw new PreconditionFailedException("Bad version");
			}

			return new MethodOutcome(theId.withVersion(theId.getVersionIdPart() + "0"));
		}

	}

}
