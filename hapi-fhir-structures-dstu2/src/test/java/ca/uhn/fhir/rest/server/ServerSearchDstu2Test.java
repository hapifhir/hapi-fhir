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
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.PortUtil;

public class ServerSearchDstu2Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static String ourLastMethod;
	private static StringParam ourLastRef;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ServerSearchDstu2Test.class);
	private static int ourPort;
	private static Server ourServer;

	@Before
	public void before() {
		ourLastMethod = null;
		ourLastRef = null;
	}
	
	@Test
	public void testSearchParam1() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/?param1=param1value");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals("searchParam1", ourLastMethod);
		assertEquals("param1value", ourLastRef.getValue());
	}

	@Test
	public void testSearchParam2() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/?param2=param2value&foo=bar");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals("searchParam2", ourLastMethod);
		assertEquals("param2value", ourLastRef.getValue());
	}

	@Test
	public void testUnknownSearchParam() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/?foo=bar");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(null, ourLastMethod);
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);

		servlet.setPlainProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static class DummyPatientResourceProvider {
		
		//@formatter:off
		@Search(allowUnknownParams=true)
		public List<IBaseResource> searchParam1(
				@RequiredParam(name = "param1") StringParam theParam) {
			ourLastMethod = "searchParam1";
			ourLastRef = theParam;
			
			List<IBaseResource> retVal = new ArrayList<IBaseResource>();
			Patient patient = new Patient();
			patient.setId("123");
			patient.addName().addGiven("GIVEN");
			retVal.add(patient);
			return retVal;
		}
		//@formatter:on

		//@formatter:off
		@Search(allowUnknownParams=true)
		public List<IBaseResource> searchParam2(
				@RequiredParam(name = "param2") StringParam theParam) {
			ourLastMethod = "searchParam2";
			ourLastRef = theParam;
			
			List<IBaseResource> retVal = new ArrayList<IBaseResource>();
			Patient patient = new Patient();
			patient.setId("123");
			patient.addName().addGiven("GIVEN");
			retVal.add(patient);
			return retVal;
		}
		//@formatter:on

	}

}
