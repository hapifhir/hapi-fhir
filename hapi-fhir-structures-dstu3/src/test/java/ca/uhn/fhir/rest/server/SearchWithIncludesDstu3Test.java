package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.annotation.IncludeParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.dstu3.model.Organization;
import org.hl7.fhir.dstu3.model.Patient;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SearchWithIncludesDstu3Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu3();
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SearchWithIncludesDstu3Test.class);
	private static int ourPort;
	private static Server ourServer;

	@Test
	public void testSearchIncludesReferences() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient?_pretty=true&_include=Patient:organization&_include=Organization:" + Organization.SP_PARTOF);
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), StandardCharsets.UTF_8);
		IOUtils.closeQuietly(status.getEntity().getContent());
		ourLog.info(responseContent);
		assertEquals(200, status.getStatusLine().getStatusCode());
		
		// Response should include both the patient, and the organization that was referred to
		// by a linked resource
		
		assertThat(responseContent, containsString("<name value=\"child\"/>"));
		assertThat(responseContent, containsString("<name value=\"parent\"/>"));
		assertThat(responseContent, containsString("<name value=\"grandparent\"/>"));
		assertThat(responseContent, containsString("<mode value=\"include\"/>"));
		
	}


	 @AfterAll
	  public static void afterClassClearContext() throws Exception {
	    JettyUtil.closeServer(ourServer);
	    TestUtil.randomizeLocaleAndTimezone();
	  }

	

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		DummyPatientResourceProvider patientProvider = new DummyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setPagingProvider(new FifoMemoryPagingProvider(10));
		servlet.setResourceProviders(patientProvider);
		servlet.setDefaultResponseEncoding(EncodingEnum.XML);

		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

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
		@Search()
		public List<Patient> search(@IncludeParam() Set<Include> theIncludes) {
			ourLog.info("Includes: {}", theIncludes);
			
			// Grandparent has ID, other orgs don't
			Organization gp = new Organization();
			gp.setId("Organization/GP");
			gp.setName("grandparent");
			
			Organization parent = new Organization();
			parent.setName("parent");
			parent.getPartOf().setResource(gp);
			
			Organization child = new Organization();
			child.setName("child");
			child.getPartOf().setResource(parent);
			
			Patient patient = new Patient();
			patient.setId("Patient/FOO");
			patient.getManagingOrganization().setResource(child);

			ArrayList<Patient> retVal = new ArrayList<Patient>();
			retVal.add(patient);
			return retVal;
		}
		//@formatter:on


	}

}
