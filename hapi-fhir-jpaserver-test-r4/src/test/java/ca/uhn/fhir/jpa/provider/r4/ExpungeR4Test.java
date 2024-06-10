package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertTrue;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.api.model.ExpungeOutcome;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.dao.expunge.ExpungeService;
import ca.uhn.fhir.jpa.delete.ThreadSafeResourceDeleterSvc;
import ca.uhn.fhir.jpa.entity.TermCodeSystem;
import ca.uhn.fhir.jpa.entity.TermCodeSystemVersion;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.jpa.interceptor.CascadingDeleteInterceptor;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.search.PersistedJpaSearchFirstPageBundleProvider;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.PreconditionFailedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceGoneException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.HapiExtensions;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.DecimalType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Quantity;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static ca.uhn.fhir.batch2.jobs.termcodesystem.TermCodeSystemJobConfig.TERM_CODE_SYSTEM_DELETE_JOB_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;


public class ExpungeR4Test extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeR4Test.class);
	private IIdType myOneVersionPatientId;
	private IIdType myTwoVersionPatientId;
	private IIdType myDeletedPatientId;
	private IIdType myOneVersionObservationId;
	private IIdType myTwoVersionObservationId;
	private IIdType myDeletedObservationId;
	private IIdType myOneVersionCodeSystemId;
	private IIdType myTwoVersionCodeSystemIdV1;
	private IIdType myTwoVersionCodeSystemIdV2;
	@Autowired
	private ISearchDao mySearchEntityDao;
	@Autowired
	private ISearchResultDao mySearchResultDao;
	@Autowired
	private ThreadSafeResourceDeleterSvc myThreadSafeResourceDeleterSvc;
	@Autowired
	private ExpungeService myExpungeService;

	@AfterEach
	public void afterDisableExpunge() {
		myStorageSettings.setExpungeEnabled(new JpaStorageSettings().isExpungeEnabled());
		myStorageSettings.setAllowMultipleDelete(new JpaStorageSettings().isAllowMultipleDelete());
		myStorageSettings.setTagStorageMode(new JpaStorageSettings().getTagStorageMode());
		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED);

		myServer.getRestfulServer().getInterceptorService().unregisterInterceptorsIf(t -> t instanceof CascadingDeleteInterceptor);
	}

	@BeforeEach
	public void beforeEnableExpunge() {
		myStorageSettings.setExpungeEnabled(true);
		myStorageSettings.setAllowMultipleDelete(true);
	}

	private void assertExpunged(IIdType theId) {
		try {
			getDao(theId).read(theId);
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
	}

	private void assertNotExpunged(IIdType theId) {
		try {
			getDao(theId).read(theId);
			fail();
		} catch (ResourceGoneException e) {
			// good
		}
	}

	private void assertStillThere(IIdType theId) {
		getDao(theId).read(theId);
	}

	public void createStandardPatients() {
		SearchParameter sp = new SearchParameter();
		sp.setId("SearchParameter/patient-birthdate");
		sp.setType(Enumerations.SearchParamType.DATE);
		sp.setCode("birthdate");
		sp.setExpression("Patient.birthDate");
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		mySearchParameterDao.update(sp);

		sp = new SearchParameter();
		sp.setId("SearchParameter/patient-birthdate-unique");
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.addBase("Patient");
		sp.addComponent()
			.setExpression("Patient")
			.setDefinition("SearchParameter/patient-birthdate");
		sp.addExtension()
			.setUrl(HapiExtensions.EXT_SP_UNIQUE)
			.setValue(new BooleanType(true));
		mySearchParameterDao.update(sp);

		mySearchParamRegistry.forceRefresh();

		Patient p = new Patient();
		p.setId("PT-ONEVERSION");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.getMeta().setSource("http://foo_source");
		p.setActive(true);
		p.addIdentifier().setSystem("foo").setValue("bar");
		p.setBirthDateElement(new DateType("2020-01"));
		p.addName().setFamily("FAM");
		myOneVersionPatientId = myPatientDao.update(p).getId();

		p = new Patient();
		p.setId("PT-TWOVERSION");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		myTwoVersionPatientId = myPatientDao.update(p).getId();
		p.setActive(false);
		myTwoVersionPatientId = myPatientDao.update(p).getId();

		p = new Patient();
		p.setId("PT-DELETED");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		myDeletedPatientId = myPatientDao.update(p).getId();
		myDeletedPatientId = myPatientDao.delete(myDeletedPatientId).getId();

		assertStillThere(myDeletedPatientId.withVersion("1"));
		assertGone(myDeletedPatientId.withVersion("2"));

		// Observation

		Observation o = new Observation();
		o.setStatus(Observation.ObservationStatus.FINAL);
		myOneVersionObservationId = myObservationDao.create(o).getId();

		o = new Observation();
		o.setStatus(Observation.ObservationStatus.FINAL);
		myTwoVersionObservationId = myObservationDao.create(o).getId();
		o.setStatus(Observation.ObservationStatus.AMENDED);
		myTwoVersionObservationId = myObservationDao.update(o).getId();
		CodeableConcept cc = o.getCode();
		cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
		o.setValue(new Quantity().setValueElement(new DecimalType(125.12)).setUnit("CM").setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("cm"));

		o = new Observation();
		o.setStatus(Observation.ObservationStatus.FINAL);
		myDeletedObservationId = myObservationDao.create(o).getId();
		myDeletedObservationId = myObservationDao.delete(myDeletedObservationId).getId();
		cc = o.getCode();
		cc.addCoding().setCode("2345-7").setSystem("http://loinc.org");
		o.setValue(new Quantity().setValueElement(new DecimalType(13.45)).setUnit("DM").setSystem(UcumServiceUtil.UCUM_CODESYSTEM_URL).setCode("dm"));

	}

	public void createStandardCodeSystemWithOneVersion(){
		CodeSystem codeSystem1 = new CodeSystem();
		codeSystem1.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem1.setName("CS1-V1");
		codeSystem1.setVersion("1");
		codeSystem1.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		codeSystem1
				.addConcept().setCode("C").setDisplay("Code C").addDesignation(
						new CodeSystem.ConceptDefinitionDesignationComponent().setLanguage("en").setValue("CodeCDesignation")).addProperty(
						new CodeSystem.ConceptPropertyComponent().setCode("CodeCProperty").setValue(new StringType("CodeCPropertyValue"))
				)
				.addConcept(new CodeSystem.ConceptDefinitionComponent().setCode("CA").setDisplay("Code CA")
						.addConcept(new CodeSystem.ConceptDefinitionComponent().setCode("CAA").setDisplay("Code CAA"))
				)
				.addConcept(new CodeSystem.ConceptDefinitionComponent().setCode("CB").setDisplay("Code CB"));
		codeSystem1
				.addConcept().setCode("D").setDisplay("Code D");
		myOneVersionCodeSystemId = myCodeSystemDao.create(codeSystem1).getId();
	}

	public void createStandardCodeSystemWithTwoVersions(){
		CodeSystem cs2v1 = new CodeSystem();
		cs2v1.setUrl(URL_MY_CODE_SYSTEM_2);
		cs2v1.setVersion("1");
		cs2v1.setName("CS2-V1");
		cs2v1.addConcept().setCode("E").setDisplay("Code E");
		myTwoVersionCodeSystemIdV1 = myCodeSystemDao.create(cs2v1).getId();

		CodeSystem cs2v2 = new CodeSystem();
		cs2v2.setUrl(URL_MY_CODE_SYSTEM_2);
		cs2v2.setVersion("2");
		cs2v2.setName("CS2-V2");
		cs2v2.addConcept().setCode("F").setDisplay("Code F");
		myTwoVersionCodeSystemIdV2 = myCodeSystemDao.create(cs2v2).getId();
	}

	public void createStandardCodeSystems() {
		createStandardCodeSystemWithOneVersion();
		createStandardCodeSystemWithTwoVersions();
	}

	private IFhirResourceDao<?> getDao(IIdType theId) {
		IFhirResourceDao<?> dao;
		switch (theId.getResourceType()) {
			case "Patient":
				dao = myPatientDao;
				break;
			case "Observation":
				dao = myObservationDao;
				break;
			case "CodeSystem":
				dao = myCodeSystemDao;
				break;
			default:
				fail("Restype: " + theId.getResourceType());
				dao = myPatientDao;
		}
		return dao;
	}

	@Test
	public void testDeleteCascade() throws IOException {
		myServer.getRestfulServer().registerInterceptor(new CascadingDeleteInterceptor(myFhirContext, myDaoRegistry, myInterceptorRegistry, myThreadSafeResourceDeleterSvc));

		// setup
		Organization organization = new Organization();
		organization.setName("FOO");
		IIdType organizationId = myOrganizationDao.create(organization).getId().toUnqualifiedVersionless();

		Organization organization2 = new Organization();
		organization2.setName("FOO2");
		organization2.getPartOf().setReference(organizationId.getValue());
		IIdType organizationId2 = myOrganizationDao.create(organization2).getId().toUnqualifiedVersionless();

		Patient patient = new Patient();
		patient.setManagingOrganization(new Reference(organizationId2));
		IIdType patientId = myPatientDao.create(patient).getId().toUnqualifiedVersionless();

		// execute
		String url = myServerBase + "/Organization/" + organizationId.getIdPart() + "?" +
			Constants.PARAMETER_CASCADE_DELETE + "=" + Constants.CASCADE_DELETE
			+ "&" +
			JpaConstants.PARAM_DELETE_EXPUNGE + "=true"
			;
		HttpDelete delete = new HttpDelete(url);
		try (CloseableHttpResponse response = ourHttpClient.execute(delete)) {
			String responseString = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ourLog.info("Response:\n{}", responseString);
			assertEquals(200, response.getStatusLine().getStatusCode());
		}

		runInTransaction(() -> {
			ResourceTable res;
			List<ResourceHistoryTable> versions;

			res = myResourceTableDao.findById(patientId.getIdPartAsLong()).orElseThrow(() -> new IllegalStateException());
			assertNotNull(res.getDeleted());
			versions = myResourceHistoryTableDao.findAllVersionsForResourceIdInOrder(patientId.getIdPartAsLong());
			assertEquals(2, versions.size());
			assertEquals(1L, versions.get(0).getVersion());
			assertNull(versions.get(0).getDeleted());
			assertEquals(2L, versions.get(1).getVersion());
			assertNotNull(versions.get(1).getDeleted());

			res = myResourceTableDao.findById(organizationId.getIdPartAsLong()).orElseThrow(() -> new IllegalStateException());
			assertNotNull(res.getDeleted());
			versions = myResourceHistoryTableDao.findAllVersionsForResourceIdInOrder(organizationId.getIdPartAsLong());
			assertEquals(2, versions.size());
			assertEquals(1L, versions.get(0).getVersion());
			assertNull(versions.get(0).getDeleted());
			assertEquals(2L, versions.get(1).getVersion());
			assertNotNull(versions.get(1).getDeleted());
		});
	}


	@Test
	public void testExpungeInstanceOldVersionsAndDeleted() {
		createStandardPatients();

		Patient p = new Patient();
		p.setId("PT-TWOVERSION");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		p.addName().setFamily("FOO");
		myPatientDao.update(p).getId();

		myPatientDao.expunge(myTwoVersionPatientId.toUnqualifiedVersionless(), new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		// Patients
		assertStillThere(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertExpunged(myTwoVersionPatientId.withVersion("2"));
		assertStillThere(myTwoVersionPatientId.withVersion("3"));
		assertGone(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);
	}

	@Test
	public void testExpungeAllVersionsDeletesRow() {
		// Create then delete
		Patient p = new Patient();
		p.setId("TEST");
		p.setActive(true);
		p.addName().setFamily("FOO");
		myPatientDao.update(p);

		p.setActive(false);
		myPatientDao.update(p);

		myPatientDao.delete(new IdType("Patient/TEST"));

		runInTransaction(() -> assertThat(myResourceTableDao.findAll()).isNotEmpty());
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll()).isNotEmpty());

		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		runInTransaction(() -> assertThat(myResourceTableDao.findAll()).isEmpty());
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll()).isEmpty());
	}

	@Test
	public void testExpungeAllVersionsWithTagsDeletesRow() {
		// Setup
		// Create then delete
		Patient p = new Patient();
		p.setId("TEST");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		p.addName().setFamily("FOO");
		myPatientDao.update(p).getId();

		p.setActive(false);
		myPatientDao.update(p);

		myPatientDao.delete(new IdType("Patient/TEST"));

		runInTransaction(() -> assertThat(myResourceTableDao.findAll()).isNotEmpty());
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll()).isNotEmpty());

		// Test
		myCaptureQueriesListener.clear();
		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		// Verify
		assertEquals(8, myCaptureQueriesListener.countSelectQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countUpdateQueriesForCurrentThread());
		assertEquals(0, myCaptureQueriesListener.countInsertQueriesForCurrentThread());
		assertEquals(8, myCaptureQueriesListener.countDeleteQueriesForCurrentThread());

		runInTransaction(() -> assertThat(myResourceTableDao.findAll()).isEmpty());
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll()).isEmpty());

	}

	@Test
	public void testExpungeInstanceVersionCurrentVersion() {
		createStandardPatients();

		try {
			myPatientDao.expunge(myTwoVersionPatientId.withVersion("2"), new ExpungeOptions()
				.setExpungeDeletedResources(true)
				.setExpungeOldVersions(true), null);
			fail();
		} catch (PreconditionFailedException e) {
			assertEquals(Msg.code(969) + "Can not perform version-specific expunge of resource Patient/PT-TWOVERSION/_history/2 as this is the current version", e.getMessage());
		}
	}

	@Test
	public void testExpungeInstanceVersionOldVersionsAndDeleted() {
		createStandardPatients();

		Patient p = new Patient();
		p.setId("PT-TWOVERSION");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		p.addName().setFamily("FOO");
		myPatientDao.update(p).getId();

		myPatientDao.expunge(myTwoVersionPatientId.withVersion("2"), new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		// Patients
		assertStillThere(myOneVersionPatientId);
		assertStillThere(myTwoVersionPatientId.withVersion("1"));
		assertExpunged(myTwoVersionPatientId.withVersion("2"));
		assertStillThere(myTwoVersionPatientId.withVersion("3"));
		assertGone(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);
	}

	@Test
	public void testExpungeSystemOldVersionsAndDeleted() {
		createStandardPatients();

		mySystemDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		// Only deleted and prior patients
		assertStillThere(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertStillThere(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId);

		// Also observations deleted
		assertStillThere(myOneVersionObservationId);
		assertExpunged(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertExpunged(myDeletedObservationId);
	}

	@Test
	public void testExpungeTypeDeletedResources() {
		createStandardPatients();

		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(false), null);

		// Only deleted and prior patients
		assertStillThere(myOneVersionPatientId);
		assertStillThere(myTwoVersionPatientId.withVersion("1"));
		assertStillThere(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);
	}

	@Test
	public void testExpungeTypeOldVersions() {
		createStandardPatients();

		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(false)
			.setExpungeOldVersions(true), null);

		// Only deleted and prior patients
		assertStillThere(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertStillThere(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId.withVersion("1"));
		assertGone(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);
	}

	@Test
	public void testExpungeSystemEverything() {
		createStandardPatients();

		mySystemDao.expunge(new ExpungeOptions()
			.setExpungeEverything(true), null);

		// Everything deleted
		assertExpunged(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertExpunged(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId.withVersion("1"));
		assertExpunged(myDeletedPatientId);

		// Everything deleted
		assertExpunged(myOneVersionObservationId);
		assertExpunged(myTwoVersionObservationId.withVersion("1"));
		assertExpunged(myTwoVersionObservationId.withVersion("2"));
		assertExpunged(myDeletedObservationId);
	}

	@Test
	public void testExpungeSystemEverythingWithNormalizedQuantitySearchSupported() {
		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
		createStandardPatients();

		mySystemDao.expunge(new ExpungeOptions()
			.setExpungeEverything(true), null);

		// Everything deleted
		assertExpunged(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertExpunged(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId.withVersion("1"));
		assertExpunged(myDeletedPatientId);

		// Everything deleted
		assertExpunged(myOneVersionObservationId);
		assertExpunged(myTwoVersionObservationId.withVersion("1"));
		assertExpunged(myTwoVersionObservationId.withVersion("2"));
		assertExpunged(myDeletedObservationId);
	}

	@Test
	public void testExpungeSystemEverythingWithNormalizedQuantityStorageSupported() {
		myStorageSettings.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_STORAGE_SUPPORTED);
		createStandardPatients();

		mySystemDao.expunge(new ExpungeOptions()
			.setExpungeEverything(true), null);

		// Everything deleted
		assertExpunged(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertExpunged(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId.withVersion("1"));
		assertExpunged(myDeletedPatientId);

		// Everything deleted
		assertExpunged(myOneVersionObservationId);
		assertExpunged(myTwoVersionObservationId.withVersion("1"));
		assertExpunged(myTwoVersionObservationId.withVersion("2"));
		assertExpunged(myDeletedObservationId);
	}

	@Test
	public void testExpungeSystemEverythingDeletedResourceCount() {
		createStandardPatients();

		ExpungeOutcome outcome = mySystemDao.expunge(new ExpungeOptions()
			.setExpungeEverything(true), null);

		// Make sure the deleted resource entities count is correct
		assertEquals(8, outcome.getDeletedCount());
	}

	@Test
	public void testExpungeTypeOldVersionsAndDeleted() {
		createStandardPatients();

		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		// Only deleted and prior patients
		assertStillThere(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertStillThere(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId);

		// No observations deleted
		assertStillThere(myOneVersionObservationId);
		assertStillThere(myTwoVersionObservationId.withVersion("1"));
		assertStillThere(myTwoVersionObservationId.withVersion("2"));
		assertGone(myDeletedObservationId);
	}

	@Test
	public void testExpungeEverythingWhereResourceInSearchResults() {
		createStandardPatients();

		await().until(() -> runInTransaction(() -> mySearchEntityDao.count() == 0));
		await().until(() -> runInTransaction(() -> mySearchResultDao.count() == 0));

		PersistedJpaSearchFirstPageBundleProvider search = (PersistedJpaSearchFirstPageBundleProvider) myPatientDao.search(new SearchParameterMap());
		assertEquals(PersistedJpaSearchFirstPageBundleProvider.class, search.getClass());
		assertEquals(2, search.size().intValue());
		assertThat(search.getResources(0, 2)).hasSize(2);

		await().until(() -> runInTransaction(() -> mySearchEntityDao.count() == 1));
		await().until(() -> runInTransaction(() -> mySearchResultDao.count() == 2));

		mySystemDao.expunge(new ExpungeOptions()
			.setExpungeEverything(true), null);

		// Everything deleted
		assertExpunged(myOneVersionPatientId);
		assertExpunged(myTwoVersionPatientId.withVersion("1"));
		assertExpunged(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId.withVersion("1"));
		assertExpunged(myDeletedPatientId);

		// Everything deleted
		assertExpunged(myOneVersionObservationId);
		assertExpunged(myTwoVersionObservationId.withVersion("1"));
		assertExpunged(myTwoVersionObservationId.withVersion("2"));
		assertExpunged(myDeletedObservationId);
	}

	@Test
	public void testExpungeByTypeAndNoId_NonVersionedTags() {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.NON_VERSIONED);

		Patient p = new Patient();
		p.setId("PT-DELETED");
		p.getMeta().addTag().setSystem("http://foo").setCode("bar");
		p.setActive(true);
		myDeletedPatientId = myPatientDao.update(p).getId();
		myDeletedPatientId = myPatientDao.delete(myDeletedPatientId).getId();

		myCaptureQueriesListener.clear();
		myExpungeService.expunge("Patient", null, new ExpungeOptions().setExpungeDeletedResources(true), null);
		myCaptureQueriesListener.logAllQueries();

		assertExpunged(myDeletedPatientId.withVersion("2"));
	}

	@Test
	public void testExpungeDeletedWhereResourceInSearchResults() {
		createStandardPatients();

		IBundleProvider search = myPatientDao.search(new SearchParameterMap());
		assertEquals(2, search.size().intValue());
		List<IBaseResource> resources = search.getResources(0, 2);
		myPatientDao.delete(resources.get(0).getIdElement());

		runInTransaction(() -> {
			assertEquals(2, mySearchResultDao.count());
		});


		mySystemDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true), null);

		// Everything deleted
		assertExpunged(myOneVersionPatientId);
		assertStillThere(myTwoVersionPatientId.withVersion("1"));
		assertStillThere(myTwoVersionPatientId.withVersion("2"));
		assertExpunged(myDeletedPatientId.withVersion("1"));
		assertExpunged(myDeletedPatientId);

	}


	@Test
	public void testExpungeForcedIdAndThenReuseIt() {
		// Create with forced ID, and an Observation that links to it
		Patient p = new Patient();
		p.setId("TEST");
		p.setActive(true);
		p.addName().setFamily("FOO");
		myPatientDao.update(p);

		Observation obs = new Observation();
		obs.setId("OBS");
		obs.getSubject().setReference("Patient/TEST");
		myObservationDao.update(obs);

		// Make sure read works
		p = myPatientDao.read(new IdType("Patient/TEST"));
		assertTrue(p.getActive());

		// Make sure search by ID works
		IBundleProvider outcome = myPatientDao.search(SearchParameterMap.newSynchronous("_id", new TokenParam("Patient/TEST")));
		p = (Patient) outcome.getResources(0, 1).get(0);
		assertTrue(p.getActive());

		// Make sure search by Reference works
		outcome = myObservationDao.search(SearchParameterMap.newSynchronous(Observation.SP_SUBJECT, new ReferenceParam("Patient/TEST")));
		obs = (Observation) outcome.getResources(0, 1).get(0);
		assertEquals("OBS", obs.getIdElement().getIdPart());

		// Delete and expunge
		myObservationDao.delete(new IdType("Observation/OBS"));
		myPatientDao.delete(new IdType("Patient/TEST"));
		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);
		myObservationDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);
		runInTransaction(() -> assertThat(myResourceTableDao.findAll()).isEmpty());
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll()).isEmpty());

		// Create again with the same forced ID
		p = new Patient();
		p.setId("TEST");
		p.setActive(true);
		p.addName().setFamily("FOO");
		myPatientDao.update(p);

		obs = new Observation();
		obs.setId("OBS");
		obs.getSubject().setReference("Patient/TEST");
		myObservationDao.update(obs);

		// Make sure read works
		p = myPatientDao.read(new IdType("Patient/TEST"));
		assertTrue(p.getActive());

		// Make sure search works
		outcome = myPatientDao.search(SearchParameterMap.newSynchronous("_id", new TokenParam("Patient/TEST")));
		p = (Patient) outcome.getResources(0, 1).get(0);
		assertTrue(p.getActive());

		// Make sure search by Reference works
		outcome = myObservationDao.search(SearchParameterMap.newSynchronous(Observation.SP_SUBJECT, new ReferenceParam("Patient/TEST")));
		obs = (Observation) outcome.getResources(0, 1).get(0);
		assertEquals("OBS", obs.getIdElement().getIdPart());

		// Delete and expunge
		myObservationDao.delete(new IdType("Observation/OBS"));
		myPatientDao.delete(new IdType("Patient/TEST"));
		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);
		myObservationDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);
		runInTransaction(() -> assertThat(myResourceTableDao.findAll()).isEmpty());
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll()).isEmpty());

	}


	@Test
	public void testExpungeOperationRespectsConfiguration() {
		// set up
		myStorageSettings.setExpungeEnabled(false);
		myStorageSettings.setAllowMultipleDelete(false);

		createStandardPatients();

		// execute
		try {
			myPatientDao.expunge(myOneVersionPatientId,
				new ExpungeOptions().setExpungeOldVersions(true), null);
			fail();
		} catch (MethodNotAllowedException e) {
			assertEquals("HAPI-0968: $expunge is not enabled on this server", e.getMessage());
		}

		try {
			myPatientDao.expunge(myOneVersionPatientId.toVersionless(),
				new ExpungeOptions().setExpungeOldVersions(true), null);
			fail();
		} catch (MethodNotAllowedException e) {
			assertEquals("HAPI-0968: $expunge is not enabled on this server", e.getMessage());
		}

		try {
			myPatientDao.expunge(null,
				new ExpungeOptions().setExpungeOldVersions(true), null);
			fail();
		} catch (MethodNotAllowedException e) {
			assertEquals("HAPI-0968: $expunge is not enabled on this server", e.getMessage());
		}

		try {
			mySystemDao.expunge(new ExpungeOptions().setExpungeEverything(true), null);
			fail();
		} catch (MethodNotAllowedException e) {
			assertEquals("HAPI-2080: $expunge is not enabled on this server", e.getMessage());
		}

		myStorageSettings.setExpungeEnabled(true);
		try {
			mySystemDao.expunge(new ExpungeOptions().setExpungeEverything(true), null);
			fail();
		} catch (MethodNotAllowedException e) {
			assertEquals("HAPI-2081: Multiple delete is not enabled on this server", e.getMessage());
		}

		// re-enable multi-delete for clean-up
		myStorageSettings.setAllowMultipleDelete(true);
	}

	@Test
	public void testExpungeRaceConditionsWithLowThreadCountAndBatchSize() {
		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		final int numPatients = 5;
		myStorageSettings.setExpungeThreadCount(2);
		myStorageSettings.setExpungeBatchSize(2);

		List<Patient> patients = createPatientsWithForcedIds(numPatients);
		patients = updatePatients(patients, 1);
		deletePatients(patients);

		int expectedPatientHistoryRecords = 15; // 5 resources x 3 versions
		int actualPatientHistoryRecords = myPatientDao.history(null, null, null, requestDetails).getAllResources().size();
		assertEquals(expectedPatientHistoryRecords, actualPatientHistoryRecords);

		int expungeLimit = numPatients;
		ExpungeOptions expungeOptions = new ExpungeOptions()
			.setLimit(numPatients)
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true);

		myPatientDao.expunge(expungeOptions, requestDetails);

		int maximumRemainingPatientHistoryRecords = expectedPatientHistoryRecords - expungeLimit;
		int actualRemainingPatientHistoryRecords = myPatientDao.history(null, null, null, requestDetails).getAllResources().size();

		// Note that the limit used in ExpungeOptions is meant to be a rough throttle.
		// We care that AT LEAST the specified number of resources are expunged and not if the limit is exceeded.
		assertTrue(actualRemainingPatientHistoryRecords <= maximumRemainingPatientHistoryRecords);
	}

	@Test
	public void testDeleteCodeSystemByUrlThenExpunge() {
		createStandardCodeSystems();

		myCodeSystemDao.deleteByUrl("CodeSystem?url=" + URL_MY_CODE_SYSTEM, null);
		myTerminologyDeferredStorageSvc.saveDeferred();
		myBatch2JobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_DELETE_JOB_NAME);
		myCodeSystemDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		assertExpunged(myOneVersionCodeSystemId);
		assertStillThere(myTwoVersionCodeSystemIdV1);
		assertStillThere(myTwoVersionCodeSystemIdV2);
		runInTransaction(() -> {
			verifyOneVersionCodeSystemChildrenExpunged();
			verifyTwoVersionCodeSystemV1AndChildrenStillThere();
			verifyTwoVersionCodeSystemV2AndChildrenStillThere();
		});

		myCodeSystemDao.deleteByUrl("CodeSystem?url=" + URL_MY_CODE_SYSTEM_2, null);
		myTerminologyDeferredStorageSvc.saveDeferred();
		myBatch2JobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_DELETE_JOB_NAME);
		myCodeSystemDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		assertExpunged(myTwoVersionCodeSystemIdV1);
		assertExpunged(myTwoVersionCodeSystemIdV2);
		runInTransaction(this::verifyCodeSystemsAndChildrenExpunged);
	}

	@Test
	public void testExpungeCodeSystem_whenCsIsBeingBatchDeleted_willGracefullyHandleConstraintViolationException(){
		//set up
		createStandardCodeSystemWithOneVersion();

		myCodeSystemDao.deleteByUrl("CodeSystem?url=" + URL_MY_CODE_SYSTEM, null);
		myTerminologyDeferredStorageSvc.saveDeferred();

		try {
			// execute
			myCodeSystemDao.expunge(new ExpungeOptions()
				.setExpungeDeletedResources(true)
				.setExpungeOldVersions(true), null);
			fail();
		} catch (PreconditionFailedException preconditionFailedException){
			// verify
			assertThat(preconditionFailedException.getMessage()).startsWith("HAPI-2415: The resource could not be expunged. It is likely due to unfinished asynchronous deletions, please try again later");
		}

		myBatch2JobHelper.awaitAllJobsOfJobDefinitionIdToComplete(TERM_CODE_SYSTEM_DELETE_JOB_NAME);
	}

	@ParameterizedTest
	@ValueSource(strings = {"instance", "type", "system"})
	public void testExpungeNotAllowedWhenNotEnabled(String level) {
		// setup
		myStorageSettings.setExpungeEnabled(false);

		Patient p = new Patient();
		p.setActive(true);
		p.addName().setFamily("FOO");
		IIdType patientId = myPatientDao.create(p).getId();

		myPatientDao.delete(patientId);

		runInTransaction(() -> assertThat(myResourceTableDao.findAll()).isNotEmpty());
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll()).isNotEmpty());

		// execute & verify
		MethodNotAllowedException exception = null;
		switch (level) {
			case "instance":
				exception = assertThrows(MethodNotAllowedException.class, () -> {
					myPatientDao.expunge(patientId, new ExpungeOptions()
						.setExpungeDeletedResources(true)
						.setExpungeOldVersions(true), null);
				});
				break;

			case "type":
				exception = assertThrows(MethodNotAllowedException.class, () -> {
					myPatientDao.expunge(new ExpungeOptions()
						.setExpungeDeletedResources(true)
						.setExpungeOldVersions(true), null);
				});
				break;

			case "system":
				exception = assertThrows(MethodNotAllowedException.class, () -> {
					mySystemDao.expunge(new ExpungeOptions()
						.setExpungeEverything(true), null);
				});
		}

		assertStillThere(patientId);
		assertThat(exception.getMessage()).contains("$expunge is not enabled on this server");
	}

	private List<Patient> createPatientsWithForcedIds(int theNumPatients) {
		RequestDetails requestDetails = new SystemRequestDetails();
		List<Patient> createdPatients = new ArrayList<>();
		for(int i = 1; i <= theNumPatients; i++){
			Patient patient = new Patient();
			patient.setId("pt-00" + i);
			patient.getNameFirstRep().addGiven("Patient-"+i);
			Patient createdPatient = (Patient)myPatientDao.update(patient, requestDetails).getResource();
			createdPatients.add(createdPatient);
		}
		return createdPatients;
	}

	private List<Patient> updatePatients(List<Patient> thePatients, int theUpdateNumber) {
		RequestDetails requestDetails = new SystemRequestDetails();
		List<Patient> updatedPatients = new ArrayList<>();
		for(Patient patient : thePatients){
			patient.getNameFirstRep().addGiven("Update-" + theUpdateNumber);
			Patient updatedPatient = (Patient)myPatientDao.update(patient, requestDetails).getResource();
			updatedPatients.add(updatedPatient);
		}
		return updatedPatients;
	}

	private void deletePatients(List<Patient> thePatients){
		RequestDetails requestDetails = new SystemRequestDetails();
		for(Patient patient : thePatients){
			myPatientDao.delete(patient.getIdElement(), requestDetails);
		}
	}

	private void verifyOneVersionCodeSystemChildrenExpunged() {
		List<TermCodeSystemVersion> myOneVersionCodeSystemVersions = myTermCodeSystemVersionDao.findByCodeSystemResourcePid(myOneVersionCodeSystemId.getIdPartAsLong());
		assertThat(myOneVersionCodeSystemVersions).isEmpty();
		assertThat(myTermConceptDesignationDao.findAll()).isEmpty();
		assertThat(myTermConceptPropertyDao.findAll()).isEmpty();
		assertThat(myTermConceptParentChildLinkDao.findAll()).isEmpty();
		List<TermConcept> existingCodeSystemConcepts = myTermConceptDao.findAll();
		for (TermConcept tc : existingCodeSystemConcepts) {
			if (tc.getCode().charAt(0) == 'C' || tc.getCode().charAt(0) == 'D') {
				fail();
			}
		}
	}

	private void verifyTwoVersionCodeSystemV1AndChildrenStillThere() {
		TermCodeSystem myTwoVersionCodeSystem = myTermCodeSystemDao.findByResourcePid(myTwoVersionCodeSystemIdV2.getIdPartAsLong());
		TermCodeSystemVersion myTwoVersionCodeSystemVersion1 = verifyTermCodeSystemVersionExistsWithDisplayName("CS2-V1");
		assertThat(myTwoVersionCodeSystemVersion1.getPid()).isNotEqualTo(myTwoVersionCodeSystem.getCurrentVersion().getPid());
		List<TermConcept> myTwoVersionCodeSystemVersion1Concepts = new ArrayList(myTwoVersionCodeSystemVersion1.getConcepts());
		assertThat(myTwoVersionCodeSystemVersion1Concepts).hasSize(1);
		TermConcept conceptE = myTwoVersionCodeSystemVersion1Concepts.get(0);
		assertEquals("E", conceptE.getCode());
	}

	private void verifyTwoVersionCodeSystemV2AndChildrenStillThere() {
		TermCodeSystem myTwoVersionCodeSystem = myTermCodeSystemDao.findByResourcePid(myTwoVersionCodeSystemIdV2.getIdPartAsLong());
		TermCodeSystemVersion myTwoVersionCodeSystemVersion2 = verifyTermCodeSystemVersionExistsWithDisplayName("CS2-V2");
		assertEquals(myTwoVersionCodeSystem.getCurrentVersion().getPid(), myTwoVersionCodeSystemVersion2.getPid());
		List<TermConcept> myTwoVersionCodeSystemVersion2Concepts = new ArrayList(myTwoVersionCodeSystemVersion2.getConcepts());
		assertThat(myTwoVersionCodeSystemVersion2Concepts).hasSize(1);
		TermConcept conceptF = myTwoVersionCodeSystemVersion2Concepts.get(0);
		assertEquals("F", conceptF.getCode());
	}

	private TermCodeSystemVersion verifyTermCodeSystemVersionExistsWithDisplayName(String theDisplayName) {
		List<TermCodeSystemVersion> myCodeSystemVersions = myTermCodeSystemVersionDao.findAll();
		for (TermCodeSystemVersion csv : myCodeSystemVersions) {
			if (csv.getCodeSystemDisplayName().equals(theDisplayName)) {
				return csv;
			}
		}
		fail();
		return null;
	}

	private void verifyCodeSystemsAndChildrenExpunged() {
		assertThat(myTermCodeSystemVersionDao.findAll()).isEmpty();
		assertThat(myTermConceptDesignationDao.findAll()).isEmpty();
		assertThat(myTermConceptPropertyDao.findAll()).isEmpty();
		assertThat(myTermConceptParentChildLinkDao.findAll()).isEmpty();
		assertThat(myTermConceptDao.findAll()).isEmpty();
		assertThat(myResourceTableDao.findAll()).isEmpty();
		assertThat(myResourceHistoryTableDao.findAll()).isEmpty();
	}
}
