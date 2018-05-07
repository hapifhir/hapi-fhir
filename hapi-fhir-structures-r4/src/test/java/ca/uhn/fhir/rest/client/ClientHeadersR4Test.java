package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.util.RandomServerPortProvider;
import ca.uhn.fhir.util.TestUtil;
import ca.uhn.fhir.util.VersionUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.Patient;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

public class ClientHeadersR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ClientHeadersR4Test.class);
	private static FhirContext ourCtx;
	private static Server ourServer;
	private static String ourServerBase;
	private static HashMap<String, List<String>> ourHeaders;
	private static IGenericClient ourClient;
	private static String ourMethod;

	@Before
	public void before() {
		ourHeaders = null;
		ourMethod = null;
	}

	private String expectedUserAgent() {
		return "HAPI-FHIR/" + VersionUtil.getVersion() + " (FHIR Client; FHIR " + FhirVersionEnum.R4.getFhirVersionString() + "/R4; apache)";
	}

	private byte[] extractBodyAsByteArray(ArgumentCaptor<HttpUriRequest> capt) throws IOException {
		byte[] body = IOUtils.toByteArray(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(0)).getEntity().getContent());
		return body;
	}

	private String extractBodyAsString(ArgumentCaptor<HttpUriRequest> capt) throws IOException {
		String body = IOUtils.toString(((HttpEntityEnclosingRequestBase) capt.getAllValues().get(0)).getEntity().getContent(), "UTF-8");
		return body;
	}


	@Test
	public void testCreateWithPreferRepresentationServerReturnsResource() throws Exception {

		final Patient resp1 = new Patient();
		resp1.setActive(true);

		MethodOutcome resp = ourClient.create().resource(resp1).execute();

		assertNotNull(resp);
		assertEquals(1, ourHeaders.get(Constants.HEADER_CONTENT_TYPE).size());
		assertEquals("application/fhir+xml; charset=UTF-8", ourHeaders.get(Constants.HEADER_CONTENT_TYPE).get(0));
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourCtx = FhirContext.forR4();

		int myPort = RandomServerPortProvider.findFreePort();
		ourServer = new Server(myPort);

		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		ourServerBase = "http://localhost:" + myPort + "/fhir/context";
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		ourClient = ourCtx.newRestfulGenericClient(ourServerBase);

		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(new TestServlet());
		proxyHandler.addServlet(servletHolder, "/fhir/context/*");

		ourServer.setHandler(proxyHandler);
		ourServer.start();

	}

	private static class TestServlet extends HttpServlet {

		@Override
		protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

			if (ourHeaders != null) {
				fail();
			}
			ourHeaders = new HashMap<>();
			ourMethod = req.getMethod();
			Enumeration<String> names = req.getHeaderNames();
			while (names.hasMoreElements()) {
				String nextName = names.nextElement();
				ourHeaders.put(nextName, new ArrayList<String>());
				Enumeration<String> values = req.getHeaders(nextName);
				while (values.hasMoreElements()) {
					ourHeaders.get(nextName).add(values.nextElement());
				}
			}

			resp.setStatus(200);
		}

	}

}
