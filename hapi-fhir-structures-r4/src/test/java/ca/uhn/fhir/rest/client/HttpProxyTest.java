package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.collections.EnumerationUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpProxyTest {
	private static FhirContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(HttpProxyTest.class);
	private static HttpServletRequest ourRequest;
	private boolean myFirstRequest;
	private String myAuthHeader;

	@SuppressWarnings("serial")
	@Test
	public void testProxiedRequest() throws Exception {
		Server server = new Server(0);
		myFirstRequest = true;
		myAuthHeader = null;
		
		RestfulServer restServer = new RestfulServer(ourCtx) {

			@Override
			protected void doGet(HttpServletRequest theRequest, HttpServletResponse theResponse) throws ServletException, IOException {
				if (myFirstRequest) {
					theResponse.addHeader("Proxy-Authenticate", "Basic realm=\"some_realm\"");
					theResponse.setStatus(407);
					theResponse.getWriter().close();
					myFirstRequest = false;
					return;
				}
				String auth = theRequest.getHeader("Proxy-Authorization");
				if (auth != null) {
					myAuthHeader = auth;
				}
				
				super.doGet(theRequest, theResponse);
			}

		};
		restServer.setResourceProviders(new PatientResourceProvider());

		// ServletHandler proxyHandler = new ServletHandler();
		ServletHolder servletHolder = new ServletHolder(restServer);

		ServletContextHandler ch = new ServletContextHandler();
		ch.setContextPath("/rootctx/rcp2");
		ch.addServlet(servletHolder, "/fhirctx/fcp2/*");

		ContextHandlerCollection contexts = new ContextHandlerCollection();
		server.setHandler(contexts);

		server.setHandler(ch);
		JettyUtil.startServer(server);
        int port = JettyUtil.getPortForStartedServer(server);
		try {

			ourCtx.getRestfulClientFactory().setProxy("127.0.0.1", port);
			ourCtx.getRestfulClientFactory().setProxyCredentials("username", "password");
			
			String baseUri = "http://99.99.99.99:" + port + "/rootctx/rcp2/fhirctx/fcp2";
			IGenericClient client = ourCtx.newRestfulGenericClient(baseUri);

			IdType id = new IdType("Patient", "123");
			client.read().resource(Patient.class).withId(id).execute();

			assertEquals("Basic dXNlcm5hbWU6cGFzc3dvcmQ=", myAuthHeader);
			
		} finally {
			JettyUtil.closeServer(server);
		}

	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourCtx = FhirContext.forR4();
	}

	public static class PatientResourceProvider implements IResourceProvider {

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Read
		public Patient read(@IdParam IdType theId, HttpServletRequest theRequest) {
			Patient retVal = new Patient();
			retVal.setId(theId);
			ourRequest = theRequest;

			ourLog.info(EnumerationUtils.toList(ourRequest.getHeaderNames()).toString());
			ourLog.info("Proxy-Connection: " + EnumerationUtils.toList(ourRequest.getHeaders("Proxy-Connection")));
			ourLog.info("Host: " + EnumerationUtils.toList(ourRequest.getHeaders("Host")));
			ourLog.info("User-Agent: " + EnumerationUtils.toList(ourRequest.getHeaders("User-Agent")));

			return retVal;
		}

	}


	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
