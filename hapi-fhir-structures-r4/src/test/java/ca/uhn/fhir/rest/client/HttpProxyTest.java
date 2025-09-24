package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.TestUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections.EnumerationUtils;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("SpellCheckingInspection")
public class HttpProxyTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(HttpProxyTest.class);
	/**
	 * Don't use the cached FhirContext here because we modify the
	 * REST Client Factory!
	 */
	private final FhirContext ourCtx = FhirContext.forR4();
	private final MyProxySimulatingFilter myProxySimulatingFilter = new MyProxySimulatingFilter();

	@SuppressWarnings({"JUnitMalformedDeclaration", "SpellCheckingInspection"})
	@RegisterExtension
	private final RestfulServerExtension myServer = new RestfulServerExtension(ourCtx)
		 .withServletFilter(myProxySimulatingFilter)
		 .registerProvider(new PatientResourceProvider())
		 .withContextPath("/rootctx/rcp2")
		 .withServletPath("/fhirctx/fcp2/*");

	@Test
	public void testProxiedRequest() {
		int port = myServer.getPort();
		ourCtx.getRestfulClientFactory().setProxy("127.0.0.1", port);
		ourCtx.getRestfulClientFactory().setProxyCredentials("username", "password");

		String baseUri = "http://99.99.99.99:" + port + "/rootctx/rcp2/fhirctx/fcp2";
		IGenericClient client = ourCtx.newRestfulGenericClient(baseUri);

		IdType id = new IdType("Patient", "123");
		client.read().resource(Patient.class).withId(id).execute();

		assertEquals("Basic dXNlcm5hbWU6cGFzc3dvcmQ=", myProxySimulatingFilter.myAuthHeader);
	}

	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

	public static class MyProxySimulatingFilter extends HttpFilter {

		private final AtomicBoolean myFirstRequest = new AtomicBoolean(true);
		private String myAuthHeader;

		@Override
		protected void doFilter(HttpServletRequest theRequest, HttpServletResponse theResponse, FilterChain theChain) throws IOException, ServletException {
			if (myFirstRequest.get()) {
				myFirstRequest.set(false);
				theResponse.addHeader("Proxy-Authenticate", "Basic realm=\"some_realm\"");
				theResponse.setStatus(407);
				theResponse.getWriter().close();
				return;
			}
			String auth = theRequest.getHeader("Proxy-Authorization");
			if (auth != null) {
				myAuthHeader = auth;
			}

			theChain.doFilter(theRequest, theResponse);
		}
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

			ourLog.info(EnumerationUtils.toList(theRequest.getHeaderNames()).toString());
			ourLog.info("Proxy-Connection: {}",  EnumerationUtils.toList(theRequest.getHeaders("Proxy-Connection")));
			ourLog.info("Host: {}", EnumerationUtils.toList(theRequest.getHeaders("Host")));
			ourLog.info("User-Agent: {}", EnumerationUtils.toList(theRequest.getHeaders("User-Agent")));

			return retVal;
		}

	}

}
