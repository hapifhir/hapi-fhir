package ca.uhn.fhir.rest.server;

import static org.junit.Assert.assertEquals;

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
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.context.RuntimeSearchParam.RuntimeSearchParamStatusEnum;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Conformance;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.method.RestSearchParameterTypeEnum;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class DynamicSearchTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu1();
	private static SearchParameterMap ourLastSearchParams;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(DynamicSearchTest.class);

	private static int ourPort;

	private static Server ourServer;

	@Before
	public void before() {
		ourLastSearchParams = null;
	}

	@Test
	public void testConformance() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/metadata?_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Conformance conf = ourCtx.newXmlParser().parseResource(Conformance.class,responseContent);
		
		ourLog.info(responseContent);
	}


	@Test
	public void testSearchOneStringParam() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?param1=param1value");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());

		assertEquals(1, ourLastSearchParams.size());
		StringAndListParam andList =(StringAndListParam) ourLastSearchParams.get("param1");
		assertEquals(1,andList.getValuesAsQueryTokens().size());
		StringOrListParam orList = andList.getValuesAsQueryTokens().get(0);
		assertEquals(1,orList.getValuesAsQueryTokens().size());
		StringParam param1 = orList.getValuesAsQueryTokens().get(0);
		assertEquals("param1value", param1.getValue());
	}

	@Test
	public void testSearchOneStringParamWithAnd() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?param1=param1value&param1=param1value2");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());

		assertEquals(1, ourLastSearchParams.size());
		StringAndListParam andList =(StringAndListParam) ourLastSearchParams.get("param1");
		assertEquals(2,andList.getValuesAsQueryTokens().size());
		StringOrListParam orList = andList.getValuesAsQueryTokens().get(0);
		assertEquals(1,orList.getValuesAsQueryTokens().size());
		StringParam param1 = orList.getValuesAsQueryTokens().get(0);
		assertEquals("param1value", param1.getValue());

		orList = andList.getValuesAsQueryTokens().get(1);
		assertEquals(1,orList.getValuesAsQueryTokens().size());
		StringParam param1b = orList.getValuesAsQueryTokens().get(0);
		assertEquals("param1value2", param1b.getValue());
	}

	@Test
	public void testSearchOneStringParamWithOr() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?param1=param1value,param1value2");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		Bundle bundle = ourCtx.newXmlParser().parseBundle(responseContent);
		assertEquals(1, bundle.getEntries().size());

		assertEquals(1, ourLastSearchParams.size());
		StringAndListParam andList =(StringAndListParam) ourLastSearchParams.get("param1");
		assertEquals(1,andList.getValuesAsQueryTokens().size());
		StringOrListParam orList = andList.getValuesAsQueryTokens().get(0);
		assertEquals(2,orList.getValuesAsQueryTokens().size());
		StringParam param1 = orList.getValuesAsQueryTokens().get(0);
		assertEquals("param1value", param1.getValue());
		StringParam param1b = orList.getValuesAsQueryTokens().get(1);
		assertEquals("param1value2", param1b.getValue());
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

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IDynamicSearchResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Override
		public List<RuntimeSearchParam> getSearchParameters() {
			ArrayList<RuntimeSearchParam> retVal = new ArrayList<RuntimeSearchParam>();
			retVal.add(new RuntimeSearchParam("param1", "This is the first parameter", "Patient.param1", RestSearchParameterTypeEnum.STRING, null, null, RuntimeSearchParamStatusEnum.ACTIVE));
			retVal.add(new RuntimeSearchParam("param2", "This is the second parameter", "Patient.param2", RestSearchParameterTypeEnum.DATE, null, null, RuntimeSearchParamStatusEnum.ACTIVE));
			return retVal;
		}

		@Search(dynamic=true)
		public List<Patient> search(SearchParameterMap theSearchParams) {
			ourLastSearchParams = theSearchParams;

			ArrayList<Patient> retVal = new ArrayList<Patient>();

			Patient patient = new Patient();
			patient.setId("Patient/1");
			patient.addIdentifier("system", "fooCompartment");
			retVal.add(patient);
			return retVal;
		}

	}


}
