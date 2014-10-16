package ca.uhn.fhir.jpa.test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.Date;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.provider.JpaSystemProvider;
import ca.uhn.fhir.jpa.testutil.RandomServerPortProvider;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.Location;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.dstu.resource.Questionnaire;
import ca.uhn.fhir.model.dstu.valueset.EncounterClassEnum;
import ca.uhn.fhir.model.dstu.valueset.EncounterStateEnum;
import ca.uhn.fhir.model.dstu.valueset.NarrativeStatusEnum;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.narrative.DefaultThymeleafNarrativeGenerator;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import ca.uhn.fhir.rest.gclient.StringClientParam;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.test.jpasrv.EncounterResourceProvider;
import ca.uhn.test.jpasrv.LocationResourceProvider;
import ca.uhn.test.jpasrv.ObservationResourceProvider;
import ca.uhn.test.jpasrv.OrganizationResourceProvider;
import ca.uhn.test.jpasrv.PatientResourceProvider;

public class CompleteResourceProviderTest {

	private static IFhirResourceDao<Observation> observationDao;
	private static ClassPathXmlApplicationContext ourAppCtx;

	private static IGenericClient ourClient;
	private static FhirContext ourCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(CompleteResourceProviderTest.class);
	private static Server ourServer;
	private static IFhirResourceDao<Patient> patientDao;

	// private static JpaConformanceProvider ourConfProvider;

	// @Test
	// public void test01UploadTestResources() throws Exception {
	//
	// IGenericClient client = ourCtx.newRestfulGenericClient("http://localhost:8888/fhir/context");
	//
	// File[] files = new File("src/test/resources/resources").listFiles(new PatternFilenameFilter(".*patient.*"));
	// for (File file : files) {
	// ourLog.info("Uploading: {}", file);
	// Patient patient = ourCtx.newXmlParser().parseResource(Patient.class, new FileReader(file));
	// client.create(patient);
	// }
	//
	// files = new File("src/test/resources/resources").listFiles(new PatternFilenameFilter(".*questionnaire.*"));
	// for (File file : files) {
	// ourLog.info("Uploading: {}", file);
	// Questionnaire patient = ourCtx.newXmlParser().parseResource(Questionnaire.class, new FileReader(file));
	// client.create(patient);
	// }
	//
	// }

	private static IFhirResourceDao<Questionnaire> questionnaireDao;

	@Test
	public void testUpdateWithClientSuppliedIdWhichDoesntExist() {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testUpdateWithClientSuppliedIdWhichDoesntExist");

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateWithClientSuppliedIdWhichDoesntExist");
		MethodOutcome outcome = ourClient.update().resource(p1).withId("testUpdateWithClientSuppliedIdWhichDoesntExist").execute();
		assertEquals(true, outcome.getCreated().booleanValue());
		IdDt p1Id = outcome.getId();

		assertThat(p1Id.getValue(), containsString("Patient/testUpdateWithClientSuppliedIdWhichDoesntExist/_history"));

		Bundle actual = ourClient.search().forResource(Patient.class).where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", "testUpdateWithClientSuppliedIdWhichDoesntExist")).encodedJson().prettyPrint().execute();
		assertEquals(1, actual.size());
		assertEquals(p1Id.getIdPart(), actual.getEntries().get(0).getId().getIdPart());

	}
	
	@Test
	public void testCreateWithClientSuppliedId() {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testCreateWithId01");

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testCreateWithId01");
		IdDt p1Id = ourClient.create().resource(p1).withId("testCreateWithId").execute().getId();

		assertThat(p1Id.getValue(), containsString("Patient/testCreateWithId/_history"));

		Bundle actual = ourClient.search().forResource(Patient.class).where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", "testCreateWithId01")).encodedJson().prettyPrint().execute();
		assertEquals(1, actual.size());
		assertEquals(p1Id.getIdPart(), actual.getEntries().get(0).getId().getIdPart());

		/*
		 * ensure that trying to create the same ID again fails appropriately
		 */
		try {
			ourClient.create().resource(p1).withId("testCreateWithId").execute().getId();
			fail();
		} catch (UnprocessableEntityException e) {
			// good
		}

		Bundle history = ourClient.history(null, (String) null, null, null);
		assertEquals(p1Id.getIdPart(), history.getEntries().get(0).getId().getIdPart());
		assertNotNull(history.getEntries().get(0).getResource());
	}

