package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.interceptor.VerboseLoggingInterceptor;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.ConceptMap;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

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
		restfulServer.setResourceProviders(new HashMapResourceProvider<>(ourCtx, ConceptMap.class));

		ServletHolder servletHolder = new ServletHolder(restfulServer);
		servletHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(servletHandler);

		ourServer.start();

		ourBase = "http://localhost:" + ourPort;

		IGenericClient client = ourCtx.newRestfulGenericClient(ourBase);

		ConceptMap conceptMap = new ConceptMap();
		conceptMap.setUrl(CM_URL);

		client.create().resource(conceptMap).execute();
	}

	@Test
	public void testServer() {
		App.main(new String[] {"export-conceptmap-to-csv",
			"-v", "r4",
			"-t", ourBase,
			"-u", CM_URL,
			"-f", "diederik.csv"});
	}
}
