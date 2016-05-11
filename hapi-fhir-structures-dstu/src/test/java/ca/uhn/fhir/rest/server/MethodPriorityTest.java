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
import org.hamcrest.core.StringContains;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class MethodPriorityTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu1();
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer ourServlet;

	@Test
	public void testDelegateTo_idMethod() throws Exception {
		ourServlet.setResourceProviders(new DummyObservationResourceProvider());
		ourServer.start();

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation?name=name");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, StringContains.containsString("01"));

		httpGet = new HttpGet("http://localhost:" + ourPort + "/Observation?_id=id");
		status = ourClient.execute(httpGet);
		responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, StringContains.containsString("02"));
	}

	@After
	public void after() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		ServletHandler proxyHandler = new ServletHandler();
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	public static class DummyObservationResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Observation.class;
		}

		@SuppressWarnings("unused")
		@Search
		public List<Observation> search01(@OptionalParam(name = Observation.SP_NAME) StringParam theName) {
			List<Observation> retVal = new ArrayList<Observation>();
			Observation o = new Observation();
			o.setId("01");
			o.getName().setText("search01");
			retVal.add(o);
			return retVal;
		}

		@SuppressWarnings("unused")
		@Search
		public List<Observation> search02(@RequiredParam(name = "_id") StringParam theId) {
			List<Observation> retVal = new ArrayList<Observation>();
			Observation o = new Observation();
			o.setId("02");
			o.getName().setText("search02");
			retVal.add(o);
			return retVal;

		}
	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
