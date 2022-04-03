package ca.uhn.fhir.rest.client;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.api.ServerValidationModeEnum;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import ca.uhn.fhir.util.TestUtil;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;
import org.slf4j.LoggerFactory;

import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoggingInterceptorTest {

	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static int ourPort;
	private static Server ourServer;
	private Logger myLoggerRoot;
	private Appender<ILoggingEvent> myMockAppender;

	@AfterEach
	public void after() {
		myLoggerRoot.detachAppender(myMockAppender);
	}

	@SuppressWarnings("unchecked")
	@BeforeEach
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
	public void testLoggerNonVerbose() {
		System.out.println("Starting testLogger");
		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);

		LoggingInterceptor interceptor = new LoggingInterceptor(false);
		client.registerInterceptor(interceptor);
		Patient patient = client.read(Patient.class, "1");
		assertFalse(patient.getIdentifierFirstRep().isEmpty());

		verify(myMockAppender, times(2)).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
			@Override
			public boolean matches(final ILoggingEvent argument) {
				String formattedMessage = argument.getFormattedMessage();
				System.out.flush();
				System.out.println("** Got Message: " + formattedMessage);
				System.out.flush();
				return
					formattedMessage.contains("Client request: GET http://localhost:" + ourPort + "/Patient/1 HTTP/1.1") ||
					formattedMessage.contains("Client response: HTTP 200 OK (Patient/1/_history/1) in ");
			}
		}));
	}

		@Test
	public void testLoggerVerbose() {
		System.out.println("Starting testLogger");
		IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:" + ourPort);
		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
		
		LoggingInterceptor interceptor = new LoggingInterceptor(true);
		client.registerInterceptor(interceptor);
		Patient patient = client.read(Patient.class, "1");
		assertFalse(patient.getIdentifierFirstRep().isEmpty());

		verify(myMockAppender, times(1)).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
			@Override
			public boolean matches(final ILoggingEvent argument) {
				String formattedMessage = argument.getFormattedMessage();
				System.out.println("Verifying: " + formattedMessage);
				return formattedMessage.replace("; ", ";").toLowerCase().contains("Content-Type: application/fhir+xml;charset=utf-8".toLowerCase());
			}
		}));

		// Unregister the interceptor
		client.unregisterInterceptor(interceptor);
		
		patient = client.read(Patient.class, "1");
		assertFalse(patient.getIdentifierFirstRep().isEmpty());

		verify(myMockAppender, times(1)).doAppend(argThat(new ArgumentMatcher<ILoggingEvent>() {
			@Override
			public boolean matches(final ILoggingEvent argument) {
				String formattedMessage = argument.getFormattedMessage();
				System.out.println("Verifying: " + formattedMessage);
				return formattedMessage.replace("; ", ";").toLowerCase().contains("Content-Type: application/fhir+xml;charset=utf-8".toLowerCase());
			}
		}));

	}

	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
		TestUtil.randomizeLocaleAndTimezone();
	}

	@BeforeAll
	public static void beforeClass() throws Exception {

		URL conf = LoggingInterceptor.class.getResource("/logback-test-dstuforce.xml");
		assertNotNull(conf);
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(context);
		context.reset();
		configurator.doConfigure(conf);

		ourServer = new Server(0);

		DummyProvider patientProvider = new DummyProvider();

		ServletHandler proxyHandler = new ServletHandler();
		RestfulServer servlet = new RestfulServer(ourCtx);
		servlet.setDefaultResponseEncoding(EncodingEnum.XML);
		servlet.setResourceProviders(patientProvider);
		ServletHolder servletHolder = new ServletHolder(servlet);
		proxyHandler.addServletWithMapping(servletHolder, "/*");
		ourServer.setHandler(proxyHandler);
		JettyUtil.startServer(ourServer);
        ourPort = JettyUtil.getPortForStartedServer(ourServer);

		ourCtx.getRestfulClientFactory().setServerValidationMode(ServerValidationModeEnum.NEVER);
	}


	public static class DummyProvider implements IResourceProvider {

		@Read(version = true)
		public Patient findPatient(@IdParam IdType theId) {
			Patient patient = new Patient();
			patient.addIdentifier().setSystem(theId.getIdPart()).setValue(theId.getVersionIdPart());
			patient.setId("Patient/1/_history/1");
			return patient;
		}

		@Override
		public Class<Patient> getResourceType() {
			return Patient.class;
		}

	}

}
