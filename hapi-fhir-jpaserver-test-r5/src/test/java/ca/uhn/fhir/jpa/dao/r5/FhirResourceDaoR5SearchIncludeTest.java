package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.search.PersistedJpaSearchFirstPageBundleProvider;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ca.uhn.fhir.util.BundleBuilder;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.BodyStructure;
import org.hl7.fhir.r5.model.CarePlan;
import org.hl7.fhir.r5.model.Encounter;
import org.hl7.fhir.r5.model.Enumerations;
import org.hl7.fhir.r5.model.EpisodeOfCare;
import org.hl7.fhir.r5.model.Observation;
import org.hl7.fhir.r5.model.Organization;
import org.hl7.fhir.r5.model.Patient;
import org.hl7.fhir.r5.model.Procedure;
import org.hl7.fhir.r5.model.Questionnaire;
import org.hl7.fhir.r5.model.QuestionnaireResponse;
import org.hl7.fhir.r5.model.Reference;
import org.hl7.fhir.r5.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SuppressWarnings({"Duplicates"})
public class FhirResourceDaoR5SearchIncludeTest extends BaseJpaR5Test {

	@Mock
	private IAnonymousInterceptor myAnonymousInterceptor;
	@Captor
	private ArgumentCaptor<HookParams> myParamsCaptor;

	@AfterEach
	public void afterEach() {
		myStorageSettings.setMaximumIncludesToLoadPerPage(JpaStorageSettings.DEFAULT_MAXIMUM_INCLUDES_TO_LOAD_PER_PAGE);
		myInterceptorRegistry.unregisterInterceptor(myAnonymousInterceptor);
	}

	@ParameterizedTest
	@CsvSource({
		// theQuestionnaireRespId,    theReverse, theMatchAll
		"QuestionnaireResponse/qr   , false,      false",
		"QuestionnaireResponse/qr2  , false,      false",
		"QuestionnaireResponse/qr   , true,       false",
		"QuestionnaireResponse/qr2  , true,       false",
		"QuestionnaireResponse/qr   , false,      true",
		"QuestionnaireResponse/qr2  , false,      true",
//		"QuestionnaireResponse/qr   , true,       true", // Not yet supported
//		"QuestionnaireResponse/qr2  , true,       true", // Not yet supported
	})
	public void testIncludeCanonicalReference(String theQuestionnaireRespId, boolean theReverse, boolean theMatchAll) {
		Questionnaire qWrongVersion = new Questionnaire();
		qWrongVersion.setId("qWrongVersion");
		qWrongVersion.setUrl("http://foo");
		qWrongVersion.setVersion("99.0");
		myQuestionnaireDao.update(qWrongVersion, mySrd);

		Questionnaire q = new Questionnaire();
		q.setId("q");
		q.setUrl("http://foo");
		q.setVersion("1.0");
		myQuestionnaireDao.update(q, mySrd);

		if (theQuestionnaireRespId.equals("QuestionnaireResponse/qr")) {
			QuestionnaireResponse qr = new QuestionnaireResponse();
			qr.setId("qr");
			qr.setQuestionnaire("http://foo");
			myQuestionnaireResponseDao.update(qr, mySrd);
		} else {
			QuestionnaireResponse qr2 = new QuestionnaireResponse();
			qr2.setId("qr2");
			qr2.setQuestionnaire("http://foo|1.0");
			myQuestionnaireResponseDao.update(qr2, mySrd);
		}

		logAllUriIndexes();
		logAllResourceLinks();

		// Create a QR and Q that have other URLs and shouldn't be turned up in searches here
		Questionnaire qIrrelevant = new Questionnaire();
		qIrrelevant.setId("qIrrelevant");
		qIrrelevant.setUrl("http://fooIrrelevant");
		qIrrelevant.setVersion("1.0");
		myQuestionnaireDao.update(qIrrelevant, mySrd);

		QuestionnaireResponse qrIrrelevant = new QuestionnaireResponse();
		qrIrrelevant.setId("qrIrrelevant");
		qrIrrelevant.setQuestionnaire("http://fooIrrelevant");
		myQuestionnaireResponseDao.update(qrIrrelevant, mySrd);

		IBundleProvider outcome;
		IFhirResourceDao<?> dao;
		SearchParameterMap map;
		String expectWarning = null;
		if (theReverse) {
			map = new SearchParameterMap();
			map.add("_id", new TokenParam("Questionnaire/q"));
			if (theMatchAll) {
				map.addRevInclude(IBaseResource.INCLUDE_ALL);
			} else {
				map.addRevInclude(QuestionnaireResponse.INCLUDE_QUESTIONNAIRE);
			}
			dao = myQuestionnaireDao;
		} else {
			map = new SearchParameterMap();
			map.add("_id", new TokenParam(theQuestionnaireRespId));
			if (theMatchAll) {
				map.addInclude(IBaseResource.INCLUDE_ALL);
			} else {
				map.addInclude(QuestionnaireResponse.INCLUDE_QUESTIONNAIRE);
			}
			dao = myQuestionnaireResponseDao;
		}

		if (theMatchAll) {
			expectWarning = "Search with _include=* can be inefficient";
		}

		myCaptureQueriesListener.clear();
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_WARNING, myAnonymousInterceptor);

