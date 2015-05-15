package ca.uhn.fhir.jpa.provider;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.testutil.RandomServerPortProvider;
import ca.uhn.fhir.model.api.Bundle;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.dstu.composite.PeriodDt;
import ca.uhn.fhir.model.dstu.composite.ResourceReferenceDt;
import ca.uhn.fhir.model.dstu.resource.DiagnosticOrder;
import ca.uhn.fhir.model.dstu.resource.DocumentManifest;
import ca.uhn.fhir.model.dstu.resource.DocumentReference;
import ca.uhn.fhir.model.dstu.resource.Encounter;
import ca.uhn.fhir.model.dstu.resource.ImagingStudy;
import ca.uhn.fhir.model.dstu.resource.Location;
import ca.uhn.fhir.model.dstu.resource.Organization;
import ca.uhn.fhir.model.dstu.resource.Patient;
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
import ca.uhn.fhir.rest.server.FifoMemoryPagingProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;

public class ResourceProviderDstu1Test {

	private static ClassPathXmlApplicationContext ourAppCtx;
	private static IGenericClient ourClient;
	private static DaoConfig ourDaoConfig;
	private static FhirContext ourFhirCtx;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ResourceProviderDstu1Test.class);
	private static IFhirResourceDao<Organization> ourOrganizationDao;
	// private static IFhirResourceDao<Observation> ourObservationDao;
	// private static IFhirResourceDao<Patient> ourPatientDao;
	// private static IFhirResourceDao<Questionnaire> ourQuestionnaireDao;
	private static Server ourServer;

	// private static JpaConformanceProvider ourConfProvider;

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
	public void testCountParam() throws Exception {
		// NB this does not get used- The paging provider has its own limits built in
		ourDaoConfig.setHardSearchLimit(100);

		List<IResource> resources = new ArrayList<IResource>();
		for (int i = 0; i < 100; i++) {
			Organization org = new Organization();
			org.setName("testCountParam_01");
			resources.add(org);
		}
		ourClient.transaction().withResources(resources).prettyPrint().encodedXml().execute();

		Bundle found = ourClient.search().forResource(Organization.class).where(Organization.NAME.matches().value("testCountParam_01")).limitTo(10).execute();
		assertEquals(100, found.getTotalResults().getValue().intValue());
		assertEquals(10, found.getEntries().size());

		found = ourClient.search().forResource(Organization.class).where(Organization.NAME.matches().value("testCountParam_01")).limitTo(999).execute();
		assertEquals(100, found.getTotalResults().getValue().intValue());
		assertEquals(50, found.getEntries().size());

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

		assertEquals("Expected[" + p1Id.getIdPart() + "] but was " + history.getEntries().get(0).getId(), p1Id.getIdPart(), history.getEntries().get(0).getId().getIdPart());
		assertNotNull(history.getEntries().get(0).getResource());
	}

	@Test
	public void testDeepChaining() {
		delete("Location", Location.SP_NAME, "testDeepChainingL1");
		delete("Location", Location.SP_NAME, "testDeepChainingL2");
		deleteToken("Encounter", Encounter.SP_IDENTIFIER, "urn:foo", "testDeepChainingE1");

		Location l1 = new Location();
		l1.getNameElement().setValue("testDeepChainingL1");
		IdDt l1id = ourClient.create().resource(l1).execute().getId();

		Location l2 = new Location();
		l2.getNameElement().setValue("testDeepChainingL2");
		l2.getPartOf().setReference(l1id.toVersionless().toUnqualified());
		IdDt l2id = ourClient.create().resource(l2).execute().getId();

		Encounter e1 = new Encounter();
		e1.addIdentifier().setSystem("urn:foo").setValue("testDeepChainingE1");
		e1.getStatusElement().setValueAsEnum(EncounterStateEnum.IN_PROGRESS);
		e1.getClassElementElement().setValueAsEnum(EncounterClassEnum.HOME);
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

	/**
	 * See issue #52
	 */
	@Test
	public void testDiagnosticOrderResources() throws Exception {
		IGenericClient client = ourClient;

		int initialSize = client.search().forResource(DiagnosticOrder.class).execute().size();

		DiagnosticOrder res = new DiagnosticOrder();
		res.addIdentifier().setSystem("urn:foo").setValue( "123");

		client.create().resource(res).execute();

		int newSize = client.search().forResource(DiagnosticOrder.class).execute().size();

		assertEquals(1, newSize - initialSize);

	}

	/**
	 * See issue #52
	 */
	@Test
	public void testDocumentManifestResources() throws Exception {
		IGenericClient client = ourClient;

		int initialSize = client.search().forResource(DocumentManifest.class).execute().size();

		String resBody = IOUtils.toString(ResourceProviderDstu1Test.class.getResource("/documentmanifest.json"));
		client.create().resource(resBody).execute();

		int newSize = client.search().forResource(DocumentManifest.class).execute().size();

		assertEquals(1, newSize - initialSize);

	}

	/**
	 * See issue #52
	 */
	@Test
	public void testDocumentReferenceResources() throws Exception {
		IGenericClient client = ourClient;

		int initialSize = client.search().forResource(DocumentReference.class).execute().size();

		String resBody = IOUtils.toString(ResourceProviderDstu1Test.class.getResource("/documentreference.json"));
		client.create().resource(resBody).execute();

		int newSize = client.search().forResource(DocumentReference.class).execute().size();

		assertEquals(1, newSize - initialSize);

	}

	/**
	 * See issue #52
	 */
	@Test
	public void testImagingStudyResources() throws Exception {
		IGenericClient client = ourClient;

		int initialSize = client.search().forResource(ImagingStudy.class).execute().size();

		String resBody = IOUtils.toString(ResourceProviderDstu1Test.class.getResource("/imagingstudy.json"));
		client.create().resource(resBody).execute();

		int newSize = client.search().forResource(ImagingStudy.class).execute().size();

		assertEquals(1, newSize - initialSize);

	}

	@Test
	public void testSaveAndRetrieveExistingNarrative() {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testSaveAndRetrieveExistingNarrative01");

		Patient p1 = new Patient();
		p1.getText().setStatus(NarrativeStatusEnum.GENERATED);
		p1.getText().getDiv().setValueAsString("<div>HELLO WORLD</div>");
		p1.addIdentifier().setSystem("urn:system").setValue("testSaveAndRetrieveExistingNarrative01");

		IdDt newId = ourClient.create().resource(p1).execute().getId();

		Patient actual = ourClient.read(Patient.class, newId);
		assertEquals("<div xmlns=\"http://www.w3.org/1999/xhtml\">HELLO WORLD</div>", actual.getText().getDiv().getValueAsString());
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

		Bundle b = ourClient.search().forResource("Patient").where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", "testSaveAndRetrieveWithContained01")).execute();
		assertEquals(1, b.size());

	}

	@Test
	public void testSaveAndRetrieveWithoutNarrative() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByResourceChain01");

		IdDt newId = ourClient.create().resource(p1).execute().getId();

		Patient actual = ourClient.read(Patient.class, newId);
		assertThat(actual.getText().getDiv().getValueAsString(), containsString("<td>Identifier</td><td>testSearchByResourceChain01</td>"));
	}

	@Test
	public void testSearchByIdentifier() {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testSearchByIdentifier01");
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testSearchByIdentifier02");

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByIdentifier01");
		p1.addName().addFamily("testSearchByIdentifierFamily01").addGiven("testSearchByIdentifierGiven01");
		IdDt p1Id = ourClient.create().resource(p1).execute().getId();

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testSearchByIdentifier02");
		p2.addName().addFamily("testSearchByIdentifierFamily01").addGiven("testSearchByIdentifierGiven02");
		ourClient.create().resource(p2).execute().getId();

		Bundle actual = ourClient.search().forResource(Patient.class).where(Patient.IDENTIFIER.exactly().systemAndCode("urn:system", "testSearchByIdentifier01")).encodedJson().prettyPrint().execute();
		assertEquals(1, actual.size());
		assertEquals(p1Id.getIdPart(), actual.getEntries().get(0).getId().getIdPart());
	}

	@Test
	public void testSearchByIdentifierWithoutSystem() {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "", "testSearchByIdentifierWithoutSystem01");

		Patient p1 = new Patient();
		p1.addIdentifier().setValue("testSearchByIdentifierWithoutSystem01");
		IdDt p1Id = ourClient.create().resource(p1).execute().getId();

		Bundle actual = ourClient.search().forResource(Patient.class).where(Patient.IDENTIFIER.exactly().systemAndCode(null, "testSearchByIdentifierWithoutSystem01")).encodedJson().prettyPrint().execute();
		assertEquals(1, actual.size());
		assertEquals(p1Id.getIdPart(), actual.getEntries().get(0).getId().getIdPart());

	}

	@Test
	public void testSearchByResourceChain() {
		delete("Organization", Organization.SP_NAME, "testSearchByResourceChainName01");
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testSearchByResourceChain01");

		Organization o1 = new Organization();
		o1.setName("testSearchByResourceChainName01");
		IdDt o1id = ourClient.create().resource(o1).execute().getId();

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testSearchByResourceChain01");
		p1.addName().addFamily("testSearchByResourceChainFamily01").addGiven("testSearchByResourceChainGiven01");
		p1.setManagingOrganization(new ResourceReferenceDt(o1id));
		IdDt p1Id = ourClient.create().resource(p1).execute().getId();

		//@formatter:off
		Bundle actual = ourClient.search()
				.forResource(Patient.class)
				.where(Patient.PROVIDER.hasId(o1id.getIdPart()))
				.encodedJson().prettyPrint().execute();
		//@formatter:on
		assertEquals(1, actual.size());
		assertEquals(p1Id.getIdPart(), actual.getEntries().get(0).getId().getIdPart());

		//@formatter:off
		actual = ourClient.search()
				.forResource(Patient.class)
				.where(Patient.PROVIDER.hasId(o1id.getValue()))
				.encodedJson().prettyPrint().execute();
		//@formatter:on
		assertEquals(1, actual.size());
		assertEquals(p1Id.getIdPart(), actual.getEntries().get(0).getId().getIdPart());

	}

	@Test
	public void testSearchWithInclude() throws Exception {
		Organization org = new Organization();
		org.addIdentifier().setSystem("urn:system").setValue( "testSearchWithInclude01");
		IdDt orgId = ourClient.create().resource(org).prettyPrint().encodedXml().execute().getId();

		Patient pat = new Patient();
		pat.addIdentifier().setSystem("urn:system").setValue("testSearchWithInclude02");
		pat.getManagingOrganization().setReference(orgId);
		ourClient.create().resource(pat).prettyPrint().encodedXml().execute().getId();

		//@formatter:off
		Bundle found = ourClient
				.search()
				.forResource(Patient.class)
				.where(Patient.IDENTIFIER.exactly().systemAndIdentifier("urn:system","testSearchWithInclude02"))
				.include(Patient.INCLUDE_MANAGINGORGANIZATION)
				.execute();
		//@formatter:on
		
		assertEquals(2, found.size());
		assertEquals(Patient.class, found.getEntries().get(0).getResource().getClass());
		assertEquals(null, found.getEntries().get(0).getSearchMode().getValueAsEnum());
		assertEquals(null, found.getEntries().get(0).getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE));
		assertEquals(Organization.class, found.getEntries().get(1).getResource().getClass());
		assertEquals(null, found.getEntries().get(1).getSearchMode().getValueAsEnum());
		assertEquals(null, found.getEntries().get(1).getResource().getResourceMetadata().get(ResourceMetadataKeyEnum.ENTRY_SEARCH_MODE));
	}

	/**
	 * Test for issue #60
	 */
	@Test
	public void testStoreUtf8Characters() throws Exception {
		Organization org = new Organization();
		org.setName("測試醫院");
		org.addIdentifier().setSystem("urn:system").setValue("testStoreUtf8Characters_01");
		IdDt orgId = ourClient.create().resource(org).prettyPrint().encodedXml().execute().getId();

		// Read back directly from the DAO
		{
			Organization returned = ourOrganizationDao.read(orgId);
			String val = ourFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(returned);
			ourLog.info(val);
			assertThat(val, containsString("<name value=\"測試醫院\"/>"));
		}
		// Read back through the HTTP API
		{
			Organization returned = ourClient.read(Organization.class, orgId);
			String val = ourFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(returned);
			ourLog.info(val);
			assertThat(val, containsString("<name value=\"測試醫院\"/>"));
		}
	}

	@Test
	public void testTryToCreateResourceWithReferenceThatDoesntExist() {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testTryToCreateResourceWithReferenceThatDoesntExist01");

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testTryToCreateResourceWithReferenceThatDoesntExist01");
		p1.addName().addFamily("testTryToCreateResourceWithReferenceThatDoesntExistFamily01").addGiven("testTryToCreateResourceWithReferenceThatDoesntExistGiven01");
		p1.setManagingOrganization(new ResourceReferenceDt("Organization/1323123232349875324987529835"));

		try {
			ourClient.create().resource(p1).execute().getId();
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Organization/1323123232349875324987529835"));
		}

	}

	@Test
	public void testUpdateRejectsInvalidTypes() throws InterruptedException {
		deleteToken("Patient", Patient.SP_IDENTIFIER, "urn:system", "testUpdateRejectsInvalidTypes");

		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateRejectsInvalidTypes");
		p1.addName().addFamily("Tester").addGiven("testUpdateRejectsInvalidTypes");
		IdDt p1id = ourClient.create().resource(p1).execute().getId();

		Organization p2 = new Organization();
		p2.getNameElement().setValue("testUpdateRejectsInvalidTypes");
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
		ourFhirCtx = FhirContext.forDstu1();
		restServer.setFhirContext(ourFhirCtx);
		
		String serverBase = "http://localhost:" + port + "/fhir/context";

		ourAppCtx = new ClassPathXmlApplicationContext("hapi-fhir-server-resourceproviders-dstu1.xml", "fhir-jpabase-spring-test-config.xml");

		ourDaoConfig = (DaoConfig) ourAppCtx.getBean(DaoConfig.class);

		ourOrganizationDao = (IFhirResourceDao<Organization>) ourAppCtx.getBean("myOrganizationDaoDstu1", IFhirResourceDao.class);

		List<IResourceProvider> rpsDev = (List<IResourceProvider>) ourAppCtx.getBean("myResourceProvidersDstu1", List.class);
		restServer.setResourceProviders(rpsDev);

		restServer.getFhirContext().setNarrativeGenerator(new DefaultThymeleafNarrativeGenerator());

		JpaSystemProviderDstu1 systemProv = ourAppCtx.getBean(JpaSystemProviderDstu1.class, "mySystemProviderDstu1");
		restServer.setPlainProviders(systemProv);

		restServer.setPagingProvider(new FifoMemoryPagingProvider(10));

		ourServer = new Server(port);

		ServletContextHandler proxyHandler = new ServletContextHandler();
		proxyHandler.setContextPath("/");

		ServletHolder servletHolder = new ServletHolder();
		servletHolder.setServlet(restServer);
		proxyHandler.addServlet(servletHolder, "/fhir/context/*");

		ourServer.setHandler(proxyHandler);
		ourServer.start();

		ourClient = ourFhirCtx.newRestfulGenericClient(serverBase);
		ourClient.registerInterceptor(new LoggingInterceptor(true));

	}

}
