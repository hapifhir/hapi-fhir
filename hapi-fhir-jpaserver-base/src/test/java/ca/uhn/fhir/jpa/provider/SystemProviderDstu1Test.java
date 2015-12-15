package ca.uhn.fhir.jpa.provider;

import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.hamcrest.text.IsBlankString;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.dao.IDaoFactory;
import ca.uhn.fhir.dao.IFhirResourceDao;
import ca.uhn.fhir.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.config.TestDstu1Config;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.dao.FhirSystemDaoDstu1;
import ca.uhn.fhir.jpa.dao.JpaDaoFactory;
import ca.uhn.fhir.provider.config.BaseJavaConfigDstu1;
import ca.uhn.fhir.provider.dstu.ObservationResourceProvider;
import ca.uhn.fhir.provider.dstu.OrganizationResourceProvider;
import ca.uhn.fhir.provider.dstu.PatientResourceProvider;
import ca.uhn.fhir.jpa.testutil.RandomServerPortProvider;
import ca.uhn.fhir.model.api.BundleEntry;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Questionnaire;
import ca.uhn.fhir.model.dstu2.composite.MetaDt;
import ca.uhn.fhir.model.dstu2.resource.Bundle;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.provider.impl.SystemProviderDstu1;

public class SystemProviderDstu1Test  extends BaseJpaTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(SystemProviderDstu1Test.class);
	private static Server ourServer;
	private static AnnotationConfigApplicationContext ourAppCtx;
	private static FhirContext ourCtx;
	private static IGenericClient ourClient;

	@Test
	public void testTransactionFromBundle() throws Exception {

		InputStream bundleRes = SystemProviderDstu1Test.class.getResourceAsStream("/testbundle.xml");
		String bundle = IOUtils.toString(bundleRes);
		String response = ourClient.transaction().withBundle(bundle).prettyPrint().execute();
		ourLog.info(response);
		
		ca.uhn.fhir.model.api.Bundle respBundle = ourCtx.newXmlParser().parseBundle(response);
		assertEquals(3, respBundle.size());
		for (int i = 1; i < 3; i++) {
			BundleEntry next = respBundle.getEntries().get(i);
			String nextValue = next.getResource().getId().getValue();
			assertThat(nextValue, not(blankOrNullString()));
			assertThat(nextValue, not(containsString("cid")));
		}
		
	}
	
	
	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
		ourAppCtx.stop();
	}

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void beforeClass() throws Exception {
		ourAppCtx = new AnnotationConfigApplicationContext(TestDstu1Config.class, BaseJavaConfigDstu1.class);

		IDaoFactory daoFactory = ourAppCtx.getBean("myDaoFactoryDstu1", IDaoFactory.class);
		
		IFhirResourceDao<Patient> patientDao = (IFhirResourceDao<Patient>) ourAppCtx.getBean("myPatientDaoDstu1", IFhirResourceDao.class);
		PatientResourceProvider patientRp = new PatientResourceProvider();
		patientRp.setResourceDaoFactory(daoFactory);

		IFhirResourceDao<Questionnaire> questionnaireDao = (IFhirResourceDao<Questionnaire>) ourAppCtx.getBean("myQuestionnaireDaoDstu1", IFhirResourceDao.class);
		QuestionnaireResourceProvider questionnaireRp = new QuestionnaireResourceProvider();
		questionnaireRp.setResourceDaoFactory(daoFactory);

		IFhirResourceDao<Observation> observationDao = (IFhirResourceDao<Observation>) ourAppCtx.getBean("myObservationDaoDstu1", IFhirResourceDao.class);
		ObservationResourceProvider observationRp = new ObservationResourceProvider();
		observationRp.setResourceDaoFactory(daoFactory);

		IFhirResourceDao<Organization> organizationDao = (IFhirResourceDao<Organization>) ourAppCtx.getBean("myOrganizationDaoDstu1", IFhirResourceDao.class);
		OrganizationResourceProvider organizationRp = new OrganizationResourceProvider();
		organizationRp.setResourceDaoFactory(daoFactory);

		RestfulServer restServer = new RestfulServer(ourCtx);
		restServer.setResourceProviders(patientRp, questionnaireRp, observationRp, organizationRp);

//		IFhirSystemDao<List<IResource>, MetaDt> systemDao = ourAppCtx.getBean(FhirSystemDaoDstu1.class);
		
		SystemProviderDstu1 systemProv = ourAppCtx.getBean(SystemProviderDstu1.class, "mySystemProviderDstu1");
		systemProv.setSystemDaoFactory(daoFactory);
		restServer.setPlainProviders(systemProv);

		int myPort = RandomServerPortProvider.findFreePort();
		ourServer = new Server(myPort);

		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		String serverBase = "http://localhost:" + myPort + "/fhir/context";

		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(restServer);
		proxyHandler.addServlet(servletHolder, "/fhir/context/*");

		ourCtx = FhirContext.forDstu1();
		restServer.setFhirContext(ourCtx);
		
		ourServer.setHandler(proxyHandler);
		ourServer.start();


		ourCtx.getRestfulClientFactory().setSocketTimeout(600 * 1000);
		ourClient = ourCtx.newRestfulGenericClient(serverBase);
		ourClient.setLogRequestAndResponse(true);
	}
	
}
