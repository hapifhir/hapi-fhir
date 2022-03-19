package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.UriParam;
import org.hamcrest.Matchers;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FhirResourceDaoR4SearchSqlTest extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4SearchSqlTest.class);

	@BeforeEach
	public void before() {
		myDaoConfig.setAdvancedLuceneIndexing(false);
	}

	@AfterEach
	public void after() {
		myDaoConfig.setTagStorageMode(DaoConfig.DEFAULT_TAG_STORAGE_MODE);
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
		assertEquals("SELECT t0.RES_ID FROM HFJ_SPIDX_STRING t0 WHERE ((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?))", sql);

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
		assertEquals("SELECT t1.RES_ID FROM HFJ_RESOURCE t1 LEFT OUTER JOIN HFJ_SPIDX_STRING t0 ON (t1.RES_ID = t0.RES_ID) LEFT OUTER JOIN HFJ_SPIDX_TOKEN t2 ON (t1.RES_ID = t2.RES_ID) WHERE (((t0.HASH_NORM_PREFIX = ?) AND (t0.SP_VALUE_NORMALIZED LIKE ?)) AND (t2.HASH_SYS_AND_VALUE = ?))", sql);


	}

	@Test
	public void testSearchByProfile_VersionedMode() {

		// Put a tag in so we can search for it
		String code = "http://" + UUID.randomUUID();
		Patient p = new Patient();
		p.getMeta().addProfile(code);
		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();
		myMemoryCacheService.invalidateAllCaches();

		// Search
		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add(Constants.PARAM_PROFILE, new TokenParam(code));
		IBundleProvider outcome = myPatientDao.search(map);
		assertEquals(3, myCaptureQueriesListener.countSelectQueries());
		// Query 1 - Find resources: Make sure we search for tag type+system+code always
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 LEFT OUTER JOIN HFJ_RES_TAG t1 ON (t0.RES_ID = t1.RES_ID) LEFT OUTER JOIN HFJ_TAG_DEF t2 ON (t1.TAG_ID = t2.TAG_ID) WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND ((t2.TAG_TYPE = ?) AND (t2.TAG_SYSTEM = ?) AND (t2.TAG_CODE = ?)))", sql);
		// Query 2 - Load resourece contents
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(false, false);
		assertThat(sql, containsString("where resourcese0_.RES_ID in (?)"));
		// Query 3 - Load tags and defintions
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(2).getSql(false, false);
		assertThat(sql, containsString("from HFJ_RES_TAG resourceta0_ inner join HFJ_TAG_DEF"));

		assertThat(toUnqualifiedVersionlessIds(outcome), Matchers.contains(id));

	}

	@Test
	public void testSearchByProfile_InlineMode() {
		myDaoConfig.setTagStorageMode(DaoConfig.TagStorageModeEnum.INLINE);

		SearchParameter searchParameter = FhirResourceDaoR4TagsTest.createSearchParamForInlineResourceProfile();
		ourLog.info("SearchParam:\n{}", myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(searchParameter));
		mySearchParameterDao.update(searchParameter, mySrd);
		mySearchParamRegistry.forceRefresh();

		// Put a tag in so we can search for it
		String code = "http://" + UUID.randomUUID();
		Patient p = new Patient();
		p.getMeta().addProfile(code);
		IIdType id = myPatientDao.create(p).getId().toUnqualifiedVersionless();
		myMemoryCacheService.invalidateAllCaches();

		// Search
		myCaptureQueriesListener.clear();
		SearchParameterMap map = SearchParameterMap.newSynchronous()
			.add(Constants.PARAM_PROFILE, new UriParam(code));
		IBundleProvider outcome = myPatientDao.search(map);
		assertEquals(2, myCaptureQueriesListener.countSelectQueries());
		// Query 1 - Find resources: Just a standard token search in this mode
		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
		assertEquals("SELECT t0.RES_ID FROM HFJ_SPIDX_URI t0 WHERE (t0.HASH_URI = ?)", sql);
		// Query 2 - Load resourece contents
		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(false, false);
		assertThat(sql, containsString("where resourcese0_.RES_ID in (?)"));

		assertThat(toUnqualifiedVersionlessIds(outcome), Matchers.contains(id));

	}


}
