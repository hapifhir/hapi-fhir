package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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
import org.hl7.fhir.instance.model.HumanName;
import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class SearchWithGenericListHl7OrgDstu2Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2Hl7Org();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchWithGenericListHl7OrgDstu2Test.class);
	private static int ourPort;
	private static Server ourServer;
	private static String ourLastMethod;

	@Before
	public void before() {
		ourLastMethod = null;
	}

	/**
	 * See #291
	 */
	@Test
	public void testSearch() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?identifier=foo&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertEquals("searchByIdentifier", ourLastMethod);
		assertThat(responseContent, containsString("<family value=\"FAMILY\""));
		assertThat(responseContent, containsString("<fullUrl value=\"http://localhost:" + ourPort + "/Patient/1\"/>"));
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
		public List searchByIdentifier(
				@RequiredParam(name=Patient.SP_IDENTIFIER) TokenParam theIdentifier) {
			ourLastMethod = "searchByIdentifier";
			ArrayList<Patient> retVal = new ArrayList<Patient>();
			retVal.add((Patient) new Patient().addName(new HumanName().addFamily("FAMILY")).setId("1"));
			return retVal;
		}
		//@formatter:on


	}

}
