package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Count;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class SearchCountParamDstu3Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchCountParamDstu3Test.class);
	private static int ourPort;
	private static Server ourServer;
	private static String ourLastMethod;
	private static Integer ourLastParam;

	@Before
	public void before() {
		ourLastMethod = null;
		ourLastParam = null;
	}

	@Test
	public void testSearch() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_count=2");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("search", ourLastMethod);
			assertEquals(new Integer(2), ourLastParam);
			
			//@formatter:off
			assertThat(responseContent, stringContainsInOrder(
				"<link>", 
				"<relation value=\"self\"/>", 
				"<url value=\"http://localhost:" + ourPort + "/Patient?_count=2\"/>", 
				"</link>", 
				"<link>",
				"<relation value=\"next\"/>", 
				"<url value=\"http://localhost:" + ourPort + "?_getpages=", "&amp;_getpagesoffset=2&amp;_count=2&amp;_bundletype=searchset\"/>", 
				"</link>"));
			//@formatter:on
			
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

	}

	/**
	 * See #372
	 */
	@Test
	public void testSearchWithNoCountParam() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=searchWithNoCountParam&_count=2");
		CloseableHttpResponse status = ourClient.execute(httpGet);
		try {
			String responseContent = IOUtils.toString(status.getEntity().getContent());
			ourLog.info(responseContent);
			assertEquals(200, status.getStatusLine().getStatusCode());
			assertEquals("searchWithNoCountParam", ourLastMethod);
			assertEquals(null, ourLastParam);
			
			//@formatter:off
			assertThat(responseContent, stringContainsInOrder(
				"<link>", 
				"<relation value=\"self\"/>", 
				"<url value=\"http://localhost:" + ourPort + "/Patient?_count=2&amp;_query=searchWithNoCountParam\"/>", 
				"</link>", 
				"<link>",
				"<relation value=\"next\"/>", 
				"<url value=\"http://localhost:" + ourPort + "?_getpages=", "&amp;_getpagesoffset=2&amp;_count=2&amp;_bundletype=searchset\"/>", 
				"</link>"));
			//@formatter:on
			
		} finally {
			IOUtils.closeQuietly(status.getEntity().getContent());
		}

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
		servlet.setPagingProvider(new FifoMemoryPagingProvider(10));

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

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

		//@formatter:off
		@SuppressWarnings("rawtypes")
		@Search()
		public List search(
				@OptionalParam(name=Patient.SP_IDENTIFIER) TokenParam theIdentifier,
				@Count() Integer theParam
				) {
			ourLastMethod = "search";
			ourLastParam = theParam;
			ArrayList<Patient> retVal = new ArrayList<Patient>();
			for (int i = 1; i < 100; i++) {
				retVal.add((Patient) new Patient().addName(new HumanName().setFamily("FAMILY")).setId("" + i));
			}
			return retVal;
		}
		//@formatter:on

		//@formatter:off
		@SuppressWarnings("rawtypes")
		@Search(queryName="searchWithNoCountParam")
		public List searchWithNoCountParam() {
			ourLastMethod = "searchWithNoCountParam";
			ourLastParam = null;
			ArrayList<Patient> retVal = new ArrayList<Patient>();
			for (int i = 1; i < 100; i++) {
				retVal.add((Patient) new Patient().addName(new HumanName().setFamily("FAMILY")).setId("" + i));
			}
			return retVal;
		}
		//@formatter:on

	}

}
