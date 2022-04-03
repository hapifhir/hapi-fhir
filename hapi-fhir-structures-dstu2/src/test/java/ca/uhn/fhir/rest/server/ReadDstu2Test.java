package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.AddProfileTagEnum;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.DateUtils;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReadDstu2Test {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ReadDstu2Test.class);
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static boolean ourInitializeProfileList;
	private static IdDt ourLastId;
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer ourServlet;

	@BeforeEach
	public void before() {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.NEVER);
		ourInitializeProfileList = false;
		ourLastId = null;
	}

	@Test
	public void testIfModifiedSince() throws Exception {

		CloseableHttpResponse status;
		HttpGet httpGet;

		// Fixture was last modified at 2012-01-01T12:12:12Z
		// thus it has changed before the later time of 2012-01-01T13:00:00Z
		// so we expect a 304
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
		httpGet.addHeader(Constants.HEADER_IF_MODIFIED_SINCE, DateUtils.formatDate(new InstantDt("2012-01-01T13:00:00Z").getValue()));
		status = ourClient.execute(httpGet);
		try {
			assertEquals(304, status.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(status);
		}

		// Fixture was last modified at 2012-01-01T12:12:12Z
		// thus it has changed at the same time of 2012-01-01T12:12:12Z
		// so we expect a 304
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
		httpGet.addHeader(Constants.HEADER_IF_MODIFIED_SINCE, DateUtils.formatDate(new InstantDt("2012-01-01T12:12:12Z").getValue()));
		status = ourClient.execute(httpGet);
		try {
			assertEquals(304, status.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(status);
		}

		// Fixture was last modified at 2012-01-01T12:12:12Z
		// thus it has changed after the earlier time of 2012-01-01T10:00:00Z
		// so we expect a 200
		httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/2");
		httpGet.addHeader(Constants.HEADER_IF_MODIFIED_SINCE, DateUtils.formatDate(new InstantDt("2012-01-01T10:00:00Z").getValue()));
		status = ourClient.execute(httpGet);
		try {
			assertEquals(200, status.getStatusLine().getStatusCode());
		} finally {
			IOUtils.closeQuietly(status);
		}

	}

	/**
	 * See #302
	 */
	@Test
	public void testAddProfile() throws Exception {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.ONLY_FOR_CUSTOM);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123?_format=xml");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Constants.CHARSET_UTF8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("p1ReadValue"));
		assertThat(responseContent, containsString("p1ReadId"));
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"p1ReadId\"/><meta><lastUpdated value=\"2012-01-01T12:12:12Z\"/><profile value=\"http://foo_profile\"/></meta><identifier><value value=\"p1ReadValue\"/></identifier></Patient>", responseContent);

		ourLog.info(responseContent);

		assertEquals("Patient/123", ourLastId.getValue());
	}

	/**
	 * See #302 and #268
	 */
	@Test
	public void testAddProfileToExistingList() throws Exception {
		ourInitializeProfileList = true;
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.ONLY_FOR_CUSTOM);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123&_format=xml");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Constants.CHARSET_UTF8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		ourLog.info(responseContent);

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("p1ReadValue"));
		assertThat(responseContent, containsString("p1ReadId"));
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"p1ReadId\"/><meta><lastUpdated value=\"2012-01-01T12:12:12Z\"/><profile value=\"http://foo\"/><profile value=\"http://foo_profile\"/></meta><identifier><value value=\"p1ReadValue\"/></identifier></Patient>", responseContent);
	}

	/**
	 * In DSTU2+ the resource ID appears in the resource body
	 */
	@Test
	public void testReadJson() throws Exception {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.ONLY_FOR_CUSTOM);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123?_format=json");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Constants.CHARSET_UTF8);
		ourLog.info(responseContent);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("p1ReadValue"));
		assertThat(responseContent, containsString("p1ReadId"));
		assertThat(responseContent, containsString("\"meta\":{\"lastUpdated\":\"2012-01-01T12:12:12Z\",\"profile\":[\"http://foo_profile\"]}"));
	}

	/**
	 * In DSTU2+ the resource ID appears in the resource body
	 */
	@Test
	public void testReadXml() throws Exception {
		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123&_format=xml");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Constants.CHARSET_UTF8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("p1ReadValue"));
		assertThat(responseContent, containsString("p1ReadId"));

		ourLog.info(responseContent);
	}

	@Test
	public void testVread() throws Exception {
		ourCtx.setAddProfileTagWhenEncoding(AddProfileTagEnum.ONLY_FOR_CUSTOM);

		HttpGet httpGet = new HttpGet("http://localhost:" + ourPort + "/Patient/123/_history/1");
		HttpResponse status = ourClient.execute(httpGet);
		String responseContent = IOUtils.toString(status.getEntity().getContent(), Constants.CHARSET_UTF8);
		IOUtils.closeQuietly(status.getEntity().getContent());

		assertEquals(200, status.getStatusLine().getStatusCode());
		assertThat(responseContent, containsString("p1ReadValue"));
		assertThat(responseContent, containsString("p1ReadId"));
		assertEquals("<Patient xmlns=\"http://hl7.org/fhir\"><id value=\"p1ReadId\"/><meta><lastUpdated value=\"2012-01-01T12:12:12Z\"/><profile value=\"http://foo_profile\"/></meta><identifier><value value=\"p1ReadValue\"/></identifier></Patient>", responseContent);

		ourLog.info(responseContent);

		assertEquals("Patient/123/_history/1", ourLastId.getValue());
		assertEquals("123", ourLastId.getIdPart());
		assertEquals("1", ourLastId.getVersionIdPart());
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

		@Read(version = true)
		public Patient read(@IdParam IdDt theId) {
			ourLastId = theId;
			Patient p1 = new MyPatient();
			ResourceMetadataKeyEnum.UPDATED.put(p1, new InstantDt("2012-01-01T12:12:12Z"));

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
		ourServlet = new RestfulServer(ourCtx);
		ourServlet.setFhirContext(ourCtx);
		ourServlet.setResourceProviders(patientProvider);
		ourServlet.setDefaultResponseEncoding(EncodingEnum.XML);

		ServletHolder servletHolder = new ServletHolder(ourServlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
		ourPort = JettyUtil.getPortForStartedServer(ourServer);

		PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		HttpClientBuilder builder = HttpClientBuilder.create();
		builder.setConnectionManager(connectionManager);
		ourClient = builder.build();

	}

}
