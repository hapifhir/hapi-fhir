package ca.uhn.fhir.jpa.provider;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.testutil.RandomServerPortProvider;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.valueset.AdministrativeGenderCodesEnum;
import ca.uhn.fhir.model.dstu2.resource.PaymentNotice;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;

public class ResourceProviderMultiVersionTest  extends BaseJpaTest {

	private static ClassPathXmlApplicationContext ourAppCtx;
	private static IGenericClient ourClientDstu2;
	private static Server ourServer;
	private static IGenericClient ourClientDstu1;

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
		ourAppCtx.stop();
	}

	@Test
	public void testSubmitPatient() {
		Patient p = new Patient();
		p.addIdentifier("urn:MultiFhirVersionTest", "testSubmitPatient01");
		p.addUndeclaredExtension(false, "http://foo#ext1", new StringDt("The value"));
		p.getGender().setValueAsEnum(AdministrativeGenderCodesEnum.M);
		IdDt id = (IdDt) ourClientDstu1.create().resource(p).execute().getId();

		// Read back as DSTU1
		Patient patDstu1 = ourClientDstu1.read(Patient.class, id);
		assertEquals("testSubmitPatient01", p.getIdentifierFirstRep().getValue().getValue());
		assertEquals(1, patDstu1.getUndeclaredExtensionsByUrl("http://foo#ext1").size());
		assertEquals("M", patDstu1.getGender().getCodingFirstRep().getCode().getValue());

		// Read back as DEV
		ca.uhn.fhir.model.dstu2.resource.Patient patDstu2;
		patDstu2 = ourClientDstu2.read(ca.uhn.fhir.model.dstu2.resource.Patient.class, id);
		assertEquals("testSubmitPatient01", p.getIdentifierFirstRep().getValue().getValue());
		assertEquals(1, patDstu2.getUndeclaredExtensionsByUrl("http://foo#ext1").size());
		assertEquals(null, patDstu2.getGender());

		// Search using new bundle format
		Bundle bundle = ourClientDstu2.search().forResource(ca.uhn.fhir.model.dstu2.resource.Patient.class).where(Patient.IDENTIFIER.exactly().systemAndCode("urn:MultiFhirVersionTest", "testSubmitPatient01")).encodedJson().execute();
		patDstu2 = (ca.uhn.fhir.model.dstu2.resource.Patient) bundle.getEntries().get(0).getResource();
		assertEquals("testSubmitPatient01", p.getIdentifierFirstRep().getValue().getValue());
		assertEquals(1, patDstu2.getUndeclaredExtensionsByUrl("http://foo#ext1").size());
		assertEquals(null, patDstu2.getGender());

	}

	@Test
	public void testSubmitPatientDstu2() {
		ca.uhn.fhir.model.dstu2.resource.Patient p = new ca.uhn.fhir.model.dstu2.resource.Patient();
		p.addIdentifier().setSystem("urn:MultiFhirVersionTest").setValue("testSubmitPatientDstu201");
		p.addUndeclaredExtension(false, "http://foo#ext1", new StringDt("The value"));
		p.setGender(AdministrativeGenderEnum.MALE);
		IdDt id = (IdDt) ourClientDstu2.create().resource(p).execute().getId();

		// Read back as DSTU1
		Patient patDstu1 = ourClientDstu1.read(Patient.class, id);
		assertEquals("testSubmitPatientDstu201", p.getIdentifierFirstRep().getValue());
		assertEquals(1, patDstu1.getUndeclaredExtensionsByUrl("http://foo#ext1").size());
		assertEquals(null, patDstu1.getGender().getCodingFirstRep().getCode().getValue());

		// Read back as DEV
		ca.uhn.fhir.model.dstu2.resource.Patient patDstu2;
		patDstu2 = ourClientDstu2.read(ca.uhn.fhir.model.dstu2.resource.Patient.class, id);
		assertEquals("testSubmitPatientDstu201", p.getIdentifierFirstRep().getValue());
		assertEquals(1, patDstu2.getUndeclaredExtensionsByUrl("http://foo#ext1").size());
		assertEquals("male", patDstu2.getGender());

		// Search using new bundle format
		Bundle bundle = ourClientDstu2.search().forResource(ca.uhn.fhir.model.dstu2.resource.Patient.class).where(Patient.IDENTIFIER.exactly().systemAndCode("urn:MultiFhirVersionTest", "testSubmitPatientDstu201")).encodedJson().execute();
		patDstu2 = (ca.uhn.fhir.model.dstu2.resource.Patient) bundle.getEntries().get(0).getResource();
		assertEquals("testSubmitPatientDstu201", p.getIdentifierFirstRep().getValue());
		assertEquals(1, patDstu2.getUndeclaredExtensionsByUrl("http://foo#ext1").size());
		assertEquals("male", patDstu2.getGender());

	}

	@SuppressWarnings("deprecation")
	@Test
	public void testUnknownResourceType() {
		ca.uhn.fhir.model.dstu2.resource.Patient p = new ca.uhn.fhir.model.dstu2.resource.Patient();
		p.addIdentifier().setSystem("urn:MultiFhirVersionTest").setValue("testUnknownResourceType01");
		IdDt id = (IdDt) ourClientDstu2.create().resource(p).execute().getId();

		PaymentNotice s = new PaymentNotice();
		s.addIdentifier().setSystem("urn:MultiFhirVersionTest").setValue("testUnknownResourceType02");
		ourClientDstu2.create().resource(s).execute().getId();

		Bundle history = ourClientDstu2.history(null, id, null, null);
		assertEquals(PaymentNotice.class, history.getEntries().get(0).getResource().getClass());
		assertEquals(ca.uhn.fhir.model.dstu2.resource.Patient.class, history.getEntries().get(1).getResource().getClass());

		history = ourClientDstu1.history(null, id, null, null);
		assertEquals(ca.uhn.fhir.model.dstu.resource.Patient.class, history.getEntries().get(0).getResource().getClass());
		
		history = ourClientDstu2.history().onServer().andReturnDstu1Bundle().execute();
		assertEquals(PaymentNotice.class, history.getEntries().get(0).getResource().getClass());
		assertEquals(ca.uhn.fhir.model.dstu2.resource.Patient.class, history.getEntries().get(1).getResource().getClass());

		history = ourClientDstu1.history().onServer().andReturnDstu1Bundle().execute();
		assertEquals(ca.uhn.fhir.model.dstu.resource.Patient.class, history.getEntries().get(0).getResource().getClass());

		history = ourClientDstu1.history().onInstance(id).andReturnDstu1Bundle().execute();
		assertEquals(ca.uhn.fhir.model.dstu.resource.Patient.class, history.getEntries().get(0).getResource().getClass());

	}

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void beforeClass() throws Exception {
		//@formatter:off
		ourAppCtx = new ClassPathXmlApplicationContext(
				"hapi-fhir-server-resourceproviders-dstu1.xml", 
				"hapi-fhir-server-resourceproviders-dstu2.xml", 
				"fhir-jpabase-spring-test-config.xml"
				);
		//@formatter:on

		int port = RandomServerPortProvider.findFreePort();
		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		ourServer = new Server(port);

		/*
		 * DEV resources
		 */

		RestfulServer restServerDstu2 = new RestfulServer(ourAppCtx.getBean("myFhirContextDstu2", FhirContext.class));
		List<IResourceProvider> rpsDstu2 = (List<IResourceProvider>) ourAppCtx.getBean("myResourceProvidersDstu2", List.class);
		restServerDstu2.setResourceProviders(rpsDstu2);

		JpaSystemProviderDstu2 systemProvDstu2 = (JpaSystemProviderDstu2) ourAppCtx.getBean("mySystemProviderDstu2", JpaSystemProviderDstu2.class);
		restServerDstu2.setPlainProviders(systemProvDstu2);

		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(restServerDstu2);
		proxyHandler.addServlet(servletHolder, "/fhir/contextDstu2/*");

		/*
		 * DSTU resources
		 */

		RestfulServer restServerDstu1 = new RestfulServer(ourAppCtx.getBean("myFhirContextDstu1", FhirContext.class));
		List<IResourceProvider> rpsDstu1 = (List<IResourceProvider>) ourAppCtx.getBean("myResourceProvidersDstu1", List.class);
		restServerDstu1.setResourceProviders(rpsDstu1);

		JpaSystemProviderDstu1 systemProvDstu1 = (JpaSystemProviderDstu1) ourAppCtx.getBean("mySystemProviderDstu1", JpaSystemProviderDstu1.class);
		restServerDstu1.setPlainProviders(systemProvDstu1);

		servletHolder = new ServletHolder();
		servletHolder.setServlet(restServerDstu1);
		proxyHandler.addServlet(servletHolder, "/fhir/contextDstu1/*");

		/*
		 * Start server
		 */
		ourServer.setHandler(proxyHandler);
		ourServer.start();

		/*
		 * DEV Client
		 */
		String serverBaseDstu2 = "http://localhost:" + port + "/fhir/contextDstu2";
		FhirContext ctxDstu2 = ourAppCtx.getBean("myFhirContextDstu2", FhirContext.class);
		ctxDstu2.getRestfulClientFactory().setSocketTimeout(600 * 1000);
		ourClientDstu2 = ctxDstu2.newRestfulGenericClient(serverBaseDstu2);
		ourClientDstu2.registerInterceptor(new LoggingInterceptor(true));

		/*
		 * DSTU1 Client
		 */
		String serverBaseDstu1 = "http://localhost:" + port + "/fhir/contextDstu1";
		FhirContext ctxDstu1 = ourAppCtx.getBean("myFhirContextDstu1", FhirContext.class);
		ctxDstu1.getRestfulClientFactory().setSocketTimeout(600 * 1000);
		ourClientDstu1 = ctxDstu1.newRestfulGenericClient(serverBaseDstu1);
		ourClientDstu1.registerInterceptor(new LoggingInterceptor(true));
	}

}
