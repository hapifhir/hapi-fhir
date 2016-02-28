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
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.util.PortUtil;

public class ReadDstu2Test {

	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static boolean ourInitializeProfileList;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ReadDstu2Test.class);
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer ourServlet;

	@Before
	public void before() {
		ourServlet.setAddProfileTag(AddProfileTagEnum.NEVER);
		ourInitializeProfileList = false;
	}

	/**
	 * See #302
	 */
	@Test
	public void testAddProfile() throws Exception {
		ourServlet.setAddProfileTag(AddProfileTagEnum.ALWAYS);
		
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123&_format=xml");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("p1ReadValue"));
		assertThat(responseContent, containsString("p1ReadId"));
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"p1ReadId\"/><meta><profile value=\"http://foo_profile\"/></meta><identifier><value value=\"p1ReadValue\"/></identifier></Patient>", responseContent);
		
		ourLog.info(responseContent);
	}

	
	/**
	 * See #302
	 */
	@Test
	public void testAddProfileToExistingList() throws Exception {
		ourServlet.setAddProfileTag(AddProfileTagEnum.ALWAYS);
		ourInitializeProfileList = true;
		
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123&_format=xml");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("p1ReadValue"));
		assertThat(responseContent, containsString("p1ReadId"));
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"p1ReadId\"/><meta><profile value=\"http://foo\"/><profile value=\"http://foo_profile\"/></meta><identifier><value value=\"p1ReadValue\"/></identifier></Patient>", responseContent);
		
		ourLog.info(responseContent);
	}
	
	/**
	 * In DSTU2+ the resource ID appears in the resource body
	 */
	@Test
	public void testReadJson() throws Exception {
		ourServlet.setAddProfileTag(AddProfileTagEnum.ALWAYS);
		
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123?_format=json");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		ourLog.info(responseContent);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("p1ReadValue"));
		assertThat(responseContent, containsString("p1ReadId"));
		assertThat(responseContent, containsString("\"meta\":{\"profile\":[\"http://foo_profile\"]}"));
	}

	/**
	 * In DSTU2+ the resource ID appears in the resource body
	 */
	@Test
	public void testReadXml() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123&_format=xml");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent());
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("p1ReadValue"));
		assertThat(responseContent, containsString("p1ReadId"));
		
		ourLog.info(responseContent);
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
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setFhirContext(ourCtx);
		ourServlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(ourServlet);
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
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdDt theId) {
			Patient p1 = new MyPatient();
			p1.setId("p1ReadId");
			p1.addIdentifier().setValue("p1ReadValue");
			if (ourInitializeProfileList) {
				List<IdDt> profiles = new ArrayList<IdDt>();
				profiles.add(new IdDt("http://foo"));
				ResourceMetadataKeyEnum.PROFILES.put(p1, profiles);
			}
			return p1;
		}

	}

	@ResourceDef(name = "Patient", profile = "http://foo_profile")
	public static class MyPatient extends Patient {

		private static final long serialVersionUID = 1L;

	}

}
