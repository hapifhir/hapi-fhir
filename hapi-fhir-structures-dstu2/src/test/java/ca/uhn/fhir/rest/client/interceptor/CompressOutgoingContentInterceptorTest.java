package ca.uhn.fhir.rest.client.interceptor;

import static org.junit.Assert.*;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.apache.GZipContentInterceptor;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.util.PortUtil;

public class CompressOutgoingContentInterceptorTest {

	private static IGenericClient ourClient;
	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static Patient ourLastPatient;
	private static String ourLastReq;
	private static String ourLastResponseEncoding;
	private static int ourPort;
	private static Server ourServer;

	@Before
	public void before() {
		ourClient = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort);
	}

	@Test
	public void testCreate() throws Exception {
		ourClient.registerInterceptor(new GZipContentInterceptor());

		Patient p = new Patient();
		p.addName().addFamily("FAMILY");

		ourClient.create().resource(p).execute();

		assertEquals("FAMILY", p.getName().get(0).getFamily().get(0).getValue());
		assertEquals("gzip", ourLastReq);
		assertEquals("gzip", ourLastResponseEncoding);
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
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setPagingProvider(new FifoMemoryPagingProvider(10));

		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

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
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

	}

}
