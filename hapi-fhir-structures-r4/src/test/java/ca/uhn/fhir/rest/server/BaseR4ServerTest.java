package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.api.BundleInclusionRule;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.jupiter.api.AfterEach;

public class BaseR4ServerTest {
	protected FhirContext myCtx = FhirContext.forR4Cached();
	private Server myServer;
	protected IGenericClient myClient;
	protected String myBaseUrl;

	@AfterEach
	public void after() throws Exception {
		JettyUtil.closeServer(myServer);
	}

	protected void startServer(Object theProvider) throws Exception {
		RestfulServer servlet = new RestfulServer(myCtx);
		servlet.registerProvider(theProvider);
		ServletHandler proxyHandler = new ServletHandler();
		servlet.setDefaultResponseEncoding(EncodingEnum.XML);
		servlet.setBundleInclusionRule(BundleInclusionRule.BASED_ON_RESOURCE_PRESENCE);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");

		myServer = new Server(0);
		myServer.setHandler(proxyHandler);
		JettyUtil.startServer(myServer);
		int port = JettyUtil.getPortForStartedServer(myServer);

		myBaseUrl = "http://localhost:" + port;
		myCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		myClient = myCtx.newRestfulGenericClient(myBaseUrl);
	}

}
