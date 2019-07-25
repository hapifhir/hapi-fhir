package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

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
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class SearchWithDstu2BundleTest {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchWithDstu2BundleTest.class);
	private static int ourPort;
	private static Server ourServer;

	@Test
	public void testSearch() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_format=xml&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());

		responseContent = responseContent.replace("_pretty=true&amp;_format=xml", "_format=xml&amp;_pretty=true");
		
		ourLog.info(responseContent);

		//@formatter:off
		assertThat(responseContent, stringContainsInOrder("<Bundle xmlns=\"http://hl7.org/fhir\">", 
				"<type value=\"searchset\"/>", 
				"<link>" ,
				"<relation value=\"self\"/>", 
				"<url value=\"http://localhost:" + ourPort + "/Patient?_format=xml&amp;_pretty=true\"/>", 
				"</link>" ,
				"<entry>" , 
				"<resource>" , 
				"<Patient xmlns=\"http://hl7.org/fhir\">"));
		// @formatter:off
		
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
		servlet.setFhirContext(ourCtx);
		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Search
		public Bundle search() {
			Bundle retVal = new Bundle();
			
			Patient p1 = new Patient();
			p1.setId("Patient/123/_history/456");
			p1.addIdentifier().setValue("p1ReadValue");
			
			retVal.addEntry().setResource(p1);
			
			return retVal;
		}

	}

}
