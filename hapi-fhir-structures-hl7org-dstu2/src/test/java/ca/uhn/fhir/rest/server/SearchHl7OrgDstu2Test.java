package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

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
import org.hl7.fhir.instance.model.Bundle;
import org.hl7.fhir.instance.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.util.PatternMatcherB;
import ca.uhn.fhir.util.PortUtil;

public class SearchHl7OrgDstu2Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2Hl7Org();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchHl7OrgDstu2Test.class);
	private static int ourPort;

	private static InstantDt ourReturnPublished;

	private static Server ourServer;

	@Test
	public void testEncodeConvertsReferencesToRelative() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=searchWithRef");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		
		assertThat(responseContent, not(containsString("text")));

		assertEquals(200, status.getStatusLine().getStatusCode());
		Patient patient = (Patient) ourCtx.newXmlParser().parseResource(Bundle.class, responseContent).getEntry().get(0).getResource();
		String ref = patient.getManagingOrganization().getReference();
		assertEquals("Organization/555", ref);
		assertNull(status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION));
	}

	@Test
	public void testEncodeConvertsReferencesToRelativeJson() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=searchWithRef&_format=json");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		
		assertThat(responseContent, not(containsString("text")));

		assertEquals(200, status.getStatusLine().getStatusCode());
		Patient patient = (Patient) ourCtx.newJsonParser().parseResource(Bundle.class, responseContent).getEntry().get(0).getResource();
		String ref = patient.getManagingOrganization().getReference();
		assertEquals("Organization/555", ref);
		assertNull(status.getFirstHeader(Constants.HEADER_CONTENT_LOCATION));
	}

	@Test
	public void testResultBundleHasUuid() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=searchWithRef");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		
		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, PatternMatcherB.pattern("id value..[0-9a-f-]+\\\""));
	}

	@Test
	public void testResultBundleHasUpdateTime() throws Exception {
		ourReturnPublished = new InstantDt("2011-02-03T11:22:33Z");
		
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=searchWithBundleProvider&_pretty=true");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);

		assertThat(responseContent, stringContainsInOrder("<lastUpdated value=\"2011-02-03T11:22:33Z\"/>"));
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
	public static class DummyPatientResourceProvider implements IResourceProvider {
		

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Search(queryName="searchWithBundleProvider")
		public IBundleProvider searchWithBundleProvider() {
			return new IBundleProvider() {
				
				@Override
				public InstantDt getPublished() {
					return ourReturnPublished;
				}
				
				@Override
				public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
					throw new IllegalStateException();
				}
				
				@Override
				public Integer preferredPageSize() {
					return null;
				}
				
				@Override
				public Integer size() {
					return 0;
				}

				@Override
				public String getUuid() {
					return null;
				}
			};
		}
		
		@Search(queryName="searchWithRef")
		public Patient searchWithRef() {
			Patient patient = new Patient();
			patient.setId("Patient/1/_history/1");
			patient.getManagingOrganization().setReference("http://localhost:" + ourPort + "/Organization/555/_history/666");
			return patient;
		}

	}

}
