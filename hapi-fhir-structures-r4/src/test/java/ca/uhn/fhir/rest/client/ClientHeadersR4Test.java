package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.RequestFormatParamStyleEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;


public class ClientHeadersR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ClientHeadersR4Test.class);
	private static FhirContext ourCtx;
	private static Server ourServer;
	private static String ourServerBase;
	private static HashMap<String, List<String>> ourHeaders;
	private static HashMap<String, String[]> ourParams;
	private static String ourMethod;
	private IGenericClient myClient;

	@BeforeEach
	public void before() {
		ourHeaders = null;
		ourMethod = null;
	}


	@Test
	public void testReadXml() {
		myClient
			.read()
			.resource("Patient")
			.withId(123L)
			.encodedXml()
			.execute();

		assertThat(ourHeaders.get(Constants.HEADER_ACCEPT).get(0)).isEqualTo("application/fhir+xml;q=1.0, application/xml+fhir;q=0.9");
		assertThat(ourParams.get(Constants.PARAM_FORMAT)[0]).isEqualTo("xml");
	}

	@Test
	public void testReadXmlNoParam() {
		myClient.setFormatParamStyle(RequestFormatParamStyleEnum.NONE);
		myClient
			.read()
			.resource("Patient")
			.withId(123L)
			.encodedXml()
			.execute();

		assertThat(ourHeaders.get(Constants.HEADER_ACCEPT).get(0)).isEqualTo("application/fhir+xml;q=1.0, application/xml+fhir;q=0.9");
		assertThat(ourParams.get(Constants.PARAM_FORMAT)).isNull();
	}

	@Test
	public void testReadJson() {
		myClient
			.read()
			.resource("Patient")
			.withId(123L)
			.encodedJson()
			.execute();

		assertThat(ourHeaders.get(Constants.HEADER_ACCEPT).get(0)).isEqualTo("application/fhir+json;q=1.0, application/json+fhir;q=0.9");
		assertThat(ourParams.get(Constants.PARAM_FORMAT)[0]).isEqualTo("json");
	}

	@Test
	public void testReadJsonNoParam() {
		myClient.setFormatParamStyle(RequestFormatParamStyleEnum.NONE);
		myClient
			.read()
			.resource("Patient")
			.withId(123L)
			.encodedJson()
			.execute();

		assertThat(ourHeaders.get(Constants.HEADER_ACCEPT).get(0)).isEqualTo("application/fhir+json;q=1.0, application/json+fhir;q=0.9");
		assertThat(ourParams.get(Constants.PARAM_FORMAT)).isNull();
	}

	@Test
	public void testReadXmlDisable() {
		myClient
			.read()
			.resource("Patient")
			.withId(123L)
			.encodedXml()
			.execute();

		assertThat(ourHeaders.get(Constants.HEADER_ACCEPT).get(0)).isEqualTo("application/fhir+xml;q=1.0, application/xml+fhir;q=0.9");
		assertThat(ourParams.get(Constants.PARAM_FORMAT)[0]).isEqualTo("xml");
	}

	@Test
	public void testCreateWithPreferRepresentationServerReturnsResource() {

		final Patient resp1 = new Patient();
		resp1.setActive(true);

		MethodOutcome resp = myClient.create().resource(resp1).execute();

		assertThat(resp).isNotNull();
		assertThat(ourHeaders.get(Constants.HEADER_CONTENT_TYPE)).hasSize(1);
		assertThat(ourHeaders.get(Constants.HEADER_CONTENT_TYPE).get(0)).isEqualTo("application/fhir+json; charset=UTF-8");
	}

	@BeforeEach
	public void beforeCreateClient() {
		myClient = ourCtx.newRestfulGenericClient(ourServerBase);
	}

	private static class TestServlet extends HttpServlet {

		@Override
		protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

			if (ourHeaders != null) {
				fail("");			}
			ourHeaders = new HashMap<>();
			ourParams = new HashMap<>(req.getParameterMap());
			ourMethod = req.getMethod();
			Enumeration<String> names = req.getHeaderNames();
			while (names.hasMoreElements()) {
				String nextName = names.nextElement();
				ourHeaders.put(nextName, new ArrayList<>());
				Enumeration<String> values = req.getHeaders(nextName);
				while (values.hasMoreElements()) {
					ourHeaders.get(nextName).add(values.nextElement());
				}
			}

			resp.setStatus(200);

			if (req.getMethod().equals("GET")) {
				resp.setContentType("application/json");
				resp.getWriter().append("{\"resourceType\":\"Patient\"}");
				resp.getWriter().close();
			}

		}

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
        JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourCtx = FhirContext.forR4();

		ourServer = new Server(0);

		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(new TestServlet());
		proxyHandler.addServlet(servletHolder, "/fhir/context/*");

		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        int myPort = JettyUtil.getPortForStartedServer(ourServer);
        ourServerBase = "http://localhost:" + myPort + "/fhir/context";

	}

}
