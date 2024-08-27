package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.ConditionalUrlParam;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DeleteDstu2Test {
	private static final FhirContext ourCtx = FhirContext.forDstu2Cached();
	private static boolean ourInvoked;
	private static String ourLastConditionalUrl;
	private static IdDt ourLastIdParam;

	@RegisterExtension
	public static final RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		.setDefaultResponseEncoding(EncodingEnum.XML)
		.registerProvider(new PatientProvider())
		.withPagingProvider(new FifoMemoryPagingProvider(100))
		.setDefaultPrettyPrint(false);

	@RegisterExtension
	public static final HttpClientExtension ourClient = new HttpClientExtension();


	@BeforeEach
	public void before() {
		ourLastConditionalUrl = null;
		ourLastIdParam = null;
		ourInvoked = false;
	}


	
	@Test
	public void testDeleteWithConditionalUrl() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpDelete httpPost = new HttpDelete(ourServer.getBaseUrl() + "/Patient?identifier=system%7C001");

		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(204, status.getStatusLine().getStatusCode());

		assertNull(ourLastIdParam);
		assertEquals("Patient?identifier=system%7C001", ourLastConditionalUrl);
	}

	@Test
	public void testDeleteWithoutConditionalUrl() throws Exception {
		Patient patient = new Patient();
		patient.addIdentifier().setValue("002");

		HttpDelete httpPost = new HttpDelete(ourServer.getBaseUrl() + "/Patient/2");

		HttpResponse status = ourClient.execute(httpPost);

		assertEquals(204, status.getStatusLine().getStatusCode());
		assertNull(status.getFirstHeader(Constants.HEADER_CONTENT_TYPE));

		assertEquals("Patient/2", ourLastIdParam.toUnqualified().getValue());
		assertNull(ourLastConditionalUrl);
	}


	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}
		
	
	public static class PatientProvider implements IResourceProvider {

		@Delete()
		public MethodOutcome delete(@ConditionalUrlParam String theConditional, @IdParam IdDt theIdParam) {
			ourLastConditionalUrl = theConditional;
			ourLastIdParam = theIdParam;
			ourInvoked = true;
			return new MethodOutcome(new IdDt("Patient/001/_history/002"));
		}

		
		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

	}

}
