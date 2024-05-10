package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.hl7.fhir.dstu2.model.IdType;
import org.hl7.fhir.dstu2.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class DeleteConditionalHl7OrgTest {
	private static String ourLastConditionalUrl;
  private static final FhirContext ourCtx = FhirContext.forDstu2Hl7OrgCached();
	private static IdType ourLastIdParam;


  @RegisterExtension
  public static RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
      .registerProvider(new PatientProvider())
      .withPagingProvider(new FifoMemoryPagingProvider(100))
      .setDefaultResponseEncoding(EncodingEnum.JSON)
      .setDefaultPrettyPrint(false);

  @RegisterExtension
  public static HttpClientExtension ourClient = new HttpClientExtension();


  @BeforeEach
	public void before() {
		ourLastConditionalUrl = null;
		ourLastIdParam = null;
	}

	@Test
	public void testUpdateWithConditionalUrl() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpDelete httpPost = new HttpDelete(ourServer.getBaseUrl() + "/Patient?identifier=system%7C001");

		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(204, status.getStatusLine().getStatusCode());

		assertNull(ourLastIdParam);
		assertEquals("Patient?identifier=system%7C001", ourLastConditionalUrl);
	}

	
	@Test
	public void testUpdateWithoutConditionalUrl() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpDelete httpPost = new HttpDelete(ourServer.getBaseUrl() + "/Patient/2");

		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(204, status.getStatusLine().getStatusCode());

		assertEquals("Patient/2", ourLastIdParam.toUnqualified().getValue());
		assertNull(ourLastConditionalUrl);
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

		
		@Delete()
		public MethodOutcome updatePatient(@ConditionalUrlParam String theConditional, @IdParam IdType theIdParam) {
			ourLastConditionalUrl = theConditional;
			ourLastIdParam = theIdParam;
			return new MethodOutcome(new IdType("Patient/001/_history/002"));
		}

	}

}
