package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.hl7.fhir.dstu2.model.IdType;
import org.hl7.fhir.dstu2.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class PreferHl7OrgDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PreferHl7OrgDstu2Test.class);
  private static final FhirContext ourCtx = FhirContext.forDstu2Hl7OrgCached();

  @RegisterExtension
  public static RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
      .registerProvider(new PatientProvider())
      .withPagingProvider(new FifoMemoryPagingProvider(100))
      .setDefaultResponseEncoding(EncodingEnum.JSON)
      .setDefaultPrettyPrint(false);

  @RegisterExtension
  public static HttpClientExtension ourClient = new HttpClientExtension();


  @Test
	public void testCreateWithNoPrefer() throws Exception {

		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpPost httpPost = new HttpPost(ourServer.getBaseUrl() + "/Patient");
		httpPost.setEntity(new StringEntity(ourCtx.newXmlParser().encodeResourceToString(patient), ContentType.create(Constants.CT_FHIR_XML, "UTF-8")));

		HttpResponse status = ourClient.execute(httpPost);

		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info("Response was:\n{}", responseContent);

		assertEquals(201, status.getStatusLine().getStatusCode());
		assertEquals(ourServer.getBaseUrl() + "/Patient/001/_history/002", status.getFirstHeader("location").getValue());
		assertEquals(ourServer.getBaseUrl() + "/Patient/001/_history/002", status.getFirstHeader("content-location").getValue());
		
	}

	

	
	@AfterAll
	public static void afterClass() throws Exception {
    TestUtil.randomizeLocaleAndTimezone();
	}
		
	
	public static class PatientProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Create()
		public MethodOutcome createPatient(@ResourceParam Patient thePatient) {
			MethodOutcome retVal = new MethodOutcome(new IdType("Patient/001/_history/002"));
			return retVal;
		}

		@Update()
		public MethodOutcome updatePatient(@ResourceParam Patient thePatient, @IdParam IdType theIdParam) {
			return new MethodOutcome(new IdType("Patient/001/_history/002"));
		}

	}

}