		map.setLoadSynchronous(true);
		outcome = dao.search(map, mySrd);
		List<String> outcomeValues = toUnqualifiedVersionlessIdValues(outcome);
		myCaptureQueriesListener.logSelectQueries();

		if (theReverse) {
			assertThat(outcomeValues).as(outcomeValues.toString()).containsExactlyInAnyOrder(
				theQuestionnaireRespId, "Questionnaire/q"
			);
		} else {
			assertThat(outcomeValues).as(outcomeValues.toString()).containsExactlyInAnyOrder(
				theQuestionnaireRespId, "Questionnaire/q", "Questionnaire/qWrongVersion"
			);
		}

		if (expectWarning == null) {
			verify(myAnonymousInterceptor, never()).invoke(eq(Pointcut.JPA_PERFTRACE_WARNING), myParamsCaptor.capture());
		} else {
			verify(myAnonymousInterceptor, times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_WARNING), myParamsCaptor.capture());
			HookParams params = myParamsCaptor.getValue();
			assertThat(params.get(StorageProcessingMessage.class).getMessage()).contains(expectWarning);
		}

		if (!theReverse && theMatchAll) {
			myCaptureQueriesListener.logSelectQueries();
			SqlQuery searchForCanonicalReferencesQuery = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(2);
			// Make sure we have the right query - If this ever fails, maybe we have optimized the queries
			// (or somehow made things worse) and the search for the canonical target is no longer the 4th
			// SQL query
			assertThat(searchForCanonicalReferencesQuery.getSql(true, false)).contains("rispu1_0.HASH_IDENTITY in ('-600769180185160063')");
			assertTrue(
				searchForCanonicalReferencesQuery.getSql(true, false).contains("rispu1_0.SP_URI in ('http://foo')")
					|| searchForCanonicalReferencesQuery.getSql(true, false).contains("rispu1_0.SP_URI in ('http://foo','http://foo|1.0')"),
				searchForCanonicalReferencesQuery.getSql(true, false)
			);
		}

	}


	/**
	 * The "encounter" search parameter is used by a number of resource types other than just
	 * QuestionnaireResponse, but when we _include it, we should only add the hash_identity
	 * relevant to QuestionnaireResponse to the select statement.
	 */
	@Test
	public void testIncludeMultiTypeSearchParameter() {
		// Setup
		Encounter enc = new Encounter();
		enc.setId("Encounter/enc");
		myEncounterDao.update(enc, mySrd);

		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setId("qr");
		qr.setEncounter(new Reference("Encounter/enc"));
		myQuestionnaireResponseDao.update(qr, mySrd);

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap
			.newSynchronous()
			.addInclude(QuestionnaireResponse.INCLUDE_ENCOUNTER);
		IBundleProvider outcome = myQuestionnaireResponseDao.search(map, mySrd);
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactly("QuestionnaireResponse/qr", "Encounter/enc");

		// Verify
		myCaptureQueriesListener.logSelectQueries();
		SqlQuery searchForCanonicalReferencesQuery = myCaptureQueriesListener.getSelectQueries().get(1);
		assertThat(searchForCanonicalReferencesQuery.getSql(true, false)).contains("r.target_resource_type = 'Encounter'");
		assertThat(searchForCanonicalReferencesQuery.getSql(true, false)).doesNotContainIgnoringCase("union");
	}


	@Test
	public void testIncludesNotAppliedToIncludedResources() {
		createOrganizationWithReferencingEpisodesOfCare(10);

		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add("_id", new TokenParam("EOC-0"))
			.addInclude(new Include("*"))
			.addRevInclude(new Include("*").setRecurse(true));
		IBundleProvider results = myEpisodeOfCareDao.search(map, mySrd);
		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).as(ids.toString()).containsExactlyInAnyOrder("EpisodeOfCare/EOC-0", "Organization/ORG-0");
	}

	@Test
	public void testRevIncludesPaged_SyncSearchWithCount() {
		createOrganizationWithReferencingEpisodesOfCare(10);

		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.setCount(10)
			.addInclude(new Include("*"))
			.addRevInclude(new Include("*").setRecurse(true));
		IBundleProvider results = myOrganizationDao.search(map, mySrd);
		List<String> ids = toUnqualifiedVersionlessIdValues(results);

		List<String> expected = IntStream.range(0, 10)
			.mapToObj(t -> "EpisodeOfCare/EOC-" + t)
			.collect(Collectors.toList());
		expected.add("Organization/ORG-0");
		expected.add("Organization/ORG-P");

		assertThat(ids)
			.as("Check ids list contains all expected elements in any order")
			.containsExactlyInAnyOrderElementsOf(expected);
	}

	@Test
	public void testRevIncludesPaged_SyncSearchWithoutCount() {
		createOrganizationWithReferencingEpisodesOfCare(10);
		myStorageSettings.setMaximumIncludesToLoadPerPage(5);

		logAllResourceLinks();

		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add("_id", new TokenParam("ORG-0"))
			.addInclude(new Include("*"))
			.addRevInclude(new Include("*").setRecurse(true));
		IBundleProvider results = myOrganizationDao.search(map, mySrd);
		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).as(ids.toString()).containsExactlyInAnyOrder("EpisodeOfCare/EOC-0", "EpisodeOfCare/EOC-1", "EpisodeOfCare/EOC-2", "EpisodeOfCare/EOC-3", "EpisodeOfCare/EOC-4", "Organization/ORG-0");
	}

	@Test
	public void testSearchWithIncludeSpecDoesNotCauseNPE() {
		createPatientWithReferencingCarePlan(1);

		// First verify it with the "." syntax
		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.addInclude(new Include("CarePlan.patient"));
		try {
			myCarePlanDao.search(map, mySrd);
			fail();
		} catch (Exception e) {
			// good
		}

		// Next verify it with the ":" syntax
		SearchParameterMap map2 = SearchParameterMap.newSynchronous()
			.addInclude(new Include("CarePlan:patient"));
		try {
			IBundleProvider results = myCarePlanDao.search(map2, mySrd);
			List<String> ids = toUnqualifiedVersionlessIdValues(results);
			assertThat(ids).as(ids.toString()).containsExactlyInAnyOrder("CarePlan/CP-1", "Patient/PAT-1");
		} catch (Exception e) {
			fail();
		}
	}


	@Test
	public void testRevIncludeOnIncludedResource() {
		SearchParameter sp = new SearchParameter();
		sp.addBase(Enumerations.VersionIndependentResourceTypesAll.PROCEDURE);
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setCode("focalAccess");
		sp.setType(Enumerations.SearchParamType.REFERENCE);
		sp.setExpression("Procedure.extension('http://fkcfhir.org/fhir/cs/CS1MachNumber')");
		sp.addTarget(Enumerations.VersionIndependentResourceTypesAll.BODYSTRUCTURE);
		mySearchParameterDao.create(sp, mySrd);
		mySearchParamRegistry.forceRefresh();

		BodyStructure bs = new BodyStructure();
		bs.setId("B51936689");
		bs.setDescription("Foo");
		myBodyStructureDao.update(bs, mySrd);

		Procedure p = new Procedure();
		p.setId("PRA8780542726");
		p.setStatus(Enumerations.EventStatus.COMPLETED);
		myProcedureDao.update(p, mySrd);

		p = new Procedure();
		p.setId("PRA8780542785");
		p.addPartOf().setReference("Procedure/PRA8780542726");
		p.setStatus(Enumerations.EventStatus.COMPLETED);
		p.addExtension("http://fkcfhir.org/fhir/cs/CS1MachNumber", new Reference("BodyStructure/B51936689"));
		myProcedureDao.update(p, mySrd);

		logAllResources();
		logAllResourceLinks();

		// Non-synchronous
		SearchParameterMap map = new SearchParameterMap();
		map.add("_id", new TokenParam("PRA8780542726"));
		map.addRevInclude(new Include("Procedure:part-of"));
		map.addInclude(new Include("Procedure:focalAccess").asRecursive());
		IBundleProvider outcome = myProcedureDao.search(map, mySrd);
		assertEquals(PersistedJpaSearchFirstPageBundleProvider.class, outcome.getClass());
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(ids).as(ids.toString()).containsExactlyInAnyOrder("Procedure/PRA8780542726", "Procedure/PRA8780542785", "BodyStructure/B51936689");

		// Synchronous
		map = new SearchParameterMap().setLoadSynchronous(true);
		map.add("_id", new TokenParam("PRA8780542726"));
		map.addRevInclude(new Include("Procedure:part-of"));
		map.addInclude(new Include("Procedure:focalAccess").asRecursive());
		outcome = myProcedureDao.search(map, mySrd);
		assertEquals(SimpleBundleProvider.class, outcome.getClass());
		ids = toUnqualifiedVersionlessIdValues(outcome);
		assertThat(ids).as(ids.toString()).containsExactlyInAnyOrder("Procedure/PRA8780542726", "Procedure/PRA8780542785", "BodyStructure/B51936689");
	}


	@Test
	public void testRevIncludesPaged_AsyncSearch() {
		int eocCount = 10;
		myStorageSettings.setMaximumIncludesToLoadPerPage(5);

		createOrganizationWithReferencingEpisodesOfCare(eocCount);

		SearchParameterMap map = new SearchParameterMap()
			.setCount(10)
			.addInclude(new Include("*"))
			.addRevInclude(new Include("*").setRecurse(true));
		IBundleProvider results = myOrganizationDao.search(map, mySrd);
		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		assertThat(ids).as(ids.toString()).containsExactlyInAnyOrder("EpisodeOfCare/EOC-0", "EpisodeOfCare/EOC-1", "EpisodeOfCare/EOC-2", "EpisodeOfCare/EOC-3", "Organization/ORG-0", "Organization/ORG-P");

	}

	@Test
	public void testRevIncludesPagedSyncSearch() {
		int eocCount = 10;

		createOrganizationWithReferencingEpisodesOfCare(eocCount);

		SearchParameterMap map = new SearchParameterMap()
			.add("_id", new TokenParam("ORG-0"))
			.addRevInclude(EpisodeOfCare.INCLUDE_ORGANIZATION);
		myCaptureQueriesListener.clear();
		IBundleProvider results = myOrganizationDao.search(map, mySrd);
		List<String> ids = toUnqualifiedVersionlessIdValues(results);
		myCaptureQueriesListener.logSelectQueries();
		assertThat(ids).as(ids.toString()).containsExactlyInAnyOrder("EpisodeOfCare/EOC-0", "EpisodeOfCare/EOC-1", "EpisodeOfCare/EOC-2", "EpisodeOfCare/EOC-3", "EpisodeOfCare/EOC-4", "EpisodeOfCare/EOC-5", "EpisodeOfCare/EOC-6", "EpisodeOfCare/EOC-7", "EpisodeOfCare/EOC-8", "EpisodeOfCare/EOC-9", "Organization/ORG-0");
	}

	private void createOrganizationWithReferencingEpisodesOfCare(int theEocCount) {
		Organization org = new Organization();
		org.setId("Organization/ORG-P");
		org.setName("ORG-P");
		myOrganizationDao.update(org, mySrd);

		org = new Organization();
		org.setId("Organization/ORG-0");
		org.setName("ORG-0");
		org.setPartOf(new Reference("Organization/ORG-P"));
		myOrganizationDao.update(org, mySrd);

		for (int i = 0; i < theEocCount; i++) {
			EpisodeOfCare eoc = new EpisodeOfCare();
			eoc.setId("EpisodeOfCare/EOC-" + i);
			eoc.getManagingOrganization().setReference("Organization/ORG-0");
			myEpisodeOfCareDao.update(eoc, mySrd);
		}
	}

	@SuppressWarnings("SameParameterValue")
	private void createPatientWithReferencingCarePlan(int theCount) {
		org.hl7.fhir.r5.model.Patient patient = new Patient();
		patient.setId("Patient/PAT-1");
		myPatientDao.update(patient, mySrd);

		for (int i = 1; i <= theCount; i++) {
			CarePlan carePlan = new CarePlan();
			carePlan.setId("CarePlan/CP-" + i);
			carePlan.getSubject().setReference("Patient/PAT-1");
			myCarePlanDao.update(carePlan, mySrd);
		}
	}

	@Test
	void testIncludeLimits() {
		// Setup - Create a resource with a lot of forward includes
		BundleBuilder bb = new BundleBuilder(myFhirContext);

		Observation obs = new Observation();
		obs.setId("Observation/root");
		bb.addTransactionUpdateEntry(obs);

		int referenceCount = 100;
		for (int i = 0; i < referenceCount; i++) {
			Observation child = new Observation();
			child.setId("Observation/child-" + i);
			obs.addHasMember().setReference(child.getId());
			bb.addTransactionUpdateEntry(child);
		}
		mySystemDao.transaction(new SystemRequestDetails(), bb.getBundleTyped());

		// Test
		SearchParameterMap params = new SearchParameterMap();
		params.add("_id", new TokenParam("Observation/root"));
		params.addInclude(IBaseResource.INCLUDE_ALL);
		IBundleProvider results = myObservationDao.search(params, mySrd);

		// Verify
		assertEquals(referenceCount + 1, results.getResources(0, 100000).size());

	}


	/**
	 * <a href="https://github.com/hapifhir/hapi-fhir/issues/4896">#4896</a>
	 */
	@SuppressWarnings("DataFlowIssue")
	@Test
	void testLastUpdatedDoesNotApplyToForwardOrRevIncludes() {
		// given
		Instant now = Instant.now();
		IIdType org = createOrganization();
		IIdType patId = createPatient(withReference("managingOrganization", org));
		IIdType groupId = createGroup(withGroupMember(patId));
		IIdType careTeam = createResource("CareTeam", withSubject(patId));

		// backdate the Group and CareTeam
		int updatedCount = new TransactionTemplate(myTxManager).execute((status) ->
			myEntityManager
				.createQuery("update ResourceTable set myUpdated = :new_updated where myPid.myId in (:target_ids)")
				.setParameter("new_updated", Date.from(now.minus(1, ChronoUnit.HOURS)))
				.setParameter("target_ids", List.of(groupId.getIdPartAsLong(), careTeam.getIdPartAsLong(), org.getIdPartAsLong()))
				.executeUpdate());
		assertThat(updatedCount).as("backdated the Organization, CareTeam and Group").isEqualTo(3);


		// when
		// "Patient?_lastUpdated=gt2023-01-01&_revinclude=Group:member&_revinclude=CareTeam:subject&_include=Patient:organization");
		SearchParameterMap map = new SearchParameterMap();
		map.setLastUpdated(new DateRangeParam(new DateParam(ParamPrefixEnum.GREATERTHAN_OR_EQUALS, java.util.Date.from(now))));
		map.addInclude(new Include("Patient:organization"));
		map.addRevInclude(new Include("Group:member"));
		map.addRevInclude(new Include("CareTeam:subject"));

		IBundleProvider outcome = myPatientDao.search(map, mySrd);
		List<String> ids = toUnqualifiedVersionlessIdValues(outcome);


		// then
		assertThat(ids).containsExactlyInAnyOrder(patId.getValue(), groupId.getValue(), careTeam.getValue(), org.getValue());
	}

}
