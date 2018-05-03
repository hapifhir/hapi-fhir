package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.VerboseLoggingInterceptor;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.ConceptMap;
import org.hl7.fhir.r4.model.IdType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ExportConceptMapToCsvCommandTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ExportConceptMapToCsvCommandTest.class);
	private static final String CM_URL = "http://example.com/my_concept_map";

	private static String ourBase;
	private static FhirContext ourCtx = FhirContext.forR4();
	private static int ourPort;
	private static Server ourServer;

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {
		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		ServletHandler servletHandler = new ServletHandler();

		RestfulServer restfulServer = new RestfulServer(ourCtx);
		restfulServer.registerInterceptor(new VerboseLoggingInterceptor());
		restfulServer.setResourceProviders(new DummyConceptMapResourceProvider());

		ServletHolder servletHolder = new ServletHolder(restfulServer);
		servletHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(servletHandler);

		ourServer.start();

		ourBase = "http://localhost:" + ourPort;
	}

	public static class DummyConceptMapResourceProvider implements IResourceProvider {
		@Create()
		public MethodOutcome createConceptMap(@ResourceParam ConceptMap theConceptMap) {
			// FIXME: Save.

			MethodOutcome retVal = new MethodOutcome();
			retVal.setId(new IdType("ConceptMap", "1", "1"));
			retVal.setResource(theConceptMap);
			retVal.setCreated(true);

			return retVal;
		}

		@Override
		public Class<? extends IBaseResource> getResourceType() {
			return ConceptMap.class;
		}
	}

	@Ignore
	@Test
	public void testServerAndDummyResourceProvider() {
		IGenericClient client = ourCtx.newRestfulGenericClient(ourBase);

		ConceptMap conceptMap = new ConceptMap();
		conceptMap.setUrl(CM_URL);

		MethodOutcome methodOutcome = client.create().resource(conceptMap).execute();
		assertTrue(methodOutcome.getCreated());
		assertEquals("http://localhost:" + ourPort + "/ConceptMap/1/_history/1", methodOutcome.getId().getValueAsString());

		conceptMap = (ConceptMap) methodOutcome.getResource();
		assertEquals(CM_URL, conceptMap.getUrl()); // FIXME: Why does this return null?
	}
}
