package ca.uhn.fhir.jpa.entity;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.rp.r4.PatientResourceProvider;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.client.interceptor.SimpleRequestHeaderInterceptor;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.test.utilities.JettyUtil;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Server;
import org.h2.util.StringUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MetaSourceTest extends BaseJpaR4Test {

	private static RestfulServer ourRestServer;
	private static final FhirContext ourCtx = FhirContext.forR4Cached();
	private static Server ourServer;
	private static String ourServerBase;
	private IGenericClient myClient;

	@BeforeEach
	void beforeStartServer() throws Exception {
		if (ourRestServer == null) {

			PatientResourceProvider patientRp = new PatientResourceProvider();
			patientRp.setContext(ourCtx);
			patientRp.setDao(myPatientDao);

			RestfulServer restServer = new RestfulServer(ourCtx);
			restServer.setResourceProviders(patientRp);

			restServer.registerProviders(mySystemProvider);

			ourServer = new Server(0);

			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");

			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(restServer);
			proxyHandler.addServlet(servletHolder, "/fhir/context/*");

			restServer.setFhirContext(ourCtx);

			ourServer.setHandler(proxyHandler);
			JettyUtil.startServer(ourServer);
			int myPort = JettyUtil.getPortForStartedServer(ourServer);
			ourServerBase = "http://localhost:" + myPort + "/fhir/context";

			ourCtx.getRestfulClientFactory().setSocketTimeout(600 * 1_000);
			ourRestServer = restServer;
		}

		myClient = ourCtx.newRestfulGenericClient(ourServerBase);
		SimpleRequestHeaderInterceptor simpleHeaderInterceptor = new SimpleRequestHeaderInterceptor();
		myClient.registerInterceptor(simpleHeaderInterceptor);

		ourRestServer.setDefaultResponseEncoding(EncodingEnum.XML);
		ourRestServer.setPagingProvider(myPagingProvider);

		LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
		loggingInterceptor.setLogRequestBody(true);
		loggingInterceptor.setLogResponseBody(true);
	}


	@Test
	void testMetaSourceSupportsMaxLength() {
		int metaSourceLength = 700;

		Patient p1 = new Patient();
		p1.setActive(true);

		Meta meta = new Meta();
		String longSourceValue = StringUtils.pad("http://", metaSourceLength, "abc", true);
		meta.setSource(longSourceValue);
		p1.setMeta(meta);
		IIdType patientId = myClient.create().resource(p1).execute().getId().toUnqualifiedVersionless();

		// verify
		Patient patient = myClient.read().resource(Patient.class).withId(patientId.getValueAsString()).execute();
		assertThat(patient.getMeta().getSource()).hasSizeGreaterThan(metaSourceLength);
	}



	@AfterAll
	public static void afterClassClearContext() throws Exception {
		JettyUtil.closeServer(ourServer);
	}

}
