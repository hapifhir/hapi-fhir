package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.search.cache.SearchCacheStatusEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("unchecked")
public class PartitioningSearchCacheR4Test extends BasePartitioningR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(PartitioningSearchCacheR4Test.class);

	@Test
	public void testSearch_OnePartition_UseCache() {
		createPatient(withPartition(null), withActiveTrue());
		createPatient(withPartition(null), withActiveFalse());
		IIdType patientId11 = createPatient(withPartition(1), withActiveTrue());
		IIdType patientId12 = createPatient(withPartition(1), withActiveFalse());
		IIdType patientId21 = createPatient(withPartition(2), withActiveTrue());
		IIdType patientId22 = createPatient(withPartition(2), withActiveFalse());

		{
			myCaptureQueriesListener.clear();
			addReadPartition(1);
			PersistedJpaBundleProvider outcome = (PersistedJpaBundleProvider) myPatientDao.search(new SearchParameterMap(), mySrd);
			assertEquals(SearchCacheStatusEnum.MISS, outcome.isCacheHit());
			assertEquals(2, outcome.sizeOrThrowNpe());

			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			assertEquals(2, selectQueries.size()); // Cache check, actual search for PIDs
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertEquals(1, StringUtils.countMatches(searchSql, "from HFJ_SEARCH "), searchSql);
			assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"), searchSql);

			List<IIdType> ids = toUnqualifiedVersionlessIds(outcome);
			assertThat(ids, containsInAnyOrder(patientId11, patientId12));
		}

		// Try from a different partition
		{
			myCaptureQueriesListener.clear();
			addReadPartition(2);
			PersistedJpaBundleProvider outcome = (PersistedJpaBundleProvider) myPatientDao.search(new SearchParameterMap(), mySrd);
			assertEquals(SearchCacheStatusEnum.MISS, outcome.isCacheHit());
			assertEquals(2, outcome.sizeOrThrowNpe());

			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			assertEquals(2, selectQueries.size()); // Cache check, actual search for PIDs
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertEquals(1, StringUtils.countMatches(searchSql, "from HFJ_SEARCH "), searchSql);
			assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"), searchSql);

			List<IIdType> ids = toUnqualifiedVersionlessIds(outcome);
			assertThat(ids, containsInAnyOrder(patientId21, patientId22));
		}

		// Try from the first partition, should be a cache hit this time
		{
			myCaptureQueriesListener.clear();
			addReadPartition(2);
			PersistedJpaBundleProvider outcome = (PersistedJpaBundleProvider) myPatientDao.search(new SearchParameterMap(), mySrd);
			assertEquals(SearchCacheStatusEnum.HIT, outcome.isCacheHit());
			assertEquals(2, outcome.sizeOrThrowNpe());

			List<SqlQuery> selectQueries = myCaptureQueriesListener.logSelectQueries();
			assertEquals(2, selectQueries.size()); // Cache check, load from cache
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertEquals(1, StringUtils.countMatches(searchSql, "from HFJ_SEARCH "), searchSql);
			assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"), searchSql);

			List<IIdType> ids = toUnqualifiedVersionlessIds(outcome);
			assertThat(ids, containsInAnyOrder(patientId21, patientId22));
		}

	}

	@Test
	public void testSearch_MultiplePartitions_UseCache() {
		IIdType patientIdNull1 = createPatient(withPartition(null), withActiveTrue());
		IIdType patientIdNull2 = createPatient(withPartition(null), withActiveFalse());
		IIdType patientId11 = createPatient(withPartition(1), withActiveTrue());
		IIdType patientId12 = createPatient(withPartition(1), withActiveFalse());
		IIdType patientId21 = createPatient(withPartition(2), withActiveTrue());
		IIdType patientId22 = createPatient(withPartition(2), withActiveFalse());

		{
			myCaptureQueriesListener.clear();
			addReadPartition(1, null);
			PersistedJpaBundleProvider outcome = (PersistedJpaBundleProvider) myPatientDao.search(new SearchParameterMap(), mySrd);
			assertEquals(SearchCacheStatusEnum.MISS, outcome.isCacheHit());
			assertEquals(4, outcome.sizeOrThrowNpe());

			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			assertEquals(2, selectQueries.size()); // Cache check, actual search for PIDs
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertEquals(1, StringUtils.countMatches(searchSql, "from HFJ_SEARCH "), searchSql);
			assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"), searchSql);

			List<IIdType> ids = toUnqualifiedVersionlessIds(outcome);
			assertThat(ids, containsInAnyOrder(patientId11, patientId12, patientIdNull1, patientIdNull2));
		}

		// Try from a different partition
		{
			myCaptureQueriesListener.clear();
			addReadPartition(2, 1);
			PersistedJpaBundleProvider outcome = (PersistedJpaBundleProvider) myPatientDao.search(new SearchParameterMap(), mySrd);
			assertEquals(SearchCacheStatusEnum.MISS, outcome.isCacheHit());
			assertEquals(4, outcome.sizeOrThrowNpe());

			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			assertEquals(2, selectQueries.size()); // Cache check, actual search for PIDs
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertEquals(1, StringUtils.countMatches(searchSql, "from HFJ_SEARCH "), searchSql);
			assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"), searchSql);

			List<IIdType> ids = toUnqualifiedVersionlessIds(outcome);
			assertThat(ids, containsInAnyOrder(patientId11, patientId12, patientId21, patientId22));
		}

		// Try from the first partition, should be a cache hit this time
		{
			myCaptureQueriesListener.clear();
			addReadPartition(1, null);
			PersistedJpaBundleProvider outcome = (PersistedJpaBundleProvider) myPatientDao.search(new SearchParameterMap(), mySrd);
			assertEquals(SearchCacheStatusEnum.HIT, outcome.isCacheHit());
			assertEquals(4, outcome.sizeOrThrowNpe());

			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			assertEquals(2, selectQueries.size()); // Cache check, actual search for PIDs
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertEquals(1, StringUtils.countMatches(searchSql, "from HFJ_SEARCH "), searchSql);
			assertEquals(0, StringUtils.countMatches(searchSql, "PARTITION_ID"), searchSql);

			List<IIdType> ids = toUnqualifiedVersionlessIds(outcome);
			assertThat(ids, containsInAnyOrder(patientId11, patientId12, patientIdNull1, patientIdNull2));
		}

	}

}
