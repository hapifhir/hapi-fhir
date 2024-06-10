package ca.uhn.fhir.rest.server.interceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpTrace;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BanUnsupprtedHttpMethodsInterceptorDstu3Test {

	private static final FhirContext ourCtx = FhirContext.forDstu3Cached();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(BanUnsupprtedHttpMethodsInterceptorDstu3Test.class);
	@RegisterExtension
	private RestfulServerExtension ourServer  = new RestfulServerExtension(ourCtx)
		 .setDefaultResponseEncoding(EncodingEnum.XML)
		 .registerProvider(new DummyPatientResourceProvider())
		 .registerInterceptor(new BanUnsupportedHttpMethodsInterceptor())
		 .withPagingProvider(new FifoMemoryPagingProvider(100))
		 .setDefaultPrettyPrint(false);

	@RegisterExtension
	private HttpClientExtension ourClient = new HttpClientExtension();

	@Test
	public void testHttpTraceNotEnabled() throws Exception {
		HttpTrace req = new HttpTrace(ourServer.getBaseUrl() + "/Patient");
		CloseableHttpResponse status = ourClient.execute(req);
		try {
			ourLog.info(status.toString());
			assertEquals(405, status.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}
	}
	
	@Test	
	public void testHeadJsonWithInvalidPatient() throws Exception {	
		HttpHead httpGet = new HttpHead(ourServer.getBaseUrl() + "/Patient/123");	
		HttpResponse status = ourClient.execute(httpGet);
		assertNull(status.getEntity());	
 		ourLog.info(status.toString());

		assertEquals(404, status.getStatusLine().getStatusCode());
		assertThat(status.getFirstHeader("x-powered-by").getValue()).contains("HAPI");	
	}
	
	@Test	
	public void testHeadJsonWithValidPatient() throws Exception {	
		HttpHead httpGet = new HttpHead(ourServer.getBaseUrl() + "/Patient/1");	
		HttpResponse status = ourClient.execute(httpGet);
		assertNull(status.getEntity());	
 		ourLog.info(status.toString());

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(status.getFirstHeader("x-powered-by").getValue()).contains("HAPI");	
	}
	
	@Test
	public void testHttpTrackNotEnabled() throws Exception {
		HttpRequestBase req = new HttpRequestBase() {
			@Override
			public String getMethod() {
				return "TRACK";
			}
		};
		req.setURI(new URI(ourServer.getBaseUrl() + "/Patient"));

		CloseableHttpResponse status = ourClient.execute(req);
		try {
			ourLog.info(status.toString());
			assertEquals(405, status.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}
	}

	@Test
	public void testHttpFooNotEnabled() throws Exception {
		HttpRequestBase req = new HttpRequestBase() {
			@Override
			public String getMethod() {
				return "FOO";
			}
		};
		req.setURI(new URI(ourServer.getBaseUrl() + "/Patient"));

		CloseableHttpResponse status = ourClient.execute(req);
		try {
			ourLog.info(status.toString());
			assertEquals(501, status.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}
	}

	@Test
	public void testRead() throws Exception {

		HttpGet httpGet = new HttpGet(ourServer.getBaseUrl() + "/Patient/1");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
	}
	
	@AfterAll
	public static void afterClassClearContext() throws Exception {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		private Patient createPatient1() {
			Patient patient = new Patient();
			patient.addName();
			patient.getName().get(0).setFamily("Test");
			patient.getName().get(0).addGiven("PatientOne");
			return patient;
		}

		public Map<String, Patient> getIdToPatient() {
			Map<String, Patient> idToPatient = new HashMap<String, Patient>();
			{
				Patient patient = createPatient1();
				idToPatient.put("1", patient);
			}
			return idToPatient;
		}

		/**
		 * Retrieve the resource by its identifier
		 * 
		 * @param theId
		 *           The resource identity
		 * @return The resource
		 */
		@Read()
		public Patient getResourceById(@IdParam IdType theId) {
			if (theId.getIdPart().equals("EX")) {
				throw new InvalidRequestException("FOO");
			}
			String key = theId.getIdPart();
			Patient retVal = getIdToPatient().get(key);
			return retVal;
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

	}

}
