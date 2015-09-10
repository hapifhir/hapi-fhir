package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.DateRangeParamTest;
import ca.uhn.fhir.util.PortUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class DateRangeParamSearchTest {

	private static CloseableHttpClient ourClient;
	private static int ourPort;
	private static Server ourServer;
	private static final FhirContext ourCtx = FhirContext.forDstu1();
	
	@Test
	public void testSearchForOneUnqualifiedDate() throws Exception {
		String baseUrl = "http://localhost:" + ourPort + "/Patient?" + Patient.SP_BIRTHDATE + "=";
		HttpGet httpGet = new HttpGet(baseUrl + "2012-01-01");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("2012-01-01", ourLastDateRange.getLowerBound().getValueAsString());
		assertEquals("2012-01-01", ourLastDateRange.getUpperBound().getValueAsString());
		
		assertEquals(DateRangeParamTest.parse("2012-01-01 00:00:00.0000"), ourLastDateRange.getLowerBoundAsInstant());
		assertEquals(DateRangeParamTest.parseM1("2012-01-02 00:00:00.0000"), ourLastDateRange.getUpperBoundAsInstant());

	}

	@Test
	public void testSearchForMultipleUnqualifiedDate() throws Exception {
		String baseUrl = "http://localhost:" + ourPort + "/Patient?" + Patient.SP_BIRTHDATE + "=";
		HttpGet httpGet = new HttpGet(baseUrl + "2012-01-01&" + Patient.SP_BIRTHDATE + "=2012-02-03");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(400, status.getStatusLine().getStatusCode());

	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	
	@Before
	public void before() {
		ourLastDateRange = null;
	}
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	private static DateRangeParam ourLastDateRange;
	
	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Search()
		public List<Patient> search(@RequiredParam(name=Patient.SP_BIRTHDATE) DateRangeParam theDateRange) {
			ourLastDateRange = theDateRange;
			
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient patient = new Patient();
			patient.setId("1");
			patient.addIdentifier("system", "hello");
			retVal.add(patient);
			return retVal;
		}

		
		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

	}

}
