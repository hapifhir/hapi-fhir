package ca.uhn.fhir.jpa.dao.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.jpa.search.PersistedJpaBundleProvider;
import ca.uhn.fhir.jpa.search.cache.SearchCacheStatusEnum;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.SqlQuery;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
			assertEquals(SearchCacheStatusEnum.MISS, outcome.getCacheStatus());
			assertEquals(2, outcome.sizeOrThrowNpe());

			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertThat(StringUtils.countMatches(searchSql, "from HFJ_SEARCH ")).as(searchSql).isEqualTo(1);
			assertThat(StringUtils.countMatches(searchSql, "PARTITION_ID")).as(searchSql).isEqualTo(0);

			List<IIdType> ids = toUnqualifiedVersionlessIds(outcome);
			assertThat(ids).containsExactlyInAnyOrder(patientId11, patientId12);
		}

		// Try from a different partition
		{
			myCaptureQueriesListener.clear();
			addReadPartition(2);
			PersistedJpaBundleProvider outcome = (PersistedJpaBundleProvider) myPatientDao.search(new SearchParameterMap(), mySrd);
			assertEquals(SearchCacheStatusEnum.MISS, outcome.getCacheStatus());
			assertEquals(2, outcome.sizeOrThrowNpe());

			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertThat(StringUtils.countMatches(searchSql, "from HFJ_SEARCH ")).as(searchSql).isEqualTo(1);
			assertThat(StringUtils.countMatches(searchSql, "PARTITION_ID")).as(searchSql).isEqualTo(0);

			List<IIdType> ids = toUnqualifiedVersionlessIds(outcome);
			assertThat(ids).containsExactlyInAnyOrder(patientId21, patientId22);
		}

		// Try from the first partition, should be a cache hit this time
		{
			myCaptureQueriesListener.clear();
			addReadPartition(2);
			PersistedJpaBundleProvider outcome = (PersistedJpaBundleProvider) myPatientDao.search(new SearchParameterMap(), mySrd);
			assertEquals(SearchCacheStatusEnum.HIT, outcome.getCacheStatus());
			assertEquals(2, outcome.sizeOrThrowNpe());

			List<SqlQuery> selectQueries = myCaptureQueriesListener.logSelectQueries();
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertThat(StringUtils.countMatches(searchSql, "from HFJ_SEARCH ")).as(searchSql).isEqualTo(1);
			assertThat(StringUtils.countMatches(searchSql, "PARTITION_ID")).as(searchSql).isEqualTo(0);

			List<IIdType> ids = toUnqualifiedVersionlessIds(outcome);
			assertThat(ids).containsExactlyInAnyOrder(patientId21, patientId22);
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
			assertEquals(SearchCacheStatusEnum.MISS, outcome.getCacheStatus());
			assertEquals(4, outcome.sizeOrThrowNpe());

			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertThat(StringUtils.countMatches(searchSql, "from HFJ_SEARCH ")).as(searchSql).isEqualTo(1);
			assertThat(StringUtils.countMatches(searchSql, "PARTITION_ID")).as(searchSql).isEqualTo(0);

			List<IIdType> ids = toUnqualifiedVersionlessIds(outcome);
			assertThat(ids).containsExactlyInAnyOrder(patientId11, patientId12, patientIdNull1, patientIdNull2);
		}

		// Try from a different partition
		{
			myCaptureQueriesListener.clear();
			addReadPartition(2, 1);
			PersistedJpaBundleProvider outcome = (PersistedJpaBundleProvider) myPatientDao.search(new SearchParameterMap(), mySrd);
			assertEquals(SearchCacheStatusEnum.MISS, outcome.getCacheStatus());
			assertEquals(4, outcome.sizeOrThrowNpe());

			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertThat(StringUtils.countMatches(searchSql, "from HFJ_SEARCH ")).as(searchSql).isEqualTo(1);
			assertThat(StringUtils.countMatches(searchSql, "PARTITION_ID")).as(searchSql).isEqualTo(0);

			List<IIdType> ids = toUnqualifiedVersionlessIds(outcome);
			assertThat(ids).containsExactlyInAnyOrder(patientId11, patientId12, patientId21, patientId22);
		}

		// Try from the first partition, should be a cache hit this time
		{
			myCaptureQueriesListener.clear();
			addReadPartition(1, null);
			PersistedJpaBundleProvider outcome = (PersistedJpaBundleProvider) myPatientDao.search(new SearchParameterMap(), mySrd);
			assertEquals(SearchCacheStatusEnum.HIT, outcome.getCacheStatus());
			assertEquals(4, outcome.sizeOrThrowNpe());

			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertThat(StringUtils.countMatches(searchSql, "from HFJ_SEARCH ")).as(searchSql).isEqualTo(1);
			assertThat(StringUtils.countMatches(searchSql, "PARTITION_ID")).as(searchSql).isEqualTo(0);

			List<IIdType> ids = toUnqualifiedVersionlessIds(outcome);
			assertThat(ids).containsExactlyInAnyOrder(patientId11, patientId12, patientIdNull1, patientIdNull2);
		}

	}

}
