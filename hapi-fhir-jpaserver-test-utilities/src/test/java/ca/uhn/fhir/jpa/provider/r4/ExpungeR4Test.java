package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.ExpungeOptions;
import ca.uhn.fhir.jpa.dao.data.ISearchDao;
import ca.uhn.fhir.jpa.dao.data.ISearchResultDao;
import ca.uhn.fhir.jpa.interceptor.CascadingDeleteInterceptor;
import ca.uhn.fhir.jpa.model.entity.NormalizedQuantitySearchLevel;
import ca.uhn.fhir.jpa.model.entity.ResourceHistoryTable;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.jpa.model.util.UcumServiceUtil;
import ca.uhn.fhir.jpa.search.PersistedJpaSearchFirstPageBundleProvider;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenParam;
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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class ExpungeR4Test extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ExpungeR4Test.class);
	private IIdType myOneVersionPatientId;
	private IIdType myTwoVersionPatientId;
	private IIdType myDeletedPatientId;
	private IIdType myOneVersionObservationId;
	private IIdType myTwoVersionObservationId;
	private IIdType myDeletedObservationId;
	@Autowired
	private ISearchDao mySearchEntityDao;
	@Autowired
	private ISearchResultDao mySearchResultDao;

	@AfterEach
	public void afterDisableExpunge() {
		myDaoConfig.setExpungeEnabled(new DaoConfig().isExpungeEnabled());
		myModelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_NOT_SUPPORTED);

		ourRestServer.getInterceptorService().unregisterInterceptorsIf(t -> t instanceof CascadingDeleteInterceptor);
	}

	@BeforeEach
	public void beforeEnableExpunge() {
		myDaoConfig.setExpungeEnabled(true);
	}

	private void assertExpunged(IIdType theId) {
		try {
			getDao(theId).read(theId);
			fail();
		} catch (ResourceNotFoundException e) {
			// good
		}
	}

	private void assertGone(IIdType theId) {
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

	private IFhirResourceDao<?> getDao(IIdType theId) {
		IFhirResourceDao<?> dao;
		switch (theId.getResourceType()) {
			case "Patient":
				dao = myPatientDao;
				break;
			case "Observation":
				dao = myObservationDao;
				break;
			default:
				fail("Restype: " + theId.getResourceType());
				dao = myPatientDao;
		}
		return dao;
	}

	@Test
	public void testDeleteCascade() throws IOException {
		ourRestServer.registerInterceptor(new CascadingDeleteInterceptor(myFhirContext, myDaoRegistry, myInterceptorRegistry));

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
		String url = ourServerBase + "/Organization/" + organizationId.getIdPart() + "?" +
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

		runInTransaction(() -> assertThat(myResourceTableDao.findAll(), not(empty())));
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll(), not(empty())));
		runInTransaction(() -> assertThat(myForcedIdDao.findAll(), not(empty())));

		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		runInTransaction(() -> assertThat(myResourceTableDao.findAll(), empty()));
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll(), empty()));
		runInTransaction(() -> assertThat(myForcedIdDao.findAll(), empty()));

	}

	@Test
	public void testExpungeAllVersionsWithTagsDeletesRow() {
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

		runInTransaction(() -> assertThat(myResourceTableDao.findAll(), not(empty())));
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll(), not(empty())));
		runInTransaction(() -> assertThat(myForcedIdDao.findAll(), not(empty())));

		myPatientDao.expunge(new ExpungeOptions()
			.setExpungeDeletedResources(true)
			.setExpungeOldVersions(true), null);

		runInTransaction(() -> assertThat(myResourceTableDao.findAll(), empty()));
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll(), empty()));
		runInTransaction(() -> assertThat(myForcedIdDao.findAll(), empty()));

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
		myModelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_SEARCH_SUPPORTED);
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
		myModelConfig.setNormalizedQuantitySearchLevel(NormalizedQuantitySearchLevel.NORMALIZED_QUANTITY_STORAGE_SUPPORTED);
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
		assertEquals(2, search.getResources(0, 2).size());

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
		runInTransaction(() -> assertThat(myResourceTableDao.findAll(), empty()));
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll(), empty()));
		runInTransaction(() -> assertThat(myForcedIdDao.findAll(), empty()));

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
		runInTransaction(() -> assertThat(myResourceTableDao.findAll(), empty()));
		runInTransaction(() -> assertThat(myResourceHistoryTableDao.findAll(), empty()));
		runInTransaction(() -> assertThat(myForcedIdDao.findAll(), empty()));

	}


}
