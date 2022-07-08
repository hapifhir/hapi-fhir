package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4SystemTest;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.partition.SystemRequestDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.util.MultimapCollector;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.ExplanationOfBenefit;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class PatientIdPartitionInterceptorTest extends BaseJpaR4SystemTest {

	public static final int ALTERNATE_DEFAULT_ID = -1;
	private PatientIdPartitionInterceptor mySvc;
	private ForceOffsetSearchModeInterceptor myForceOffsetSearchModeInterceptor;

	@Autowired
	private ISearchParamExtractor mySearchParamExtractor;

	@BeforeEach
	public void before() {
		mySvc = new PatientIdPartitionInterceptor(myFhirContext, mySearchParamExtractor);
		myForceOffsetSearchModeInterceptor = new ForceOffsetSearchModeInterceptor();

		myInterceptorRegistry.registerInterceptor(mySvc);
		myInterceptorRegistry.registerInterceptor(myForceOffsetSearchModeInterceptor);

		myPartitionSettings.setPartitioningEnabled(true);
		myPartitionSettings.setUnnamedPartitionMode(true);
		myPartitionSettings.setDefaultPartitionId(ALTERNATE_DEFAULT_ID);


	}

	@AfterEach
	public void after() {
		myInterceptorRegistry.unregisterInterceptor(mySvc);
		myInterceptorRegistry.unregisterInterceptor(myForceOffsetSearchModeInterceptor);

		myPartitionSettings.setPartitioningEnabled(false);
		myPartitionSettings.setUnnamedPartitionMode(new PartitionSettings().isUnnamedPartitionMode());
		myPartitionSettings.setDefaultPartitionId(new PartitionSettings().getDefaultPartitionId());
	}


	@Test
	public void testCreatePatient_ClientAssignedId() {
		createPatientA();

		runInTransaction(() -> {
			ResourceTable pt = myResourceTableDao.findAll().iterator().next();
			assertEquals("A", pt.getIdDt().getIdPart());
			assertEquals(65, pt.getPartitionId().getPartitionId());
		});
	}

	/**
	 * This is an edge case where a client assigned ID has a Java hashCode equal to Integer.MIN_VALUE.
	 */
	@Test
	public void testCreatePatient_polygenelubricants() {
		Patient patient = new Patient();
		patient.setId("Patient/polygenelubricants");
		patient.setActive(true);
		DaoMethodOutcome update = myPatientDao.update(patient);

		runInTransaction(() -> {
			ResourceTable pt = myResourceTableDao.findAll().iterator().next();
			assertEquals("polygenelubricants", pt.getIdDt().getIdPart());
			assertEquals(8648, pt.getPartitionId().getPartitionId());
		});
	}

	@Test
	public void testCreatePatient_NonClientAssignedId() {
		Patient patient = new Patient();
		patient.setActive(true);
		try {
			myPatientDao.create(patient);
			fail();
		} catch (MethodNotAllowedException e) {
			assertEquals(Msg.code(1321) + "Patient resource IDs must be client-assigned in patient compartment mode", e.getMessage());
		}
	}

	@Test
	public void testCreateObservation_ValidMembershipInCompartment() {
		createPatientA();

		Observation obs = new Observation();
		obs.getSubject().setReference("Patient/A");
		Long id = myObservationDao.create(obs).getId().getIdPartAsLong();

		runInTransaction(() -> {
			ResourceTable observation = myResourceTableDao.findById(id).orElseThrow(() -> new IllegalArgumentException());
			assertEquals("Observation", observation.getResourceType());
			assertEquals(65, observation.getPartitionId().getPartitionId());
		});
	}

	/**
	 * Encounter.subject has a FHIRPath expression with a resolve() on it
	 */
	@Test
	public void testCreateEncounter_ValidMembershipInCompartment() {
		createPatientA();

		Encounter encounter = new Encounter();
		encounter.getSubject().setReference("Patient/A");
		Long id = myEncounterDao.create(encounter).getId().getIdPartAsLong();

		runInTransaction(() -> {
			ResourceTable observation = myResourceTableDao.findById(id).orElseThrow(() -> new IllegalArgumentException());
			assertEquals("Encounter", observation.getResourceType());
			assertEquals(65, observation.getPartitionId().getPartitionId());
		});
	}

	/**
	 * Type is not in the patient compartment
	 */
	@Test
	public void testCreateOrganization_ValidMembershipInCompartment() {
		Organization org = new Organization();
		org.setName("Foo");
		Long id = myOrganizationDao.create(org).getId().getIdPartAsLong();

		runInTransaction(() -> {
			ResourceTable observation = myResourceTableDao.findById(id).orElseThrow(() -> new IllegalArgumentException());
			assertEquals("Organization", observation.getResourceType());
			assertEquals(ALTERNATE_DEFAULT_ID, observation.getPartitionId().getPartitionId().intValue());
		});
	}

	@Test
	public void testReadPatient_Good() {
		createPatientA();

		myCaptureQueriesListener.clear();
		Patient patient = myPatientDao.read(new IdType("Patient/A"), mySrd);
		assertTrue(patient.getActive());
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(3, myCaptureQueriesListener.getSelectQueries().size());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false), containsString("forcedid0_.PARTITION_ID in (?)"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(false, false), containsString("where resourceta0_.PARTITION_ID=? and resourceta0_.RES_ID=?"));
	}

	@Test
	public void testReadObservation_Good() {
		createPatientA();

		Observation obs = new Observation();
		obs.getSubject().setReference("Patient/A");
		Long id = myObservationDao.create(obs).getId().getIdPartAsLong();

		try {
			myObservationDao.read(new IdType("Observation/" + id), mySrd);
		} catch (MethodNotAllowedException e) {
			assertEquals("This server is not able to handle this request of type READ", e.getMessage());
		}
	}

	@Test
	public void testReadPatientHistory_Good() {
		Patient patientA = createPatientA();
		patientA.setGender(Enumerations.AdministrativeGender.MALE);
		myPatientDao.update(patientA);


		IdType patientVersionOne = new IdType("Patient", "A", "1");
		myCaptureQueriesListener.clear();
		Patient patient = myPatientDao.read(patientVersionOne);
		assertEquals("1", patient.getIdElement().getVersionIdPart());

		myCaptureQueriesListener.logSelectQueries();
		assertEquals(4, myCaptureQueriesListener.getSelectQueries().size());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false), containsString("PARTITION_ID in (?)"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(false, false), containsString("PARTITION_ID="));

	}


	@Test
	public void testSearchPatient_Good() {
		createPatientA();

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myPatientDao.search(SearchParameterMap.newSynchronous("_id", new TokenParam("A")), mySrd);
		assertEquals(1, outcome.size());
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(3, myCaptureQueriesListener.getSelectQueries().size());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false), containsString("forcedid0_.PARTITION_ID in (?)"));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(false, false), containsString("t0.PARTITION_ID = ?"));
	}

	@Test
	public void testSearchObservation_Good() {
		createPatientA();
		createObservationB();

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myObservationDao.search(SearchParameterMap.newSynchronous("subject", new ReferenceParam("Patient/A")), mySrd);
		assertEquals(1, outcome.size());
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.getSelectQueries().size());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false), containsString("SELECT t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 WHERE ((t0.PARTITION_ID = ?)"));

		// Typed
		myCaptureQueriesListener.clear();
		ReferenceParam referenceParam = new ReferenceParam();
		referenceParam.setValueAsQueryToken(myFhirContext, "subject", ":Patient", "A");
		outcome = myObservationDao.search(SearchParameterMap.newSynchronous("subject", referenceParam), mySrd);
		assertEquals(1, outcome.size());
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.getSelectQueries().size());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false), containsString("SELECT t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 WHERE ((t0.PARTITION_ID = ?)"));
	}

	@Test
	public void testSearchObservation_NoCompartmentMembership() {
		createPatientA();
		createObservationB();

		myCaptureQueriesListener.clear();
		myObservationDao.search(SearchParameterMap.newSynchronous(), mySrd);
		myCaptureQueriesListener.logSelectQueries();
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE ((t0.RES_TYPE = 'Observation') AND (t0.RES_DELETED_AT IS NULL)) limit '10'", myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false));
	}

	@Test
	public void testSearchObservation_MultipleCompartmentMembership() {
		createPatientA();
		createObservationB();

		// Multiple ANDs
		try {
			myObservationDao.search(SearchParameterMap.newSynchronous()
					.add("subject", new TokenParam("http://foo", "1"))
					.add("subject", new TokenParam("http://foo", "2"))
				, mySrd);
		} catch (MethodNotAllowedException e) {
			assertEquals(Msg.code(1325) + "Multiple values for parameter subject is not supported in patient compartment mode", e.getMessage());
		}

		// Multiple ORs
		try {
			myObservationDao.search(SearchParameterMap.newSynchronous()
				.add(
					"subject", new TokenOrListParam("http://foo", "1", "2")
				), mySrd);
		} catch (MethodNotAllowedException e) {
			assertEquals(Msg.code(1324) + "Multiple values for parameter subject is not supported in patient compartment mode", e.getMessage());
		}
	}

	@Test
	public void testSearchObservation_ChainedValue() {
		createPatientA();
		createObservationB();

		// Chain
		try {
			myObservationDao.search(SearchParameterMap.newSynchronous().add("subject", new ReferenceParam("identifier", "http://foo|123")), mySrd);
		} catch (MethodNotAllowedException e) {
			assertEquals(Msg.code(1322) + "The parameter subject.identifier is not supported in patient compartment mode", e.getMessage());
		}


		// Missing
		try {
			myObservationDao.search(SearchParameterMap.newSynchronous().add("subject", new ReferenceParam("Patient/ABC").setMdmExpand(true)), mySrd);
		} catch (MethodNotAllowedException e) {
			assertEquals(Msg.code(1322) + "The parameter subject:mdm is not supported in patient compartment mode", e.getMessage());
		}

	}

	/**
	 * Type is not in the patient compartment
	 */
	@Test
	public void testSearchOrganization_Good() {
		createOrganizationC();

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myOrganizationDao.search(SearchParameterMap.newSynchronous(), mySrd);
		assertEquals(1, outcome.size());
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(2, myCaptureQueriesListener.getSelectQueries().size());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false), containsString("t0.PARTITION_ID = ?"));
	}

	@Test
	public void testHistory_Instance() {
		Organization org = createOrganizationC();
		org.setName("name 2");

		logAllResources();
		logAllForcedIds();

		myOrganizationDao.update(org);

		myCaptureQueriesListener.clear();
		IBundleProvider outcome = myOrganizationDao.history(new IdType("Organization/C"), null, null, null, mySrd);
		assertEquals(2, outcome.size());
		myCaptureQueriesListener.logSelectQueries();
		assertEquals(3, myCaptureQueriesListener.getSelectQueries().size());
		assertThat(myCaptureQueriesListener.getSelectQueries().get(0).getSql(false, false), containsString("PARTITION_ID in "));
		assertThat(myCaptureQueriesListener.getSelectQueries().get(1).getSql(false, false), containsString("PARTITION_ID="));
	}


	@Test
	public void testTransaction_NoRequestDetails() throws IOException {
		Bundle input = loadResourceFromClasspath(Bundle.class, "/r4/load_bundle.json");

		// Maybe in the future we'll make request details mandatory and if that
		// causes this to fail that's ok
		Bundle outcome = mySystemDao.transaction(null, input);

		ListMultimap<String, String> resourceIds = outcome
			.getEntry()
			.stream()
			.collect(MultimapCollector.toMultimap(t -> new IdType(t.getResponse().getLocation()).toUnqualifiedVersionless().getResourceType(), t -> new IdType(t.getResponse().getLocation()).toUnqualifiedVersionless().getValue()));

		logAllResources();
		Multimap<String, Integer> resourcesByType = runInTransaction(() -> {
			return myResourceTableDao.findAll().stream().collect(MultimapCollector.toMultimap(t -> t.getResourceType(), t -> t.getPartitionId().getPartitionId()));
		});

		assertThat(resourcesByType.get("Patient"), contains(4267));
		assertThat(resourcesByType.get("ExplanationOfBenefit"), contains(4267));
		assertThat(resourcesByType.get("Coverage"), contains(4267));
		assertThat(resourcesByType.get("Organization"), contains(-1, -1));
		assertThat(resourcesByType.get("Practitioner"), contains(-1, -1, -1));
	}

	@Test
	public void testTransaction_SystemRequestDetails() throws IOException {
		Bundle input = loadResourceFromClasspath(Bundle.class, "/r4/load_bundle.json");
		myCaptureQueriesListener.clear();
		Bundle outcome = mySystemDao.transaction(new SystemRequestDetails(), input);
		myCaptureQueriesListener.logSelectQueries();
		List<String> selectQueryStrings = myCaptureQueriesListener
			.getSelectQueries()
			.stream()
			.map(t -> t.getSql(false, false).toUpperCase(Locale.US))
			.filter(t -> !t.contains("FROM HFJ_TAG_DEF"))
			.collect(Collectors.toList());
		for (String next : selectQueryStrings) {
			assertThat(next, either(containsString("PARTITION_ID =")).or(containsString("PARTITION_ID IN")));
		}

		ListMultimap<String, String> resourceIds = outcome
			.getEntry()
			.stream()
			.collect(MultimapCollector.toMultimap(t -> new IdType(t.getResponse().getLocation()).toUnqualifiedVersionless().getResourceType(), t -> new IdType(t.getResponse().getLocation()).toUnqualifiedVersionless().getValue()));

		String patientId = resourceIds.get("Patient").get(0);

		logAllResources();
		Multimap<String, Integer> resourcesByType = runInTransaction(() -> {
			return myResourceTableDao.findAll().stream().collect(MultimapCollector.toMultimap(t -> t.getResourceType(), t -> t.getPartitionId().getPartitionId()));
		});

		assertThat(resourcesByType.get("Patient"), contains(4267));
		assertThat(resourcesByType.get("ExplanationOfBenefit"), contains(4267));
		assertThat(resourcesByType.get("Coverage"), contains(4267));
		assertThat(resourcesByType.get("Organization"), contains(-1, -1));
		assertThat(resourcesByType.get("Practitioner"), contains(-1, -1, -1));

		// Try Searching
		SearchParameterMap map = new SearchParameterMap();
		map.add(ExplanationOfBenefit.SP_PATIENT, new ReferenceParam(patientId));
		map.addInclude(new Include("*"));
		myCaptureQueriesListener.clear();
		IBundleProvider result = myExplanationOfBenefitDao.search(map);
		List<String> resultIds = toUnqualifiedVersionlessIdValues(result);
		assertThat(resultIds.toString(), resultIds, containsInAnyOrder(
			resourceIds.get("Coverage").get(0),
			resourceIds.get("Organization").get(0),
			resourceIds.get("ExplanationOfBenefit").get(0),
			resourceIds.get("Patient").get(0),
			resourceIds.get("Practitioner").get(0),
			resourceIds.get("Practitioner").get(1),
			resourceIds.get("Practitioner").get(2)
		));

		myCaptureQueriesListener.logSelectQueries();

		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
		assertThat(selectQueries.get(0).getSql(true, false).toUpperCase(Locale.US), matchesPattern("SELECT.*FROM HFJ_RES_LINK.*WHERE.*PARTITION_ID = '4267'.*"));

	}


	@Test
	public void testSearch() throws IOException {
		Bundle input = loadResourceFromClasspath(Bundle.class, "/r4/load_bundle.json");
		Bundle outcome = mySystemDao.transaction(new SystemRequestDetails(), input);

		ListMultimap<String, String> resourceIds = outcome
			.getEntry()
			.stream()
			.collect(MultimapCollector.toMultimap(t -> new IdType(t.getResponse().getLocation()).toUnqualifiedVersionless().getResourceType(), t -> new IdType(t.getResponse().getLocation()).toUnqualifiedVersionless().getValue()));

		String patientId = resourceIds.get("Patient").get(0);

		logAllResources();
		Multimap<String, Integer> resourcesByType = runInTransaction(() -> {
			return myResourceTableDao.findAll().stream().collect(MultimapCollector.toMultimap(t -> t.getResourceType(), t -> t.getPartitionId().getPartitionId()));
		});

		assertThat(resourcesByType.get("Patient"), contains(4267));
		assertThat(resourcesByType.get("ExplanationOfBenefit"), contains(4267));
		assertThat(resourcesByType.get("Coverage"), contains(4267));
		assertThat(resourcesByType.get("Organization"), contains(-1, -1));
		assertThat(resourcesByType.get("Practitioner"), contains(-1, -1, -1));

		// Try Searching
		SearchParameterMap map = new SearchParameterMap();
		map.add(ExplanationOfBenefit.SP_PATIENT, new ReferenceParam(patientId));
		map.addInclude(new Include("*"));
		myCaptureQueriesListener.clear();
		IBundleProvider result = myExplanationOfBenefitDao.search(map);
		List<String> resultIds = toUnqualifiedVersionlessIdValues(result);
		assertThat(resultIds.toString(), resultIds, containsInAnyOrder(
			resourceIds.get("Coverage").get(0),
			resourceIds.get("Organization").get(0),
			resourceIds.get("ExplanationOfBenefit").get(0),
			resourceIds.get("Patient").get(0),
			resourceIds.get("Practitioner").get(0),
			resourceIds.get("Practitioner").get(1),
			resourceIds.get("Practitioner").get(2)
		));

		myCaptureQueriesListener.logSelectQueries();

		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
		assertThat(selectQueries.get(0).getSql(true, false).toUpperCase(Locale.US), matchesPattern("SELECT.*FROM HFJ_RES_LINK.*WHERE.*PARTITION_ID = '4267'.*"));

	}


	@Test
	public void testHistory_Type() {
		myOrganizationDao.history(null, null, null, mySrd);
	}

	@Test
	public void testHistory_System() {
		mySystemDao.history(null, null, null, mySrd);
	}

	private Organization createOrganizationC() {
		Organization org = new Organization();
		org.setId("C");
		org.setName("Foo");
		myOrganizationDao.update(org);
		return org;
	}

	private void createObservationB() {
		Observation obs = new Observation();
		obs.setId("B");
		obs.getSubject().setReference("Patient/A");
		myObservationDao.update(obs);
	}

	private Patient createPatientA() {
		Patient patient = new Patient();
		patient.setId("Patient/A");
		patient.setActive(true);
		DaoMethodOutcome update = myPatientDao.update(patient);
		return (Patient)update.getResource();
	}

}
