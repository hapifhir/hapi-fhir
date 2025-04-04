package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.BaseResourceIndexedSearchParam;
import ca.uhn.fhir.jpa.model.entity.IndexedSearchParamIdentity;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.sp.SearchParamIdentityCacheSvcImpl;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.QuantityParam;
import ca.uhn.fhir.rest.param.SpecialParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import ca.uhn.fhir.rest.param.UriParamQualifierEnum;
import org.hl7.fhir.r4.model.DateType;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.RiskAssessment;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * This test verifies that IndexedSearchParamIdentities are created after reading from or writing to the HFJ_SPIDX_* tables.
 */
class FhirResourceDaoR4SearchParamIdentityTest extends BaseJpaR4Test {

	@AfterEach
	void cleanUp() {
		myStorageSettings.setIncludeHashIdentityForTokenSearches(false);
		myStorageSettings.setFilterParameterEnabled(new JpaStorageSettings().isFilterParameterEnabled());
		myStorageSettings.setIndexMissingFields(JpaStorageSettings.IndexEnabledEnum.DISABLED);
		myStorageSettings.setIndexStorageOptimized(false);
		mySearchParamRegistry.setPopulateSearchParamIdentities(false);
	}

	@Test
	void createSearchParamIdentity_createPatientInTransaction_searchParamIdentityCreatedAfterCommit() {
		runInTransaction(() -> {
			// setup
			Patient p = new Patient();
			p.setBirthDateElement(new DateType("2021-02-22"));

			// execute
			myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

			waitOneSecond();

			// IndexedSearchParamIdentity should not be created before transaction commit
			assertEquals(0, myCaptureQueriesListener.getInsertQueries().stream()
				.filter(q -> q.getSql(false, false).contains("insert into HFJ_SPIDX_IDENTITY")).count());
		});

		// verify
		verifySearchParamIdentity(2, "Patient", Patient.SP_BIRTHDATE);
	}

	private void waitOneSecond() {
		await().atLeast(1, TimeUnit.SECONDS).pollInterval(1, TimeUnit.SECONDS).until(() -> true);
	}

	@Test
	void createSearchParamIdentity_createPatient_searchParamIdentityCreated() {
		// setup
		Patient p = new Patient();
		p.setBirthDateElement(new DateType("2021-02-22"));

		// execute
		myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();

		// verify
		verifySearchParamIdentity(2, "Patient", Patient.SP_BIRTHDATE);
	}

	@Test
	void createSearchParamIdentity_withSetIncludeHashIdentityForTokenSearches_searchParamIdentityCreated() {
		// setup
		myStorageSettings.setIncludeHashIdentityForTokenSearches(true);

		SearchParameterMap map = new SearchParameterMap();
		map.add(Patient.SP_ACTIVE, new TokenParam("true"));

		// execute
		myPatientDao.search(map, mySrd);

		// verify
		verifySearchParamIdentity(1, "Patient", Patient.SP_ACTIVE);
	}


	@Test
	void createSearchParamIdentity_stringFilterSearch_searchParamIdentityCreated() {
		// setup
		myStorageSettings.setFilterParameterEnabled(true);
		SearchParameterMap map = new SearchParameterMap();
		map.add(Constants.PARAM_FILTER, new StringParam("name co smi"));

		myPatientDao.search(map, mySrd);

		verifySearchParamIdentity(1, "Patient", "name");
	}

	@ParameterizedTest
	@CsvSource({
		"DISABLED, false",
		"ENABLED , true"
	})
	void createSearchParamIdentity_missingSearch_searchParamIdentityCreated(
		StorageSettings.IndexEnabledEnum theIndexEnabled,
		boolean theIndexStorageOptimized) {
		// setup
		myStorageSettings.setIndexMissingFields(theIndexEnabled);
		myStorageSettings.setIndexStorageOptimized(theIndexStorageOptimized);

		SearchParameterMap map = new SearchParameterMap();
		QuantityParam param = new QuantityParam();
		param.setMissing(false);
		map.add(Observation.SP_VALUE_QUANTITY, param);

		myObservationDao.search(map, mySrd);

		verifySearchParamIdentity(1, "Observation", Observation.SP_VALUE_QUANTITY);
	}

