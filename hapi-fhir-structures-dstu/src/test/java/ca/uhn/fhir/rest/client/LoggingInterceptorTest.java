package ca.uhn.fhir.rest.client;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URL;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.*;
import org.mockito.ArgumentMatcher;
import org.slf4j.LoggerFactory;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.util.PortUtil;
import ca.uhn.fhir.util.TestUtil;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.Appender;

public class LoggingInterceptorTest {

	private static FhirContext ourCtx = FhirContext.forDstu1();
	private static int ourPort;
	private static Server ourServer;
	private Logger myLoggerRoot;
	private Appender<ILoggingEvent> myMockAppender;

	@After
	public void after() {
		myLoggerRoot.detachAppender(myMockAppender);
	}

	@SuppressWarnings("unchecked")
	@Before
	public void before() {

		/*
		 * This is a bit funky, but it's useful for verifying that the headers actually get logged
		 */
		myMockAppender = mock(Appender.class);
		when(myMockAppender.getName()).thenReturn("MOCK");

		org.slf4j.Logger logger = LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);

		myLoggerRoot = (ch.qos.logback.classic.Logger) logger;
		myLoggerRoot.addAppender(myMockAppender);
	}

	@Test
	public void testLogger() throws Exception {
		System.out.println("Starting testLogger");
		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		
		LoggingInterceptor interceptor = new LoggingInterceptor(true);
		client.registerInterceptor(interceptor);
		Patient patient = client.read(Patient.class, "1");
		assertFalse(patient.getIdentifierFirstRep().isEmpty());

		verify(myMockAppender, times(1)).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
			@Override
			public boolean matches(final Object argument) {
				String formattedMessage = ((LoggingEvent) argument).getFormattedMessage();
				System.out.println("Verifying: " + formattedMessage);
				return formattedMessage.replace("; ", ";").toLowerCase().contains("Content-Type: application/xml+fhir;charset=utf-8".toLowerCase());
			}
		}));

		// Unregister the interceptor
		client.unregisterInterceptor(interceptor);
		
		patient = client.read(Patient.class, "1");
		assertFalse(patient.getIdentifierFirstRep().isEmpty());

		verify(myMockAppender, times(1)).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
			@Override
			public boolean matches(final Object argument) {
				String formattedMessage = ((LoggingEvent) argument).getFormattedMessage();
				System.out.println("Verifying: " + formattedMessage);
				return formattedMessage.replace("; ", ";").toLowerCase().contains("Content-Type: application/xml+fhir;charset=utf-8".toLowerCase());
			}
		}));

	}

	@AfterClass
	public static void afterClassClearContext() throws Exception {
		ourServer.stop();
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

	@BeforeClass
	public static void beforeClass() throws Exception {

		URL conf = LoggingInterceptor.class.getResource("/logback-test-dstuforce.xml");
		assertNotNull(conf);
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(context);
		context.reset();
		configurator.doConfigure(conf);

		ourPort = PortUtil.findFreePort();
		ourServer = new Server(ourPort);

		DummyProvider patientProvider = new DummyProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
	}


	/**
	 * Created by dsotnikov on 2/25/2014.
	 */
	public static class DummyProvider implements IResourceProvider {

		@Read(version = true)
		public Patient findPatient(@IdParam IdDt theId) {
			Patient patient = new Patient();
			patient.addIdentifier(theId.getIdPart(), theId.getVersionIdPart());
			patient.setId("Patient/1/_history/1");
			return patient;
		}

		@Override
		public Class<? extends IResource> getResourceType() {
			return Patient.class;
		}

	}

}
