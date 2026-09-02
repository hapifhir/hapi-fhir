package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.model.dstu2.resource.Encounter;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.param.UriParam;
import org.hl7.fhir.instance.model.api.IAnyResource;
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
		myStorageSettings.setHibernateSearchIndexSearchParams(false);

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
				"token not as a NOT EXISTS subselect",
				"Encounter?class:not=not-there",
				"SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND ((t0.RES_ID) NOT IN (SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) )) fetch first ? rows only",
				"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND ((t0.PARTITION_ID = ?) AND (NOT (EXISTS (SELECT s0.PARTITION_ID,s0.RES_ID FROM HFJ_SPIDX_TOKEN s0 WHERE ((s0.HASH_VALUE = ?) AND (s0.PARTITION_ID = t0.PARTITION_ID) AND (s0.RES_ID = t0.RES_ID))))))) fetch first ? rows only"
			)
			, new SqlGenerationTestCase(
				"token not on chain join - NOT EXISTS from hfj_res_link target columns",
				"Observation?encounter.class:not=not-there",
				"SELECT t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 WHERE ((t0.SRC_PATH = ?) AND ((t0.TARGET_RESOURCE_ID) NOT IN (SELECT t0.RES_ID FROM HFJ_SPIDX_TOKEN t0 WHERE (t0.HASH_VALUE = ?)) )) fetch first ? rows only",
				"SELECT t0.PARTITION_ID,t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 WHERE ((t0.SRC_PATH = ?) AND ((t0.PARTITION_ID = ?) AND (NOT (EXISTS (SELECT s0.PARTITION_ID,s0.RES_ID FROM HFJ_SPIDX_TOKEN s0 WHERE ((s0.HASH_VALUE = ?) AND (s0.PARTITION_ID = t0.TARGET_RES_PARTITION_ID) AND (s0.RES_ID = t0.TARGET_RESOURCE_ID))))))) fetch first ? rows only"
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
		assertEquals(4, myCaptureQueriesListener.logSelectQueries().size());
		// Query 1 - Resolve the tag definition id(s) up front so the resource
		// search can filter on HFJ_RES_TAG.TAG_ID directly instead of hiding the selective tag id
		// behind a HFJ_TAG_DEF join
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
		assertEquals("select td1_0.TAG_ID,td1_0.TAG_CODE,td1_0.TAG_DISPLAY,td1_0.TAG_SYSTEM,td1_0.TAG_TYPE,td1_0.TAG_USER_SELECTED,td1_0.TAG_VERSION from HFJ_TAG_DEF td1_0 where td1_0.TAG_TYPE=? and td1_0.TAG_CODE in (?)", sql);
		// Query 2 - Find resources: filter on the resolved tag id, with no HFJ_TAG_DEF join
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(false, false);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 INNER JOIN HFJ_RES_TAG t1 ON (t0.RES_ID = t1.RES_ID) WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t1.TAG_ID = ?)) fetch first ? rows only", sql);
		// Query 3 - Load resource contents
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(2).getSql(false, false);
		assertThat(sql).contains("where (mrt1_0.RES_ID) in (?)");
		// Query 4 - Load tags and definitions
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(3).getSql(false, false);
		if (theTagStorageModeEnum == JpaStorageSettings.TagStorageModeEnum.VERSIONED) {
			assertThat(sql).contains("from HFJ_HISTORY_TAG rht1_0 left join HFJ_TAG_DEF");
		} else {
			assertThat(sql).contains("from HFJ_RES_TAG rt1_0 left join HFJ_TAG_DEF");
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
		assertThat(sql).contains("where (mrt1_0.RES_ID) in (?)");

		assertThat(toUnqualifiedVersionlessIds(outcome)).containsExactly(id);

		myStorageSettings.setMarkResourcesForReindexingUponSearchParameterChange(reindexParamCache);
	}

	/**
	 * A _tag search combined with another predicate must resolve the tag id up
	 * front and filter on HFJ_RES_TAG.TAG_ID directly, rather than joining HFJ_TAG_DEF (which hides
	 * the selective tag id from the query planner and causes cardinality underestimates).
	 */
	@Test
	public void testSearchByTag_filtersOnResolvedTagIdWithoutTagDefJoin() {
		String system = "http://" + UUID.randomUUID();
		String code = "some-code";

		Patient p = new Patient();
		p.getMeta().addTag().setSystem(system).setCode(code);
		p.setActive(true);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		myMemoryCacheService.invalidateAllCaches();

		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add(Constants.PARAM_TAG, new TokenParam(system, code))
			.add(Patient.SP_ACTIVE, new TokenParam(null, "true"));
		IBundleProvider outcome = myPatientDao.search(map, mySrd);
		assertThat(toUnqualifiedVersionlessIds(outcome)).containsExactly(id);
		assertEquals(4, myCaptureQueriesListener.logSelectQueries().size());

		// Query 1 - Resolve the tag definition id(s) up front
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
		assertEquals("select td1_0.TAG_ID,td1_0.TAG_CODE,td1_0.TAG_DISPLAY,td1_0.TAG_SYSTEM,td1_0.TAG_TYPE,td1_0.TAG_USER_SELECTED,td1_0.TAG_VERSION from HFJ_TAG_DEF td1_0 where td1_0.TAG_TYPE=? and td1_0.TAG_CODE in (?)", sql);
		// Query 2 - Find resources: filter on the resolved tag id, with no HFJ_TAG_DEF join
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(false, false);
		assertEquals("SELECT t1.RES_ID FROM HFJ_RESOURCE t1 INNER JOIN HFJ_SPIDX_TOKEN t0 ON (t1.RES_ID = t0.RES_ID) INNER JOIN HFJ_RES_TAG t2 ON (t1.RES_ID = t2.RES_ID) WHERE ((t0.HASH_VALUE = ?) AND (t2.TAG_ID = ?)) fetch first ? rows only", sql);
		// Query 3 - Load resource contents
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(2).getSql(false, false);
		assertThat(sql).contains("where (mrt1_0.RES_ID) in (?)");
		// Query 4 - Load tags and definitions
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(3).getSql(false, false);
		assertThat(sql).contains("from HFJ_HISTORY_TAG rht1_0 left join HFJ_TAG_DEF");
	}

	/**
	 * Two separate `_tag` parameters (ANDed) must each resolve independently and produce their own
	 * HFJ_RES_TAG join filtered on the resolved tag id, with no HFJ_TAG_DEF join.
	 */
	@Test
	public void testSearchByTwoTagsAnded_filtersOnResolvedTagIdsWithoutTagDefJoin() {
		String system1 = "http://" + UUID.randomUUID();
		String system2 = "http://" + UUID.randomUUID();
		String code1 = "code-1";
		String code2 = "code-2";

		Patient bothTags = new Patient();
		bothTags.getMeta().addTag().setSystem(system1).setCode(code1);
		bothTags.getMeta().addTag().setSystem(system2).setCode(code2);
		bothTags.setActive(true);
		IIdType id = myPatientDao.create(bothTags, mySrd).getId().toUnqualifiedVersionless();

		// A patient carrying only one of the two tags must NOT match the AND search
		Patient oneTag = new Patient();
		oneTag.getMeta().addTag().setSystem(system1).setCode(code1);
		oneTag.setActive(true);
		myPatientDao.create(oneTag, mySrd);
		myMemoryCacheService.invalidateAllCaches();

		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add(Constants.PARAM_TAG, new TokenParam(system1, code1))
			.add(Constants.PARAM_TAG, new TokenParam(system2, code2));
		IBundleProvider outcome = myPatientDao.search(map, mySrd);
		assertThat(toUnqualifiedVersionlessIds(outcome)).containsExactly(id);
		// Both tags are resolved in a single batched lookup, then the resource search, then the two
		// result-loading queries.
		assertEquals(4, myCaptureQueriesListener.logSelectQueries().size());

		// Query 1 - Resolve both tag definition ids in a single batched HFJ_TAG_DEF lookup (one IN query)
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
		assertEquals("select td1_0.TAG_ID,td1_0.TAG_CODE,td1_0.TAG_DISPLAY,td1_0.TAG_SYSTEM,td1_0.TAG_TYPE,td1_0.TAG_USER_SELECTED,td1_0.TAG_VERSION from HFJ_TAG_DEF td1_0 where td1_0.TAG_TYPE=? and td1_0.TAG_CODE in (?,?)", sql);
		// Query 2 - Find resources: a separate HFJ_RES_TAG join per tag, each filtering on the resolved id, no HFJ_TAG_DEF join
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(false, false);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 INNER JOIN HFJ_RES_TAG t1 ON (t0.RES_ID = t1.RES_ID) INNER JOIN HFJ_RES_TAG t3 ON (t0.RES_ID = t3.RES_ID) WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND ((t1.TAG_ID = ?) AND (t3.TAG_ID = ?))) fetch first ? rows only", sql);
		// Query 3 - Load resource contents
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(2).getSql(false, false);
		assertThat(sql).contains("where (mrt1_0.RES_ID) in (?)");
		// Query 4 - Load tags and definitions
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(3).getSql(false, false);
		assertThat(sql).contains("from HFJ_HISTORY_TAG rht1_0 left join HFJ_TAG_DEF");
	}

	/**
	 * Batched tag resolution is not limited to two tags: three ANDed `_tag` parameters are still
	 * resolved with a single HFJ_TAG_DEF lookup and produce one HFJ_RES_TAG join per tag.
	 */
	@Test
	public void testSearchByThreeTagsAnded_filtersOnResolvedTagIdsWithoutTagDefJoin() {
		String system1 = "http://" + UUID.randomUUID();
		String system2 = "http://" + UUID.randomUUID();
		String system3 = "http://" + UUID.randomUUID();
		String code1 = "code-1";
		String code2 = "code-2";
		String code3 = "code-3";

		Patient allThreeTags = new Patient();
		allThreeTags.getMeta().addTag().setSystem(system1).setCode(code1);
		allThreeTags.getMeta().addTag().setSystem(system2).setCode(code2);
		allThreeTags.getMeta().addTag().setSystem(system3).setCode(code3);
		allThreeTags.setActive(true);
		IIdType id = myPatientDao.create(allThreeTags, mySrd).getId().toUnqualifiedVersionless();

		// A patient carrying only two of the three tags must NOT match the AND search
		Patient twoTags = new Patient();
		twoTags.getMeta().addTag().setSystem(system1).setCode(code1);
		twoTags.getMeta().addTag().setSystem(system2).setCode(code2);
		twoTags.setActive(true);
		myPatientDao.create(twoTags, mySrd);
		myMemoryCacheService.invalidateAllCaches();

		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add(Constants.PARAM_TAG, new TokenParam(system1, code1))
			.add(Constants.PARAM_TAG, new TokenParam(system2, code2))
			.add(Constants.PARAM_TAG, new TokenParam(system3, code3));
		IBundleProvider outcome = myPatientDao.search(map, mySrd);
		assertThat(toUnqualifiedVersionlessIds(outcome)).containsExactly(id);
		// All three tags are resolved in a single batched lookup, then the resource search, then the two
		// result-loading queries.
		assertEquals(4, myCaptureQueriesListener.logSelectQueries().size());

		// Query 1 - Resolve all three tag definition ids in a single batched HFJ_TAG_DEF lookup (one IN query)
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
		assertEquals("select td1_0.TAG_ID,td1_0.TAG_CODE,td1_0.TAG_DISPLAY,td1_0.TAG_SYSTEM,td1_0.TAG_TYPE,td1_0.TAG_USER_SELECTED,td1_0.TAG_VERSION from HFJ_TAG_DEF td1_0 where td1_0.TAG_TYPE=? and td1_0.TAG_CODE in (?,?,?)", sql);
		// Query 2 - Find resources: one HFJ_RES_TAG join per tag, each filtering on the resolved id, no HFJ_TAG_DEF join
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(false, false);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 INNER JOIN HFJ_RES_TAG t1 ON (t0.RES_ID = t1.RES_ID) INNER JOIN HFJ_RES_TAG t3 ON (t0.RES_ID = t3.RES_ID) INNER JOIN HFJ_RES_TAG t5 ON (t0.RES_ID = t5.RES_ID) WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND ((t1.TAG_ID = ?) AND (t3.TAG_ID = ?) AND (t5.TAG_ID = ?))) fetch first ? rows only", sql);
		// Query 3 - Load resource contents
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(2).getSql(false, false);
		assertThat(sql).contains("where (mrt1_0.RES_ID) in (?)");
		// Query 4 - Load tags and definitions
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(3).getSql(false, false);
		assertThat(sql).contains("from HFJ_HISTORY_TAG rht1_0 left join HFJ_TAG_DEF");
	}

	/**
	 * _security searches go through the same tag resolution path as _tag (only the tag type differs),
	 * so they too filter on HFJ_RES_TAG.TAG_ID directly rather than joining HFJ_TAG_DEF.
	 */
	@Test
	public void testSearchBySecurity_filtersOnResolvedTagIdWithoutTagDefJoin() {
		String system = "http://" + UUID.randomUUID();
		String code = "some-code";

		Patient p = new Patient();
		p.getMeta().addSecurity().setSystem(system).setCode(code);
		p.setActive(true);
		IIdType id = myPatientDao.create(p, mySrd).getId().toUnqualifiedVersionless();
		myMemoryCacheService.invalidateAllCaches();

		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add(Constants.PARAM_SECURITY, new TokenParam(system, code))
			.add(Patient.SP_ACTIVE, new TokenParam(null, "true"));
		IBundleProvider outcome = myPatientDao.search(map, mySrd);
		assertThat(toUnqualifiedVersionlessIds(outcome)).containsExactly(id);
		assertEquals(4, myCaptureQueriesListener.logSelectQueries().size());

		// Query 1 - Resolve the tag definition id(s) up front
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
		assertEquals("select td1_0.TAG_ID,td1_0.TAG_CODE,td1_0.TAG_DISPLAY,td1_0.TAG_SYSTEM,td1_0.TAG_TYPE,td1_0.TAG_USER_SELECTED,td1_0.TAG_VERSION from HFJ_TAG_DEF td1_0 where td1_0.TAG_TYPE=? and td1_0.TAG_CODE in (?)", sql);
		// Query 2 - Find resources: filter on the resolved tag id, with no HFJ_TAG_DEF join
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(false, false);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 INNER JOIN HFJ_RES_TAG t1 ON (t0.RES_ID = t1.RES_ID) INNER JOIN HFJ_SPIDX_TOKEN t3 ON (t0.RES_ID = t3.RES_ID) WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t1.TAG_ID = ?) AND (t3.HASH_VALUE = ?)) fetch first ? rows only", sql);
		// Query 3 - Load resource contents
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(2).getSql(false, false);
		assertThat(sql).contains("where (mrt1_0.RES_ID) in (?)");
		// Query 4 - Load tags and definitions
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(3).getSql(false, false);
		assertThat(sql).contains("from HFJ_HISTORY_TAG rht1_0 left join HFJ_TAG_DEF");
	}

	/**
	 * The _tag:not path must also filter its NOT-IN subquery on the resolved tag
	 * id rather than joining HFJ_TAG_DEF.
	 */
	@Test
	public void testSearchByTagNot_filtersOnResolvedTagIdWithoutTagDefJoin() {
		String system = "http://" + UUID.randomUUID();
		String code = "some-code";

		Patient tagged = new Patient();
		tagged.getMeta().addTag().setSystem(system).setCode(code);
		tagged.setActive(true);
		myPatientDao.create(tagged, mySrd);

		Patient untagged = new Patient();
		untagged.setActive(true);
		IIdType untaggedId = myPatientDao.create(untagged, mySrd).getId().toUnqualifiedVersionless();
		myMemoryCacheService.invalidateAllCaches();

		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add(Constants.PARAM_TAG, new TokenParam(system, code).setModifier(TokenParamModifier.NOT));
		IBundleProvider outcome = myPatientDao.search(map, mySrd);
		assertThat(toUnqualifiedVersionlessIds(outcome)).containsExactly(untaggedId);
		assertEquals(3, myCaptureQueriesListener.logSelectQueries().size());

		// Query 1 - Resolve the tag definition id(s) up front
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
		assertEquals("select td1_0.TAG_ID,td1_0.TAG_CODE,td1_0.TAG_DISPLAY,td1_0.TAG_SYSTEM,td1_0.TAG_TYPE,td1_0.TAG_USER_SELECTED,td1_0.TAG_VERSION from HFJ_TAG_DEF td1_0 where td1_0.TAG_TYPE=? and td1_0.TAG_CODE in (?)", sql);
		// Query 2 - Find resources: exclude those carrying the resolved tag id via a NOT IN subquery, with no HFJ_TAG_DEF join
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(false, false);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND ((t0.RES_ID) NOT IN (SELECT t0.RES_ID FROM HFJ_RES_TAG t0 WHERE (t0.TAG_ID = ?)) )) fetch first ? rows only", sql);
		// Query 3 - Load resource contents
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(2).getSql(false, false);
		assertThat(sql).contains("where (mrt1_0.RES_ID) in (?)");
	}

	/**
	 * Searching for a tag that does not exist must resolve to no results without
	 * joining HFJ_TAG_DEF (the resolved id set is empty, so the predicate matches nothing).
	 */
	@Test
	public void testSearchByNonexistentTag_returnsNoResultsWithoutTagDefJoin() {
		Patient p = new Patient();
		p.setActive(true);
		myPatientDao.create(p, mySrd);
		myMemoryCacheService.invalidateAllCaches();

		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add(Constants.PARAM_TAG, new TokenParam("http://example.org/nonexistent", "missing"));
		IBundleProvider outcome = myPatientDao.search(map, mySrd);
		assertThat(toUnqualifiedVersionlessIds(outcome)).isEmpty();
		assertEquals(2, myCaptureQueriesListener.logSelectQueries().size());

		// Query 1 - Resolve the tag definition id(s) up front
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
		assertEquals("select td1_0.TAG_ID,td1_0.TAG_CODE,td1_0.TAG_DISPLAY,td1_0.TAG_SYSTEM,td1_0.TAG_TYPE,td1_0.TAG_USER_SELECTED,td1_0.TAG_VERSION from HFJ_TAG_DEF td1_0 where td1_0.TAG_TYPE=? and td1_0.TAG_CODE in (?)", sql);
		// Query 2 - Find resources: filter on the resolved tag id, with no HFJ_TAG_DEF join. The tag does
		// not exist, so the resolved id set is empty and TAG_ID is bound to a never-matching sentinel value.
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(false, false);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 INNER JOIN HFJ_RES_TAG t1 ON (t0.RES_ID = t1.RES_ID) WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND (t1.TAG_ID = ?)) fetch first ? rows only", sql);
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

	/**
	 * Including on a reference parameter like Encounter.patient shouldn't perform a
	 * token/canonical search because that parameter doesn't resolve to a canonical
	 */
	@Test
	public void testSearchByPatientReference_NoUnion() {
		createPatient(withId("PAT-0"));
		createEncounter(withId("ENC-0"), withSubject("Patient/PAT-0"));

		myCaptureQueriesListener.clear();
		SearchParameterMap params = SearchParameterMap
			.newSynchronous(IAnyResource.SP_RES_ID, new TokenParam("Patient/PAT-0"))
			.addRevInclude(Encounter.INCLUDE_PATIENT);
		IBundleProvider outcome = myPatientDao.search(params, mySrd);
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactlyInAnyOrder(
			"Patient/PAT-0", "Encounter/ENC-0"
		);
		myCaptureQueriesListener.logSelectQueries();

		String fetchIncludeSql = myCaptureQueriesListener.getCapturedQueries().get(1).getSql(true, true);
		assertThat(fetchIncludeSql).contains("r.src_path = 'Encounter.subject.where(resolve() is Patient)'");
		assertThat(fetchIncludeSql).doesNotContain("UNION");
		assertThat(fetchIncludeSql).doesNotContain("union");
	}



	public static class MyPartitionInterceptor {

		@Hook(STORAGE_PARTITION_IDENTIFY_ANY)
		public RequestPartitionId partition() {
			return RequestPartitionId.fromPartitionId(null);
		}

	}

}
