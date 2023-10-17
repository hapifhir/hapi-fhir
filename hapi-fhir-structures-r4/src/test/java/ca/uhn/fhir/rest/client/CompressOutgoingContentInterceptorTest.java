package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.apache.GZipContentInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompressOutgoingContentInterceptorTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static Patient ourLastPatient;
	private static String ourLastReq;
	private static String ourLastResponseEncoding;

	@RegisterExtension
	public RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
		 .registerProvider(new DummyPatientResourceProvider())
		 .withPagingProvider(new FifoMemoryPagingProvider(10))
		 .setDefaultResponseEncoding(EncodingEnum.XML);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@Test
	public void testCreate() {
		IGenericClient client = ourServer.getFhirClient();
		client.registerInterceptor(new GZipContentInterceptor());

		Patient p = new Patient();
		p.addName().setFamily("FAMILY");

		client.create().resource(p).execute();

		assertEquals("FAMILY", p.getName().get(0).getFamily());
		assertEquals("gzip", ourLastReq);
		assertEquals("gzip", ourLastResponseEncoding);
	}


	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Create
		public MethodOutcome create(HttpServletRequest theReq, @ResourceParam Patient thePatient) {
			ourLastReq = theReq.getHeader(Constants.HEADER_CONTENT_ENCODING.toLowerCase());
			ourLastResponseEncoding = theReq.getHeader(Constants.HEADER_ACCEPT_ENCODING.toLowerCase());
			ourLastPatient = thePatient;
			return new MethodOutcome(new IdDt("Patient", "1"));
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

	}

}
