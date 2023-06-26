package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.UrlUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InjectionAttackTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(InjectionAttackTest.class);
	private static CloseableHttpClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static int ourPort;
	private static Server ourServer;
	private static RestfulServer ourServlet;

	@Test
	public void testPreventHtmlInjectionViaInvalidContentType() throws Exception {
		String requestUrl = "http://localhost:" +
			ourPort +
			"/Patient/123";

		// XML HTML
		HttpGet httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, "application/<script>");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertThat(responseContent, not(containsString("<script>")));
		}
	}

	@Test
	public void testPreventHtmlInjectionViaInvalidParameterName() throws Exception {
		String requestUrl = "http://localhost:" +
			ourPort +
			"/Patient?a" +
			UrlUtil.escapeUrlParam("<script>") +
			"=123";

		// XML HTML
		HttpGet httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML + ", " + Constants.CT_FHIR_XML_NEW);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertEquals(400, status.getStatusLine().getStatusCode());
			assertThat(responseContent, not(containsString("<script>")));
			assertEquals("text/html", status.getFirstHeader("Content-Type").getValue().toLowerCase().replaceAll(";.*", "").trim());
		}

		// JSON HTML
		httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML + ", " + Constants.CT_FHIR_JSON_NEW);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertEquals(400, status.getStatusLine().getStatusCode());
			assertThat(responseContent, not(containsString("<script>")));
			assertEquals("text/html", status.getFirstHeader("Content-Type").getValue().toLowerCase().replaceAll(";.*", "").trim());
		}

		// XML HTML
		httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML_NEW);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertEquals(400, status.getStatusLine().getStatusCode());
			assertThat(responseContent, not(containsString("<script>")));
			assertEquals(Constants.CT_FHIR_XML_NEW, status.getFirstHeader("Content-Type").getValue().toLowerCase().replaceAll(";.*", "").trim());
		}

		// JSON Plain
		httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertEquals(400, status.getStatusLine().getStatusCode());
			assertThat(responseContent, not(containsString("<script>")));
			assertEquals(Constants.CT_FHIR_JSON_NEW, status.getFirstHeader("Content-Type").getValue().toLowerCase().replaceAll(";.*", "").trim());
		}
	}

	@Test
	public void testPreventHtmlInjectionViaInvalidResourceType() throws Exception {
		String requestUrl = "http://localhost:" +
			ourPort +
			"/AA" +
			UrlUtil.escapeUrlParam("<script>");

		// XML HTML
		HttpGet httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML + ", " + Constants.CT_FHIR_XML_NEW);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertEquals(404, status.getStatusLine().getStatusCode());
			assertThat(responseContent, not(containsString("<script>")));
			assertEquals("text/html", status.getFirstHeader("Content-Type").getValue().toLowerCase().replaceAll(";.*", "").trim());
		}

		// JSON HTML
		httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_HTML + ", " + Constants.CT_FHIR_JSON_NEW);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertEquals(404, status.getStatusLine().getStatusCode());
			assertThat(responseContent, not(containsString("<script>")));
			assertEquals("text/html", status.getFirstHeader("Content-Type").getValue().toLowerCase().replaceAll(";.*", "").trim());
		}

		// XML HTML
		httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_XML_NEW);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertEquals(404, status.getStatusLine().getStatusCode());
			assertThat(responseContent, not(containsString("<script>")));
			assertEquals(Constants.CT_FHIR_XML_NEW, status.getFirstHeader("Content-Type").getValue().toLowerCase().replaceAll(";.*", "").trim());
		}

		// JSON Plain
		httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, Constants.CT_FHIR_JSON_NEW);
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertEquals(404, status.getStatusLine().getStatusCode());
			assertThat(responseContent, not(containsString("<script>")));
			assertEquals(Constants.CT_FHIR_JSON_NEW, status.getFirstHeader("Content-Type").getValue().toLowerCase().replaceAll(";.*", "").trim());
		}
	}

	@Test
	public void testPreventHtmlInjectionViaInvalidTokenParamModifier() throws Exception {
		String requestUrl = "http://localhost:" +
			ourPort +
			"/Patient?identifier:" +
			UrlUtil.escapeUrlParam("<script>") +
			"=123";
		HttpGet httpGet = new HttpGet(requestUrl);
		httpGet.addHeader(Constants.HEADER_ACCEPT, "application/<script>");
		try (CloseableHttpResponse status = ourClient.execute(httpGet)) {
			String responseContent = IOUtils.toString(status.getEntity().getContent(), Charsets.UTF_8);
			ourLog.info(responseContent);

			assertEquals(200, status.getStatusLine().getStatusCode());
			assertThat(responseContent, not(containsString("<script>")));
		}

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
		ourServlet.registerInterceptor(new ResponseHighlighterInterceptor());
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

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends Patient> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdType theId) {
			Patient patient = new Patient();
			patient.setId(theId);
			patient.setActive(true);
			return patient;
		}

		@Search
		public List<Patient> search(@OptionalParam(name = "identifier") TokenParam theToken) {
			return new ArrayList<>();
		}


	}

}
