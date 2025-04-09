package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static ca.uhn.fhir.interceptor.api.Pointcut.STORAGE_PARTITION_IDENTIFY_ANY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FhirResourceDaoR4SearchSqlTest extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4SearchSqlTest.class);

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSettings.setAdvancedHSearchIndexing(false);

		myInterceptorRegistry.registerInterceptor(new MyPartitionInterceptor());
	}

	@AfterEach
	public void after() {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.DEFAULT_TAG_STORAGE_MODE);
		myPartitionSettings.setDefaultPartitionId(new PartitionSettings().getDefaultPartitionId());
		myPartitionSettings.setDatabasePartitionMode(new PartitionSettings().isDatabasePartitionMode());

		myInterceptorRegistry.unregisterInterceptorsIf(t->t instanceof MyPartitionInterceptor);
	}

	record SqlGenerationTestCase(String comment, String restQuery, String expectedSql, String expectedPartitionedSql) {
		@Override
		public String toString() {
			return comment;
		}
	}

	static List<SqlGenerationTestCase> sqlGenerationTestCases() {
		return List.of(
			new SqlGenerationTestCase(
				"single string - no hfj_resource root",
				"Patient?name=FOO",
				"SELECT t0.RES_ID FROM HFJ_SPIDX_STRING t0 WHERE ((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?)) fetch first ? rows only",
				"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_SPIDX_STRING t0 WHERE ((t0.PARTITION_ID = ?) AND ((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?))) fetch first ? rows only"
			)
			, new SqlGenerationTestCase(
				"two regular params - should use hfj_resource as root",
				"Patient?name=smith&active=true",
				"SELECT t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_SPIDX_STRING t0 ON (t1.RES_ID = t0.RES_ID) INNER JOIN HFJ_SPIDX_TOKEN t2 ON (t1.RES_ID = t2.RES_ID) WHERE (((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?)) AND (t2.HASH_VALUE = ?)) fetch first ? rows only",
				"SELECT t1.PARTITION_ID,t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_SPIDX_STRING t0 ON ((t1.PARTITION_ID = t0.PARTITION_ID) AND (t1.RES_ID = t0.RES_ID)) INNER JOIN HFJ_SPIDX_TOKEN t2 ON ((t1.PARTITION_ID = t2.PARTITION_ID) AND (t1.RES_ID = t2.RES_ID)) WHERE (((t0.PARTITION_ID = ?) AND ((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?))) AND ((t2.PARTITION_ID = ?) AND (t2.HASH_VALUE = ?))) fetch first ? rows only"
			)
			, new SqlGenerationTestCase(
				"token not as a NOT IN subselect",
				"Encounter?class:not=not-there",
				"SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND ((t0.RES_ID) NOT IN (SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) )) fetch first ? rows only",
				"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND ((t0.PARTITION_ID = ?) AND ((t0.PARTITION_ID,t0.RES_ID) NOT IN (SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) ))) fetch first ? rows only"
			)
			, new SqlGenerationTestCase(
				"token not on chain join - NOT IN from hfj_res_link target columns",
				"Observation?encounter.class:not=not-there",
				"SELECT t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 WHERE ((t0.SRC_PATH = ?) AND ((t0.TARGET_RESOURCE_ID) NOT IN (SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) )) fetch first ? rows only",
				"SELECT t0.PARTITION_ID,t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 WHERE ((t0.SRC_PATH = ?) AND ((t0.PARTITION_ID = ?) AND ((t0.TARGET_RES_PARTITION_ID,t0.TARGET_RESOURCE_ID) NOT IN (SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) ))) fetch first ? rows only"
			)
			, new SqlGenerationTestCase(
				"bare sort",
				"Patient?_sort=name",
				"SELECT t0.RES_ID FROM HFJ_RESOURCE t0 LEFT OUTER JOIN HFJ_SPIDX_STRING t1 ON ((t0.RES_ID = t1.RES_ID) AND (t1.HASH_IDENTITY = ?)) WHERE ((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) ORDER BY t1.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first ? rows only",
				"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 LEFT OUTER JOIN HFJ_SPIDX_STRING t1 ON ((t0.PARTITION_ID = t1.PARTITION_ID) AND (t0.RES_ID = t1.RES_ID) AND (t1.HASH_IDENTITY = ?)) WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.PARTITION_ID = ?)) ORDER BY t1.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first ? rows only"
			)
			, new SqlGenerationTestCase(
				"sort with predicate",
				"Patient?active=true&_sort=name",
				"SELECT t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_SPIDX_TOKEN t0 ON (t1.RES_ID = t0.RES_ID) LEFT OUTER JOIN HFJ_SPIDX_STRING t2 ON ((t1.RES_ID = t2.RES_ID) AND (t2.HASH_IDENTITY = ?)) WHERE (t0.HASH_VALUE = ?) ORDER BY t2.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first ? rows only",
				"SELECT t1.PARTITION_ID,t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_SPIDX_TOKEN t0 ON ((t1.PARTITION_ID = t0.PARTITION_ID) AND (t1.RES_ID = t0.RES_ID)) LEFT OUTER JOIN HFJ_SPIDX_STRING t2 ON ((t1.PARTITION_ID = t2.PARTITION_ID) AND (t1.RES_ID = t2.RES_ID) AND (t2.HASH_IDENTITY = ?)) WHERE ((t0.PARTITION_ID = ?) AND (t0.HASH_VALUE = ?)) ORDER BY t2.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first ? rows only"
			)
			, new SqlGenerationTestCase(
				"chained sort",
				"Patient?_sort=Practitioner:general-practitioner.name",
				"SELECT t0.RES_ID FROM HFJ_RESOURCE t0 LEFT OUTER JOIN HFJ_RES_LINK t1 ON ((t0.RES_ID = t1.SRC_RESOURCE_ID) AND (t1.SRC_PATH = ?)) LEFT OUTER JOIN HFJ_SPIDX_STRING t2 ON ((t1.TARGET_RESOURCE_ID = t2.RES_ID) AND (t2.HASH_IDENTITY = ?)) WHERE ((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) ORDER BY t2.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first ? rows only",
				"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 LEFT OUTER JOIN HFJ_RES_LINK t1 ON ((t0.PARTITION_ID = t1.PARTITION_ID) AND (t0.RES_ID = t1.SRC_RESOURCE_ID) AND (t1.SRC_PATH = ?)) LEFT OUTER JOIN HFJ_SPIDX_STRING t2 ON ((t1.TARGET_RES_PARTITION_ID = t2.PARTITION_ID) AND (t1.TARGET_RESOURCE_ID = t2.RES_ID) AND (t2.HASH_IDENTITY = ?)) WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t0.PARTITION_ID = ?)) ORDER BY t2.SP_VALUE_NORMALIZED ASC NULLS LAST fetch first ? rows only"
			)
		);
	}

	/**
	 * Test SQL generation with RES_ID joins.
	 */
	@ParameterizedTest(name = "[{index}] -  {0}")
	@MethodSource("sqlGenerationTestCases")
	void testSqlGeneration_DefaultNoPartitionJoin(SqlGenerationTestCase theTestCase) {
		// default config

		String sql = getSqlForRestQuery(theTestCase.restQuery);

		assertEquals(theTestCase.expectedSql, sql, theTestCase.comment);
	}

	/**
	 * Test SQL generation with joins including RES_ID, and PARTITION_ID
	 */
	@ParameterizedTest(name = "[{index}] -  {0}")
	@MethodSource("sqlGenerationTestCases")
	void testSqlGeneration_WithPartitionJoins(SqlGenerationTestCase theTestCase) {
		// include partition_id in joins
		myPartitionSettings.setDefaultPartitionId(0);
		myPartitionSettings.setDatabasePartitionMode(true);
		myPartitionSettings.setPartitioningEnabled(true);

		String sql = getSqlForRestQuery(theTestCase.restQuery);

		assertEquals(theTestCase.expectedPartitionedSql, sql, theTestCase.comment);
	}

	private String getSqlForRestQuery(String theFhirRestQuery) {
		myCaptureQueriesListener.clear();
		myTestDaoSearch.searchForIds(theFhirRestQuery);
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		return myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
	}

	/**
	 * One regular search params - Doesn't need HFJ_RESOURCE as root
	 */
	@Test
	public void testSingleRegularSearchParam() {

		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap.newSynchronous(Patient.SP_NAME, new StringParam("FOO"));
		myPatientDao.search(map);
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
		assertEquals("SELECT t0.RES_ID FROM HFJ_SPIDX_STRING t0 WHERE ((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?)) fetch first ? rows only", sql);

	}

	/**
	 * Two regular search params - Should use HFJ_RESOURCE as root
	 */
	@Test
	public void testTwoRegularSearchParams() {
		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add(Patient.SP_NAME, new StringParam("FOO"))
			.add(Patient.SP_GENDER, new TokenParam("a", "b"));
		myPatientDao.search(map);
		assertEquals(1, myCaptureQueriesListener.countSelectQueries());
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
		assertEquals("SELECT t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_SPIDX_STRING t0 ON (t1.RES_ID = t0.RES_ID) INNER JOIN HFJ_SPIDX_TOKEN t2 ON (t1.RES_ID = t2.RES_ID) WHERE (((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?)) AND (t2.HASH_SYS_AND_VALUE = ?)) fetch first ? rows only", sql);
	}

	@ParameterizedTest
	@EnumSource(value = JpaStorageSettings.TagStorageModeEnum.class, names = {"NON_VERSIONED", "VERSIONED"})
	public void testSearchByProfile_VersionedAndNonVersionedMode(JpaStorageSettings.TagStorageModeEnum theTagStorageModeEnum) {
		myStorageSettings.setTagStorageMode(theTagStorageModeEnum);

		// Put a tag in so we can search for it
		String code = "http://" + UUID.randomUUID();
		Patient p = new Patient();
		p.getMeta().addProfile(code);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		myMemoryCacheService.invalidateAllCaches();

		logAllResourceTags();
		logAllResourceHistoryTags();

		// Search
		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add(Constants.PARAM_PROFILE, new TokenParam(code));
		IBundleProvider outcome = myPatientDao.search(map, mySrd);
		assertEquals(3, myCaptureQueriesListener.logSelectQueries().size());
		// Query 1 - Find resources: Make sure we search for tag type+system+code always
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 INNER JOIN HFJ_RES_TAG t1 ON (t0.RES_ID = t1.RES_ID) INNER JOIN HFJ_TAG_DEF t2 ON (t1.TAG_ID = t2.TAG_ID) WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND ((t2.TAG_TYPE = ?) AND (t2.TAG_SYSTEM = ?) AND (t2.TAG_CODE = ?))) fetch first ? rows only", sql);
		// Query 2 - Load resource contents
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(false, false);
		assertThat(sql).contains("where (rht1_0.RES_ID) in (?)");
		// Query 3 - Load tags and definitions
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(2).getSql(false, false);
		if (theTagStorageModeEnum == JpaStorageSettings.TagStorageModeEnum.VERSIONED) {
			assertThat(sql).contains("from HFJ_HISTORY_TAG rht1_0 join HFJ_TAG_DEF");
		} else {
			assertThat(sql).contains("from HFJ_RES_TAG rt1_0 join HFJ_TAG_DEF");
		}

		assertThat(toUnqualifiedVersionlessIds(outcome)).containsExactly(id);

		List<String> profileDeclarations = outcome.getResources(0, 1).get(0).getMeta().getProfile().stream().map(t -> t.getValueAsString()).collect(Collectors.toList());
		assertThat(profileDeclarations).containsExactly(code);
	}

	@Test
	public void testSearchByProfile_InlineMode() {
		myStorageSettings.setTagStorageMode(JpaStorageSettings.TagStorageModeEnum.INLINE);
		boolean reindexParamCache = myStorageSettings.isMarkResourcesForReindexingUponSearchParameterChange();
		myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(false);

		SearchParameter searchParameter = FhirResourceDaoR4TagsInlineTest.createSearchParameterForInlineProfile();
		ourLog.debug("SearchParam:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(searchParameter));
		mySearchParameterDao.update(searchParameter, mySrd);
		mySearchParamRegistry.forceRefresh();

		// Put a tag in so we can search for it
		String code = "http://" + UUID.randomUUID();
		Patient p = new Patient();
		p.getMeta().addProfile(code);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		myMemoryCacheService.invalidateAllCaches();

		// Search
		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add(Constants.PARAM_PROFILE, new UriParam(code));
		IBundleProvider outcome = myPatientDao.search(map, mySrd);
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());

		// Query 1 - Find resources: Just a standard token search in this mode
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
		assertEquals("SELECT t0.RES_ID FROM HFJ_SPIDX_URI t0 WHERE (t0.HASH_URI = ?) fetch first ? rows only", sql);

		// Query 2 - Load resourece contents
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(false, false);
		assertThat(sql).contains("where (rht1_0.RES_ID) in (?)");

		assertThat(toUnqualifiedVersionlessIds(outcome)).containsExactly(id);

		myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(reindexParamCache);
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testSearchByToken_IncludeHashIdentity(boolean theIncludeHashIdentity) {
		// Setup
		myStorageSettings.setIncludeHashIdentityForTokenSearches(theIncludeHashIdentity);

		// Test
		myCaptureQueriesListener.clear();
		SearchParameterMap params = SearchParameterMap.newSynchronous(Patient.SP_IDENTIFIER, new TokenParam("http://foo", "bar"));
		IBundleProvider outcome = myPatientDao.search(params, mySrd);
		assertEquals(0, outcome.sizeOrThrowNpe());

		// Verify
		if (theIncludeHashIdentity) {
			assertEquals("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE ((t0.HASH_IDENTITY = '7001889285610424179') AND (t0.HASH_SYS_AND_VALUE = '-2780914544385068076')) fetch first '10000' rows only", myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false));
		} else {
			assertEquals("SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_SYS_AND_VALUE = '-2780914544385068076') fetch first '10000' rows only", myCaptureQueriesListener.getSelectQueries().get(0).getSql(true, false));
		}
	}

	public static class MyPartitionInterceptor {

		@Hook(STORAGE_PARTITION_IDENTIFY_ANY)
		public RequestPartitionId partition() {
			return RequestPartitionId.defaultPartition();
		}

	}

}