	@Test
	void createSearchParamIdentity_uriBelowSearch_searchParamIdentityCreated() {
		// setup
		SearchParameterMap map = new SearchParameterMap();
		UriParam param = new UriParam();
		param.setQualifier(UriParamQualifierEnum.BELOW);
		param.setValue("http://vs");
		map.add(ValueSet.SP_URL, param);

		myValueSetDao.search(map, mySrd);

		verifySearchParamIdentity(1, "ValueSet", ValueSet.SP_URL);
	}

	@Test
	void createSearchParamIdentity_coordsSearch_searchParamIdentityCreated() {
		SearchParameterMap map = new SearchParameterMap();
		SpecialParam param = new SpecialParam();
		param.setValue("43.7|79.4");
		map.add(Location.SP_NEAR, param);

		myLocationDao.search(map, mySrd);

		verifySearchParamIdentity(1, "Location", Location.SP_NEAR);
	}

	@Test
	void createSearchParamIdentity_dateSearch_searchParamIdentityCreated() {
		SearchParameterMap map = new SearchParameterMap();
		DateParam param = new DateParam("2021-02-22");
		map.add(Patient.SP_BIRTHDATE, param);

		myPatientDao.search(map, mySrd);

		verifySearchParamIdentity(1, "Patient", Patient.SP_BIRTHDATE);
	}

	@Test
	void createSearchParamIdentity_numberSearch_searchParamIdentityCreated() {
		SearchParameterMap map = new SearchParameterMap();
		NumberParam param = new NumberParam(15);
		map.add(RiskAssessment.SP_PROBABILITY, param);

		myRiskAssessmentDao.search(map, mySrd);

		verifySearchParamIdentity(1, "RiskAssessment", RiskAssessment.SP_PROBABILITY);
	}

	@Test
	void createSearchParamIdentity_quantitySearch_searchParamIdentityCreated() {
		SearchParameterMap map = new SearchParameterMap();
		QuantityParam param = new QuantityParam(123);
		map.add(Observation.SP_VALUE_QUANTITY, param);

		myObservationDao.search(map, mySrd);

		verifySearchParamIdentity(1, "Observation", Observation.SP_VALUE_QUANTITY);
	}

	@Test
	void createSearchParamIdentity_tokenSortSearch_searchParamIdentityCreated() {
		SearchParameterMap map = new SearchParameterMap();
		map.setSort(new SortSpec("gender"));

		myPatientDao.search(map, mySrd);

		verifySearchParamIdentity(1, "Patient", Patient.SP_GENDER);
	}

	@Test
	void createSearchParamIdentity_withSpRegistryInit_searchParamIdentitiesCreated() {
		mySearchParamRegistry.setPopulateSearchParamIdentities(true);

		mySearchParamRegistry.forceRefresh();

		// verify
		int expectedSpIdentities = mySearchParamRegistry.getHashIdentityToIndexedSearchParamMap().size();
		await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> runInTransaction(() ->
			assertEquals(expectedSpIdentities, myResourceIndexedSearchParamIdentityDao.getAllHashIdentities().size())));
	}

	private void verifySearchParamIdentity(int theSearchParamIdentityCount, String theResourceType,
										   String theSearchParamName) {
		await().atMost(30, TimeUnit.SECONDS).untilAsserted(() -> {
			assertEquals(theSearchParamIdentityCount, myCaptureQueriesListener.getInsertQueries().stream()
				.filter(q -> q.getSql(false, false).contains("insert into HFJ_SPIDX_IDENTITY")).count());

			long hashIdentity = BaseResourceIndexedSearchParam.calculateHashIdentity(new PartitionSettings(),
				RequestPartitionId.defaultPartition(), theResourceType, theSearchParamName);

			validateIndexedSearchParamIdentity(hashIdentity, theResourceType, theSearchParamName);

			assertNotNull(SearchParamIdentityCacheSvcImpl.CacheUtils
				.getSearchParamIdentityFromCache(myMemoryCacheService, hashIdentity));
		});
	}

	private void validateIndexedSearchParamIdentity(long theHashIdentity, String theResourceType,
													String theSearchParamName) {
		runInTransaction(() -> {
			IndexedSearchParamIdentity indexedSearchParamIdentity =
				myResourceIndexedSearchParamIdentityDao.getSearchParameterIdByHashIdentity(theHashIdentity);
			assertNotNull(indexedSearchParamIdentity);
			assertEquals(theResourceType, indexedSearchParamIdentity.getResourceType());
			assertEquals(theSearchParamName, indexedSearchParamIdentity.getParamName());
		});
	}
}
