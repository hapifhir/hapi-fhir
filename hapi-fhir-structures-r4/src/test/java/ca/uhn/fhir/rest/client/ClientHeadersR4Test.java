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
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

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

		assertEquals("application/fhir+xml;q=1.0, application/xml+fhir;q=0.9", ourHeaders.get(Constants.HEADER_ACCEPT).get(0));
		assertEquals("xml", ourParams.get(Constants.PARAM_FORMAT)[0]);
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

		assertEquals("application/fhir+xml;q=1.0, application/xml+fhir;q=0.9", ourHeaders.get(Constants.HEADER_ACCEPT).get(0));
		assertEquals(null, ourParams.get(Constants.PARAM_FORMAT));
	}

	@Test
	public void testReadJson() {
		myClient
			.read()
			.resource("Patient")
			.withId(123L)
			.encodedJson()
			.execute();

		assertEquals("application/fhir+json;q=1.0, application/json+fhir;q=0.9", ourHeaders.get(Constants.HEADER_ACCEPT).get(0));
		assertEquals("json", ourParams.get(Constants.PARAM_FORMAT)[0]);
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

		assertEquals("application/fhir+json;q=1.0, application/json+fhir;q=0.9", ourHeaders.get(Constants.HEADER_ACCEPT).get(0));
		assertEquals(null, ourParams.get(Constants.PARAM_FORMAT));
	}

	@Test
	public void testReadXmlDisable() {
		myClient
			.read()
			.resource("Patient")
			.withId(123L)
			.encodedXml()
			.execute();

		assertEquals("application/fhir+xml;q=1.0, application/xml+fhir;q=0.9", ourHeaders.get(Constants.HEADER_ACCEPT).get(0));
		assertEquals("xml", ourParams.get(Constants.PARAM_FORMAT)[0]);
	}

	@Test
	public void testCreateWithPreferRepresentationServerReturnsResource() {

		final Patient resp1 = new Patient();
		resp1.setActive(true);

		MethodOutcome resp = myClient.create().resource(resp1).execute();

		assertNotNull(resp);
		assertEquals(1, ourHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertEquals("application/fhir+json; charset=UTF-8", ourHeaders.get(Constants.HEADER_CONTENT_TYPE).get(0));
	}

	@BeforeEach
	public void beforeCreateClient() {
		myClient = ourCtx.newRestfulGenericClient(ourServerBase);
	}

	private static class TestServlet extends HttpServlet {

		@Override
		protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {

			if (ourHeaders != null) {
				fail();
			}
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
