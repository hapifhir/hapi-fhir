package ca.uhn.fhir.rest.client.apache;

import static org.junit.Assert.assertEquals;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.rest.annotation.OptionalParam;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.interceptor.VerboseLoggingInterceptor;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class ApacheClientIntegrationTest {

	private static FhirContext ourCtx = FhirContext.forDstu2();
	private static String ourLastMethod;
	private static StringParam ourLastName;
	private static int ourPort;
	private static Server ourServer;
	private static String ourBase;

	@Before
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
	
	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		
		servlet.registerInterceptor(new VerboseLoggingInterceptor());

		servlet.setResourceProviders(new DummyPatientResourceProvider());
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();
		
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
