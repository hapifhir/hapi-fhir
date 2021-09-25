package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.api.IBasicClient;
import ca.uhn.fhir.rest.client.impl.HttpBasicAuthInterceptor;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.apache.commons.lang3.Validate;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientIntegrationTest {
	private Server myServer;
	private MyPatientResourceProvider myPatientProvider;
	private static FhirContext ourCtx = FhirContext.forR4();

	@BeforeEach
	public void before() {
		myServer = new Server(0);

		myPatientProvider = new MyPatientResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(myPatientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		myServer.setHandler(proxyHandler);

	}

	@SuppressWarnings("deprecation")
	@Test
	public void testClientSecurity() throws Exception {

		JettyUtil.startServer(myServer);
        int myPort = JettyUtil.getPortForStartedServer(myServer);

		FhirContext ctx = FhirContext.forR4();

		HttpClientBuilder builder = HttpClientBuilder.create();
		// PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(5000, TimeUnit.MILLISECONDS);
		// builder.setConnectionManager(connectionManager);
		builder.addInterceptorFirst(new HttpBasicAuthInterceptor("foobar", "boobear"));

		CloseableHttpClient httpClient = builder.build();
		ctx.getRestfulClientFactory().setHttpClient(httpClient);

		PatientClient client = ctx.newRestfulClient(PatientClient.class, "http://localhost:" + myPort + "/");

		List<Patient> actualPatients = client.searchForPatients(new StringDt("AAAABBBB"));
		assertEquals(1, actualPatients.size());
		assertEquals("AAAABBBB", actualPatients.get(0).getNameFirstRep().getFamily());

		assertEquals("Basic Zm9vYmFyOmJvb2JlYXI=", myPatientProvider.getAuthorizationHeader());
	}

	@AfterEach
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);
	}

	public static class MyPatientResourceProvider implements IResourceProvider {
		private String myAuthorizationHeader;

		public String getAuthorizationHeader() {
			return myAuthorizationHeader;
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

		@Search
		public List<Patient> searchForPatients(@RequiredParam(name = "fooParam") StringDt theFooParam, HttpServletRequest theRequest, HttpServletResponse theResponse) {
			Validate.notNull(theRequest);
			Validate.notNull(theResponse);

			myAuthorizationHeader = theRequest.getHeader("authorization");

			Patient retVal = new Patient();
			retVal.setId("1");
			retVal.addName().setFamily(theFooParam.getValue());
			return Collections.singletonList(retVal);
		}

	}

	private static interface PatientClient extends IBasicClient {

		@Search
		public List<Patient> searchForPatients(@RequiredParam(name = "fooParam") StringDt theFooParam);

	}


	@AfterAll
	public static void afterClassClearContext() {
		TestUtil.randomizeLocaleAndTimezone();
	}

}