	@Test
	public void testTryToCreateResourceWithReferenceThatDoesntExist() {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testTryToCreateResourceWithReferenceThatDoesntExist01");

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testTryToCreateResourceWithReferenceThatDoesntExist01");
		p1.addName().addFamily("testTryToCreateResourceWithReferenceThatDoesntExistFamily01").addGiven("testTryToCreateResourceWithReferenceThatDoesntExistGiven01");
		p1.setManagingOrganization(new ResourceReferenceDt("Organization/1323123232349875324987529835"));

		try {
			ourClient.create(p1).getId();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Organization/1323123232349875324987529835"));
		}

	}

	@Test
	public void testSaveAndRetrieveExistingNarrative() {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testSaveAndRetrieveExistingNarrative01");

		Patient p1 = new Patient();
		p1.getText().setStatus(NarrativeStatusEnum.GENERATED);
		p1.getText().getDiv().setValueAsString("<div>HELLO WORLD</div>");
		p1.addIdentifier().setSystem("urn:system").setValue("testSaveAndRetrieveExistingNarrative01");

		IdDt newId = ourClient.create(p1).getId();

		Patient actual = ourClient.read(Patient.class, newId);
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">HELLO WORLD</div>", actual.getText().getDiv().getValueAsString());
	}

	@Test
	public void testSaveAndRetrieveWithoutNarrative() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByResourceChain01");

		IdDt newId = ourClient.create(p1).getId();

		Patient actual = ourClient.read(Patient.class, newId);
		assertThat(actual.getText().getDiv().getValueAsString(), containsString("<td>Identifier</td><td>testSearchByResourceChain01</td>"));
	}

	@Test
	public void testSaveAndRetrieveWithContained() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSaveAndRetrieveWithContained01");

		Organization o1 = new Organization();
		o1.addIdentifier().setSystem("urn:system").setValue("testSaveAndRetrieveWithContained02");
		
		p1.getManagingOrganization().setResource(o1);
		
		IdDt newId = ourClient.create().resource(p1).execute().getId();

		Patient actual = ourClient.read(Patient.class, newId);
		assertEquals(1, actual.getContained().getContainedResources().size());
		assertThat(actual.getText().getDiv().getValueAsString(), containsString("<td>Identifier</td><td>testSaveAndRetrieveWithContained01</td>"));
		
		Bundle b = ourClient.search().forResource("Patient").where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system","testSaveAndRetrieveWithContained01")).execute();
		assertEquals(1, b.size());
		
	}
	
	
	@Test
	public void testSearchByIdentifier() {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testSearchByIdentifier01");
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testSearchByIdentifier02");

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByIdentifier01");
		p1.addName().addFamily("testSearchByIdentifierFamily01").addGiven("testSearchByIdentifierGiven01");
		IdDt p1Id = ourClient.create(p1).getId();

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testSearchByIdentifier02");
		p2.addName().addFamily("testSearchByIdentifierFamily01").addGiven("testSearchByIdentifierGiven02");
		ourClient.create(p2).getId();

		Bundle actual = ourClient.search().forResource(Patient.class).where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", "testSearchByIdentifier01")).encodedJson().prettyPrint().execute();
		assertEquals(1, actual.size());
		assertEquals(p1Id.getIdPart(), actual.getEntries().get(0).getId().getIdPart());
	}

	@Test
	public void testSearchByIdentifierWithoutSystem() {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "", "testSearchByIdentifierWithoutSystem01");

		Patient p1 = new Patient();
		p1.addIdentifier().setValue("testSearchByIdentifierWithoutSystem01");
		IdDt p1Id = ourClient.create(p1).getId();

		Bundle actual = ourClient.search().forResource(Patient.class).where(Patient.IDENTIFIER.exactly().systemAndCode(null, "testSearchByIdentifierWithoutSystem01")).encodedJson().prettyPrint().execute();
		assertEquals(1, actual.size());
		assertEquals(p1Id.getIdPart(), actual.getEntries().get(0).getId().getIdPart());

	}

	@Test
	public void testUpdateRejectsInvalidTypes() throws InterruptedException {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testUpdateRejectsInvalidTypes");
		
		Patient p1 = new Patient();
		p1.addIdentifier("urn:system", "testUpdateRejectsInvalidTypes");
		p1.addName().addFamily("Tester").addGiven("testUpdateRejectsInvalidTypes");
		IdDt p1id = ourClient.create().resource(p1).execute().getId();

		Organization p2 = new Organization();
		p2.getName().setValue("testUpdateRejectsInvalidTypes");
		try {
			ourClient.update().resource(p2).withId("Organization/" + p1id.getIdPart()).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			// good
		}

		try {
			ourClient.update().resource(p2).withId("Patient/" + p1id.getIdPart()).execute();
			fail();
		} catch (UnprocessableEntityException e) {
			// good
		}

	}
	
	
	@Test
	public void testDeepChaining() {
		delete("Location", Location.SP_NAME, "testDeepChainingL1");
		delete("Location", Location.SP_NAME, "testDeepChainingL2");
		deleteToken("Encounter", Encounter.SP_IDENTIFIER, "urn:foo", "testDeepChainingE1");

		Location l1 = new Location();
		l1.getName().setValue("testDeepChainingL1");
		IdDt l1id = ourClient.create().resource(l1).execute().getId();

		Location l2 = new Location();
		l2.getName().setValue("testDeepChainingL2");
		l2.getPartOf().setReference(l1id.toVersionless().toUnqualified());
		IdDt l2id = ourClient.create().resource(l2).execute().getId();

		Encounter e1 = new Encounter();
		e1.addIdentifier().setSystem("urn:foo").setValue("testDeepChainingE1");
		e1.getStatus().setValueAsEnum(EncounterStateEnum.IN_PROGRESS);
		e1.getClassElement().setValueAsEnum(EncounterClassEnum.HOME);
		ca.uhn.fhir.model.dstu.resource.Encounter.Location location = e1.addLocation();
		location.getLocation().setReference(l2id.toUnqualifiedVersionless());
		location.setPeriod(new PeriodDt().setStartWithSecondsPrecision(new Date()).setEndWithSecondsPrecision(new Date()));
		IdDt e1id = ourClient.create().resource(e1).execute().getId();

		//@formatter:off
		Bundle res = ourClient.search()
			.forResource(Encounter.class)
			.where(Encounter.IDENTIFIER.exactly().systemAndCode("urn:foo", "testDeepChainingE1"))
			.include(Encounter.INCLUDE_LOCATION_LOCATION)
			.include(Location.INCLUDE_PARTOF)
			.execute();
		//@formatter:on

		assertEquals(3, res.size());
		assertEquals(1, res.getResources(Encounter.class).size());
		assertEquals(e1id.toUnqualifiedVersionless(), res.getResources(Encounter.class).get(0).getId().toUnqualifiedVersionless());

	}

	private void delete(String theResourceType, String theParamName, String theParamValue) {
		Bundle resources = ourClient.search().forResource(theResourceType).where(new StringClientParam(theParamName).matches().value(theParamValue)).execute();
		for (IResource next : resources.toListOfResources()) {
			ourLog.info("Deleting resource: {}", next.getId());
			ourClient.delete().resource(next).execute();
		}
	}

	private void deleteToken(String theResourceType, String theParamName, String theParamSystem, String theParamValue) {
		Bundle resources = ourClient.search().forResource(theResourceType).where(new TokenClientParam(theParamName).exactly().systemAndCode(theParamSystem, theParamValue)).execute();
		for (IResource next : resources.toListOfResources()) {
			ourLog.info("Deleting resource: {}", next.getId());
			ourClient.delete().resource(next).execute();
		}
	}

	@Test
	public void testSearchByResourceChain() {
		delete("Organization", Organization.SP_NAME, "testSearchByResourceChainName01");
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testSearchByResourceChain01");

		Organization o1 = new Organization();
		o1.setName("testSearchByResourceChainName01");
		IdDt o1id = ourClient.create(o1).getId();

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByResourceChain01");
		p1.addName().addFamily("testSearchByResourceChainFamily01").addGiven("testSearchByResourceChainGiven01");
		p1.setManagingOrganization(new ResourceReferenceDt(o1id));
		IdDt p1Id = ourClient.create(p1).getId();

		//@formatter:off
		Bundle actual = ourClient.search()
				.forResource(Patient.class)
				.where(Patient.PROVIDER.hasId(o1id.getIdPart()))
				.encodedJson().andLogRequestAndResponse(true).prettyPrint().execute();
		//@formatter:on
		assertEquals(1, actual.size());
		assertEquals(p1Id.getIdPart(), actual.getEntries().get(0).getId().getIdPart());

		//@formatter:off
		actual = ourClient.search()
				.forResource(Patient.class)
				.where(Patient.PROVIDER.hasId(o1id.getValue()))
				.encodedJson().andLogRequestAndResponse(true).prettyPrint().execute();
		//@formatter:on
		assertEquals(1, actual.size());
		assertEquals(p1Id.getIdPart(), actual.getEntries().get(0).getId().getIdPart());

	}

	@AfterClass
	public static void afterClass() throws Exception {
		ourServer.stop();
		ourAppCtx.stop();
	}

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void beforeClass() throws Exception {
		int port = RandomServerPortProvider.findFreePort();
		RestfulServer restServer = new RestfulServer();
		String serverBase = "http://localhost:" + port + "/fhir/context";

		if (true) {
			ourAppCtx = new ClassPathXmlApplicationContext("fhir-spring-test-config.xml");

			patientDao = (IFhirResourceDao<Patient>) ourAppCtx.getBean("myPatientDao", IFhirResourceDao.class);
			PatientResourceProvider patientRp = new PatientResourceProvider();
			patientRp.setDao(patientDao);

			questionnaireDao = (IFhirResourceDao<Questionnaire>) ourAppCtx.getBean("myQuestionnaireDao", IFhirResourceDao.class);
			QuestionnaireResourceProvider questionnaireRp = new QuestionnaireResourceProvider();
			questionnaireRp.setDao(questionnaireDao);

			observationDao = (IFhirResourceDao<Observation>) ourAppCtx.getBean("myObservationDao", IFhirResourceDao.class);
			ObservationResourceProvider observationRp = new ObservationResourceProvider();
			observationRp.setDao(observationDao);

			IFhirResourceDao<Location> locationDao = (IFhirResourceDao<Location>) ourAppCtx.getBean("myLocationDao", IFhirResourceDao.class);
			LocationResourceProvider locationRp = new LocationResourceProvider();
			locationRp.setDao(locationDao);

			IFhirResourceDao<Encounter> encounterDao = (IFhirResourceDao<Encounter>) ourAppCtx.getBean("myEncounterDao", IFhirResourceDao.class);
			EncounterResourceProvider encounterRp = new EncounterResourceProvider();
			encounterRp.setDao(encounterDao);

			IFhirResourceDao<Organization> organizationDao = (IFhirResourceDao<Organization>) ourAppCtx.getBean("myOrganizationDao", IFhirResourceDao.class);
			OrganizationResourceProvider organizationRp = new OrganizationResourceProvider();
			organizationRp.setDao(organizationDao);

			restServer.setResourceProviders(encounterRp, locationRp, patientRp, questionnaireRp, observationRp, organizationRp);
			restServer.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

			IFhirSystemDao systemDao = (IFhirSystemDao) ourAppCtx.getBean("mySystemDao", IFhirSystemDao.class);
			JpaSystemProvider systemProv = new JpaSystemProvider(systemDao);
			restServer.setPlainProviders(systemProv);

			// ourConfProvider = new JpaConformanceProvider(restServer, systemDao,
			// Collections.singletonList((IFhirResourceDao)patientDao));

			ourServer = new Server(port);

			ServletContextHandler proxyHandler = new ServletContextHandler();
			proxyHandler.setContextPath("/");

			// testerServlet.setServerBase("http://fhir.healthintersections.com.au/open");

			ServletHolder servletHolder = new ServletHolder();
			servletHolder.setServlet(restServer);
			proxyHandler.addServlet(servletHolder, "/fhir/context/*");

			ourServer.setHandler(proxyHandler);
			ourServer.start();
		}

		ourCtx = restServer.getFhirContext();
//		ourCtx.getRestfulClientFactory().setProxy("localhost", 8888);

		ourClient = ourCtx.newRestfulGenericClient(serverBase);
		// ourClient = ourCtx.newRestfulGenericClient("http://fhir.healthintersections.com.au/open");
		// ourClient = ourCtx.newRestfulGenericClient("https://fhir.orionhealth.com/blaze/fhir");
		// ourClient = ourCtx.newRestfulGenericClient("http://spark.furore.com/fhir");
		ourClient.registerInterceptor(new LoggingInterceptor(true));

	}

}
