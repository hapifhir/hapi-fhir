package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.util.BundleBuilder;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.OperationOutcome;
import org.hl7.fhir.r4.model.Patient;

import org.hl7.fhir.r4.model.StringType;

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
import java.util.stream.Collectors;

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
		logAllResources();

		{
			myCaptureQueriesListener.clear();
			addNextTargetPartitionsForRead(1);
			PersistedJpaBundleProvider outcome = (PersistedJpaBundleProvider) myPatientDao.search(new SearchParameterMap(), mySrd);
			assertEquals(SearchCacheStatusEnum.MISS, outcome.getCacheStatus());
			assertEquals(2, outcome.sizeOrThrowNpe(), ()-> "Resources:\n * " + runInTransaction(()->myResourceTableDao.findAll().stream().map(t->t.toString()).collect(Collectors.joining("\n * "))) +
				"\n\nActual IDs: " + toUnqualifiedVersionlessIdValues(outcome) +
				"\n\nSQL Queries: " + myCaptureQueriesListener.getSelectQueries().stream().map(t->t.getSql(true, false)).collect(Collectors.joining("\n * ")));

			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertThat(StringUtils.countMatches(searchSql, "from HFJ_SEARCH ")).as(searchSql).isEqualTo(1);
			assertThat(StringUtils.countMatches(searchSql, "PARTITION_ID")).as(searchSql).isEqualTo(1);

			List<IIdType> ids = toUnqualifiedVersionlessIds(outcome);
			assertThat(ids).containsExactlyInAnyOrder(patientId11, patientId12);
		}

		// Try from a different partition
		{
			myCaptureQueriesListener.clear();
			addNextTargetPartitionsForRead(2);
			PersistedJpaBundleProvider outcome = (PersistedJpaBundleProvider) myPatientDao.search(new SearchParameterMap(), mySrd);
			assertEquals(SearchCacheStatusEnum.MISS, outcome.getCacheStatus());
			assertEquals(2, outcome.sizeOrThrowNpe());

			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertThat(StringUtils.countMatches(searchSql, "from HFJ_SEARCH ")).as(searchSql).isEqualTo(1);
			assertThat(StringUtils.countMatches(searchSql, "PARTITION_ID")).as(searchSql).isEqualTo(1);

			List<IIdType> ids = toUnqualifiedVersionlessIds(outcome);
			assertThat(ids).containsExactlyInAnyOrder(patientId21, patientId22);
		}

		// Try from the first partition, should be a cache hit this time
		{
			myCaptureQueriesListener.clear();
			addNextTargetPartitionsForRead(2);
			PersistedJpaBundleProvider outcome = (PersistedJpaBundleProvider) myPatientDao.search(new SearchParameterMap(), mySrd);
			assertEquals(SearchCacheStatusEnum.HIT, outcome.getCacheStatus());
			assertEquals(2, outcome.sizeOrThrowNpe());

			List<SqlQuery> selectQueries = myCaptureQueriesListener.logSelectQueries();
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertThat(StringUtils.countMatches(searchSql, "from HFJ_SEARCH ")).as(searchSql).isEqualTo(1);
			assertThat(StringUtils.countMatches(searchSql, "PARTITION_ID")).as(searchSql).isEqualTo(1);

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
			addNextTargetPartitionsForRead(1, null);
			PersistedJpaBundleProvider outcome = (PersistedJpaBundleProvider) myPatientDao.search(new SearchParameterMap(), mySrd);
			assertEquals(SearchCacheStatusEnum.MISS, outcome.getCacheStatus());
			assertEquals(4, outcome.sizeOrThrowNpe());

			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertThat(StringUtils.countMatches(searchSql, "from HFJ_SEARCH ")).as(searchSql).isEqualTo(1);
			assertThat(StringUtils.countMatches(searchSql, "PARTITION_ID")).as(searchSql).isEqualTo(1);

			List<IIdType> ids = toUnqualifiedVersionlessIds(outcome);
			assertThat(ids).containsExactlyInAnyOrder(patientId11, patientId12, patientIdNull1, patientIdNull2);
		}

		// Try from a different partition
		{
			myCaptureQueriesListener.clear();
			addNextTargetPartitionsForRead(2, 1);
			PersistedJpaBundleProvider outcome = (PersistedJpaBundleProvider) myPatientDao.search(new SearchParameterMap(), mySrd);
			assertEquals(SearchCacheStatusEnum.MISS, outcome.getCacheStatus());
			assertEquals(4, outcome.sizeOrThrowNpe());

			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertThat(StringUtils.countMatches(searchSql, "from HFJ_SEARCH ")).as(searchSql).isEqualTo(1);
			assertThat(StringUtils.countMatches(searchSql, "PARTITION_ID")).as(searchSql).isEqualTo(1);

			List<IIdType> ids = toUnqualifiedVersionlessIds(outcome);
			assertThat(ids).containsExactlyInAnyOrder(patientId11, patientId12, patientId21, patientId22);
		}

		// Try from the first partition, should be a cache hit this time
		{
			myCaptureQueriesListener.clear();
			addNextTargetPartitionsForRead(1, null);
			PersistedJpaBundleProvider outcome = (PersistedJpaBundleProvider) myPatientDao.search(new SearchParameterMap(), mySrd);
			assertEquals(SearchCacheStatusEnum.HIT, outcome.getCacheStatus());
			assertEquals(4, outcome.sizeOrThrowNpe());

			List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
			String searchSql = selectQueries.get(0).getSql(true, false);
			assertThat(StringUtils.countMatches(searchSql, "from HFJ_SEARCH ")).as(searchSql).isEqualTo(1);
			assertThat(StringUtils.countMatches(searchSql, "PARTITION_ID")).as(searchSql).isEqualTo(1);

			List<IIdType> ids = toUnqualifiedVersionlessIds(outcome);
			assertThat(ids).containsExactlyInAnyOrder(patientId11, patientId12, patientIdNull1, patientIdNull2);
		}

	}

	@Test
	public void testConditionalCreate_withMultiplePartitionsAndMatchUrlCache_NoCachePartitionConflicts() {
		myStorageSettings.setMatchUrlCacheEnabled(true);
		myPartitionSettings.setConditionalCreateDuplicateIdentifiersEnabled(true);

		Bundle responseBundle1 = mySystemDao.transaction(mySrd, createPatientWithConditionalUrlOnPartition(1));
		assertResourceCreated(responseBundle1);

		Bundle responseBundle2 = mySystemDao.transaction(mySrd, createPatientWithConditionalUrlOnPartition(2));
		assertResourceCreated(responseBundle2);
	}

	private Bundle createPatientWithConditionalUrlOnPartition(Integer thePartitionId) {
		BundleBuilder bb = new BundleBuilder(myFhirContext);

		Patient p = new Patient();
		p.setIdentifier(List.of(new Identifier().setSystem("foo").setValue("bar")));
		p.setActive(true);
		p.setName(List.of(new HumanName().setFamily("ABC").setGiven(List.of(new StringType("DEF")))));
		bb.addTransactionUpdateEntry(p, "Patient?identifier=foo|bar");
		addNextTargetPartitionNTimesForCreate(thePartitionId, 3);

		return (Bundle) bb.getBundle();
	}

	private static void assertResourceCreated(Bundle responseBundle1) {
		assertThat(responseBundle1.getEntry()).hasSize(1);
		Bundle.BundleEntryResponseComponent response1 = responseBundle1.getEntryFirstRep().getResponse();
		assertThat(response1.getStatus()).isEqualTo("201 Created");
		OperationOutcome oo1 = (OperationOutcome) response1.getOutcome();
		assertThat(oo1.getIssue()).hasSize(1);
		CodeableConcept details1 = oo1.getIssueFirstRep().getDetails();
		assertThat(details1.getCodingFirstRep().getCode()).isEqualTo("SUCCESSFUL_UPDATE_NO_CONDITIONAL_MATCH");
	}
}
