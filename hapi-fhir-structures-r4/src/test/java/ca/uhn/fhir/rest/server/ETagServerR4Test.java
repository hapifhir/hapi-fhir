package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
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
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ETagServerR4Test {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
  private static Date ourLastModifiedDate;
  private static IdType ourLastId;
  private static boolean ourPutVersionInPatientId;
  private static boolean ourPutVersionInPatientMeta;

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new PatientProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultResponseEncoding(EncodingEnum.XML);

	@BeforeEach
  public void before() {
    ourLastId = null;
    ourPutVersionInPatientId = true;
    ourPutVersionInPatientMeta = false;
  }

  @Test
  public void testAutomaticNotModified() throws Exception {
    doTestAutomaticNotModified();
  }

  @Test
  public void testAutomaticNotModifiedFromVersionInMeta() throws Exception {
  	  ourPutVersionInPatientId = false;
  	  ourPutVersionInPatientMeta = true;
	  doTestAutomaticNotModified();
  }

  private void doTestAutomaticNotModified() throws Exception {
	  ourLastModifiedDate = new InstantDt("2012-11-25T02:34:45.2222Z").getValue();

	  ourServer.fhirRequest("/Patient/2")
		  .withHeader(Constants.HEADER_IF_NONE_MATCH, "\"222\"")
		  .get()
		  .assertStatus(Constants.STATUS_HTTP_304_NOT_MODIFIED);
  }

  @Test
  public void testETagHeader() throws Exception {
    ourLastModifiedDate = new InstantDt("2012-11-25T02:34:45.2222Z").getValue();

    HttpTestResponse response = ourServer.fhirRequest("/Patient/2/_history/3").get().assertStatus(200);
    String responseContent = response.getBody();

    Identifier dt = ourCtx.newXmlParser().parseResource(Patient.class, responseContent).getIdentifier().get(0);
		assertEquals("2", dt.getSystemElement().getValueAsString());
		assertEquals("3", dt.getValue());

    String cl = response.getHeader(Constants.HEADER_ETAG_LC);
		assertNotNull(cl);
		assertEquals("W/\"222\"", cl);
  }

  @Test
  public void testETagHeaderFromVersionInMeta() throws Exception {
    ourPutVersionInPatientMeta = true;
    ourPutVersionInPatientId = false;
    ourLastModifiedDate = null;

    HttpTestResponse response = ourServer.fhirRequest("/Patient/2/_history/3").get().assertStatus(200);

    String cl = response.getHeader(Constants.HEADER_ETAG_LC);
		assertNotNull(cl);
		assertEquals("W/\"222\"", cl);
  }

  @Test
  public void testLastModifiedHeader() throws Exception {
    ourLastModifiedDate = new InstantDt("2012-11-25T02:34:45.2222Z").getValue();

    HttpTestResponse response = ourServer.fhirRequest("/Patient/2/_history/3").get().assertStatus(200);
    String responseContent = response.getBody();

    Identifier dt = ourCtx.newXmlParser().parseResource(Patient.class, responseContent).getIdentifier().get(0);
		assertEquals("2", dt.getSystemElement().getValueAsString());
		assertEquals("3", dt.getValue());

    String cl = response.getHeader(Constants.HEADER_LAST_MODIFIED_LOWERCASE);
		assertNotNull(cl);
		assertEquals("Sun, 25 Nov 2012 02:34:45 GMT", cl);
  }

  @Test
  public void testUpdateWithIfMatch() throws Exception {
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
  public void testUpdateWithIfMatchPreconditionFailed() throws Exception {
    Patient p = new Patient();
    p.setId("2");
    p.addIdentifier().setSystem("urn:system").setValue("001");
    String resBody = ourCtx.newXmlParser().encodeResourceToString(p);

    ourServer.fhirRequest("/Patient/2")
      .withHeader(Constants.HEADER_IF_MATCH, "\"222\"")
      .put(resBody, Constants.CT_FHIR_XML)
      .assertStatus(Constants.STATUS_HTTP_412_PRECONDITION_FAILED);
		assertEquals("Patient/2/_history/222", ourLastId.toUnqualified().getValue());
  }

  @Test
  public void testUpdateWithNoVersion() throws Exception {
    Patient p = new Patient();
    p.setId("2");
    p.addIdentifier().setSystem("urn:system").setValue("001");
    String resBody = ourCtx.newXmlParser().encodeResourceToString(p);

    ourServer.fhirRequest("/Patient/2").put(resBody, Constants.CT_FHIR_XML).assertStatus(200);

  }

  @AfterAll
  public static void afterClass() {
    TestUtil.randomizeLocaleAndTimezone();
  }


  public static class PatientProvider implements IResourceProvider {

    @Override
    public Class<Patient> getResourceType() {
      return Patient.class;
    }

    @Read(version = true)
    public Patient read(@IdParam IdType theId) {
      Patient patient = new Patient();
      patient.getMeta().setLastUpdated(ourLastModifiedDate);
      patient.addIdentifier().setSystem(theId.getIdPart()).setValue(theId.getVersionIdPart());
      if (ourPutVersionInPatientId) {
        patient.setId(theId.withVersion("222"));
      }
      if (ourPutVersionInPatientMeta) {
        patient.getMeta().setVersionId("222");
      }
      return patient;
    }

    @Update
    public MethodOutcome updatePatient(@IdParam IdType theId, @ResourceParam Patient theResource) {
      ourLastId = theId;

      if ("222".equals(theId.getVersionIdPart())) {
        throw new PreconditionFailedException("Bad version");
      }

      return new MethodOutcome(theId.withVersion(theId.getVersionIdPart() + "0"));
    }

  }

}
