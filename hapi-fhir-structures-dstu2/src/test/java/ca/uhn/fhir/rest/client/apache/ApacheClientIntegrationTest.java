package ca.uhn.fhir.rest.client.apache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.VerboseLoggingInterceptor;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApacheClientIntegrationTest {

	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static String ourLastMethod;
	private static StringParam ourLastName;
	private static int ourPort;
	private static Server ourServer;
	private static String ourBase;

	@BeforeEach
	public void before() {
		ourLastMethod = null;
		ourLastName = null;
	}


	@Test
	public void testSearchWithParam() throws Exception {
		
		IGenericClient client = ourCtx.newRestfulGenericClient(ourBase);
		
		Bundle response = client.search().forResource(Patient.class).where(Patient.NAME.matches().value("FOO")).returnBundle(Bundle.class).execute();
		assertEquals("search", ourLastMethod);
		assertEquals("FOO", ourLastName.getValue());
		assertEquals(1, response.getEntry().size());
		assertEquals("123", response.getEntry().get(0).getResource().getIdElement().getIdPart());
	}
	
	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {
		ourServer = new Server(0);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		
		servlet.registerInterceptor(new VerboseLoggingInterceptor());

		servlet.setResourceProviders(new DummyPatientResourceProvider());
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);
		
		ourBase = "http://localhost:" + ourPort;
	}

	public static class DummyPatientResourceProvider implements IResourceProvider {
		
		//@formatter:off
		@Search()
		public List<IBaseResource> search(@OptionalParam(name=Patient.SP_NAME) StringParam theName) {
			ourLastMethod = "search";
			ourLastName = theName;
			
			List<IBaseResource> retVal = new ArrayList<IBaseResource>();
			Patient patient = new Patient();
			patient.setId("123");
			patient.addName().addGiven("GIVEN");
			retVal.add(patient);
			return retVal;
		}
		//@formatter:on

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return Patient.class;
		}

	}

}
