package ca.uhn.fhir.rest.server;

import static org.junit.Assert.*;

import javax.servlet.ServletException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Binary;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;

public class ResourceProviderWithNoMethodsTest {

	private Server ourServer;
	private static FhirContext ourCtx = FhirContext.forDstu1();
	
	@After
	public void after() throws Exception {
		ourServer.stop();
	}

	@Test
	public void testNoAnnotatedMethods() throws Exception {
		int port = PortUtil.findFreePort();
		ourServer = new Server(port);

		ResourceProvider patientProvider = new ResourceProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);

		try {
			ourServer.start();
			fail();
		} catch (ServletException e) {
			assertEquals(e.getCause().getClass(), ConfigurationException.class);
		}
	}

	@Test
	public void testNonPublicMethod() throws Exception {
		int port = PortUtil.findFreePort();
		ourServer = new Server(port);

		NonPublicMethodProvider patientProvider = new NonPublicMethodProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);

		try {
			ourServer.start();
			fail();
		} catch (ServletException e) {
			assertEquals(e.getCause().getClass(), ConfigurationException.class);
		}
	}

	public static class ResourceProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Binary.class;
		}

		public Binary read(@IdParam IdDt theId) {
			Binary retVal = new Binary();
			retVal.setContent(new byte[] { 1, 2, 3, 4 });
			retVal.setContentType(theId.getIdPart());
			return retVal;
		}

	}

	public static class NonPublicMethodProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return Binary.class;
		}

		@Read
		protected Binary read(@IdParam IdDt theId) {
			Binary retVal = new Binary();
			retVal.setContent(new byte[] { 1, 2, 3, 4 });
			retVal.setContentType(theId.getIdPart());
			return retVal;
		}

	}


	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
