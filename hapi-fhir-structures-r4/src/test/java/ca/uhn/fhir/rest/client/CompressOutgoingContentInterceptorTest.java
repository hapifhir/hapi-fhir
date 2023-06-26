package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.apache.GZipContentInterceptor;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompressOutgoingContentInterceptorTest {

	private static IGenericClient ourClient;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static Patient ourLastPatient;
	private static String ourLastReq;
	private static String ourLastResponseEncoding;
	private static int ourPort;
	private static Server ourServer;

	@BeforeEach
	public void before() {
		ourClient = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort);
	}


	@Test
	public void testCreate() {
		ourClient.registerInterceptor(new GZipContentInterceptor());

		Patient p = new Patient();
		p.addName().setFamily("FAMILY");

		ourClient.create().resource(p).execute();

		assertEquals("FAMILY", p.getName().get(0).getFamily());
		assertEquals("gzip", ourLastReq);
		assertEquals("gzip", ourLastResponseEncoding);
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
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {

		@Create
		public MethodOutcome create(HttpServletRequest theReq, @ResourceParam Patient thePatient) {
			ourLastReq = theReq.getHeader(Constants.HEADER_CONTENT_ENCODING.toLowerCase());
			ourLastResponseEncoding = theReq.getHeader(Constants.HEADER_ACCEPT_ENCODING.toLowerCase());
			ourLastPatient = thePatient;
			return new MethodOutcome(new IdDt("Patient", "1"));
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

	}

}
