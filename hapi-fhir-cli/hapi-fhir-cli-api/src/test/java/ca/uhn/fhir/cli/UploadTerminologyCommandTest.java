package ca.uhn.fhir.cli;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.term.IHapiTerminologyLoaderSvc;
import ca.uhn.fhir.jpa.term.IHapiTerminologySvc;
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UploadTerminologyCommandTest {

	private Server myServer;
	private FhirContext myCtx = FhirContext.forR4();
	@Mock
	private IHapiTerminologyLoaderSvc myTerminologyLoaderSvc;
	@Mock
	private IHapiTerminologySvc myTerminologySvc;
	private int myPort;

	@Test
	public void testTerminologyUploadDelta() {

//		App.main();

	}

	@After
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);
	}

	@Before
	public void start() throws Exception {
		myServer = new Server(0);

		TerminologyUploaderProvider provider = new TerminologyUploaderProvider(myCtx, myTerminologyLoaderSvc, myTerminologySvc);

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(myCtx);
		servlet.registerProvider(provider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		myServer.setHandler(proxyHandler);
		JettyUtil.startServer(myServer);
		myPort = JettyUtil.getPortForStartedServer(myServer);

	}
	

}
