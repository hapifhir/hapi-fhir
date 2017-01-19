package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import org.hl7.fhir.dstu2016may.model.Patient;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class DateRangeParamSearchDstu2_1Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2_1();
	private static DateRangeParam ourLastDateRange;
	private static int ourPort;
	
	private static Server ourServer;
	private static SimpleDateFormat ourFmt;
	private static String ourBaseUrl;

	@Before
	public void before() {
		ourLastDateRange = null;
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

	
	@Test
	public void testSearchForOneUnqualifiedDate() throws Exception {
		HttpGet httpGet = new HttpGet(ourBaseUrl + "?birthdate=2012-01-01");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("2012-01-01", ourLastDateRange.getLowerBound().getValueAsString());
		assertEquals("2012-01-01", ourLastDateRange.getUpperBound().getValueAsString());
		
		assertEquals(parse("2012-01-01 00:00:00.0000"), ourLastDateRange.getLowerBoundAsInstant());
		assertEquals(parseM1("2012-01-02 00:00:00.0000"), ourLastDateRange.getUpperBoundAsInstant());
		assertEquals(ParamPrefixEnum.EQUAL, ourLastDateRange.getLowerBound().getPrefix());
		assertEquals(ParamPrefixEnum.EQUAL, ourLastDateRange.getUpperBound().getPrefix());
	}
	
	@Test
	public void testSearchForOneQualifiedDateEq() throws Exception {
		HttpGet httpGet = new HttpGet(ourBaseUrl + "?birthdate=eq2012-01-01");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("2012-01-01", ourLastDateRange.getLowerBound().getValueAsString());
		assertEquals("2012-01-01", ourLastDateRange.getUpperBound().getValueAsString());
		
		assertEquals(parse("2012-01-01 00:00:00.0000"), ourLastDateRange.getLowerBoundAsInstant());
		assertEquals(parseM1("2012-01-02 00:00:00.0000"), ourLastDateRange.getUpperBoundAsInstant());
		assertEquals(ParamPrefixEnum.EQUAL, ourLastDateRange.getLowerBound().getPrefix());
		assertEquals(ParamPrefixEnum.EQUAL, ourLastDateRange.getUpperBound().getPrefix());
	}

	@Test
	public void testSearchForOneQualifiedDateGt() throws Exception {
		HttpGet httpGet = new HttpGet(ourBaseUrl + "?birthdate=gt2012-01-01");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("2012-01-01", ourLastDateRange.getLowerBound().getValueAsString());
		assertEquals(null, ourLastDateRange.getUpperBound());
		
		assertEquals(parse("2012-01-02 00:00:00.0000"), ourLastDateRange.getLowerBoundAsInstant());
		assertEquals(null, ourLastDateRange.getUpperBoundAsInstant());
		assertEquals(ParamPrefixEnum.GREATERTHAN, ourLastDateRange.getLowerBound().getPrefix());
		assertEquals(null, ourLastDateRange.getUpperBound());
	}

	@Test
	public void testSearchForOneQualifiedDateLt() throws Exception {
		HttpGet httpGet = new HttpGet(ourBaseUrl + "?birthdate=lt2012-01-01");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals(null, ourLastDateRange.getLowerBound());
		assertEquals("2012-01-01", ourLastDateRange.getUpperBound().getValueAsString());
		
		assertEquals(null, ourLastDateRange.getLowerBoundAsInstant());
		assertEquals(parseM1("2012-01-01 00:00:00.0000"), ourLastDateRange.getUpperBoundAsInstant());
		assertEquals(null, ourLastDateRange.getLowerBound());
		assertEquals(ParamPrefixEnum.LESSTHAN, ourLastDateRange.getUpperBound().getPrefix());
	}

	@Test
	public void testSearchForOneQualifiedDateGe() throws Exception {
		HttpGet httpGet = new HttpGet(ourBaseUrl + "?birthdate=ge2012-01-01");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals("2012-01-01", ourLastDateRange.getLowerBound().getValueAsString());
		assertEquals(null, ourLastDateRange.getUpperBound());
		
		assertEquals(parse("2012-01-01 00:00:00.0000"), ourLastDateRange.getLowerBoundAsInstant());
		assertEquals(null, ourLastDateRange.getUpperBoundAsInstant());
		assertEquals(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, ourLastDateRange.getLowerBound().getPrefix());
		assertEquals(null, ourLastDateRange.getUpperBound());
	}

	@Test
	public void testSearchForOneQualifiedDateLe() throws Exception {
		HttpGet httpGet = new HttpGet(ourBaseUrl + "?birthdate=le2012-01-01");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());

		assertEquals(null, ourLastDateRange.getLowerBound());
		assertEquals("2012-01-01", ourLastDateRange.getUpperBound().getValueAsString());
		
		assertEquals(null, ourLastDateRange.getLowerBoundAsInstant());
		assertEquals(parseM1("2012-01-02 00:00:00.0000"), ourLastDateRange.getUpperBoundAsInstant());
		assertEquals(null, ourLastDateRange.getLowerBound());
		assertEquals(ParamPrefixEnum.LESSTHAN_OR_EQUALS, ourLastDateRange.getUpperBound().getPrefix());
	}


	public static Date parse(String theString) throws ParseException {
		return ourFmt.parse(theString);
	}

	public static Date parseM1(String theString) throws ParseException {
		return new Date(ourFmt.parse(theString).getTime() - 1L);
	}
	static {
		ourFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
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

		ourBaseUrl = "http://localhost:" + ourPort + "/Patient";
	}


	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		
		@Search()
		public List<Patient> search(@RequiredParam(name=Patient.SP_BIRTHDATE) DateRangeParam theDateRange) {
			ourLastDateRange = theDateRange;
			
			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient patient = new Patient();
			patient.setId("1");
			patient.addIdentifier().setSystem("system").setValue("hello");
			retVal.add(patient);
			return retVal;
		}

	}

}
