package ca.uhn.fhir.rest.server;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.util.PortUtil;

/**
 * Created by dsotnikov on 2/25/2014.
 */
public class IncludeAndRevincludeParameterTest {

	private static CloseableHttpClient ourClient;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(IncludeAndRevincludeParameterTest.class);
	private static int ourPort;
	private static Server ourServer;
	private static FhirContext ourCtx;
	private static Set<Include> ourIncludes;
	private static Set<Include> ourReverseIncludes;

	@Before
	public void before() {
		ourIncludes = null;
		ourReverseIncludes = null;
	}
	
	@Test
	public void testNoIncludes() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=normalInclude");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		
		assertThat(ourIncludes, hasSize(0));
		assertThat(ourReverseIncludes, hasSize(0));
	}

	@Test
	public void testWithBoth() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_query=normalInclude&_include=A.a&_include=B.b&_revinclude=C.c&_revinclude=D.d");
		HttpResponse status = ourClient.execute(httpGet);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		
		assertThat(ourIncludes, hasSize(2));
		assertThat(ourReverseIncludes, hasSize(2));
		assertThat(ourIncludes, containsInAnyOrder(new Include("A.a"), new Include("B.b")));
		assertThat(ourReverseIncludes, containsInAnyOrder(new Include("C.c"), new Include("D.d")));
	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {

		ourCtx = FhirContext.forDstu2();
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(new DummyPatientResourceProvider());
        servlet.setBundleInclusionRule(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE);
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

		@Search(queryName = "normalInclude")
		public List<Patient> normalInclude(
				@IncludeParam() Set<Include> theIncludes,
				@IncludeParam(reverse=true) Set<Include> theRevincludes
				) {
			ourIncludes = theIncludes;
			ourReverseIncludes = theRevincludes;
			return new ArrayList<Patient>();
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

	}

}
