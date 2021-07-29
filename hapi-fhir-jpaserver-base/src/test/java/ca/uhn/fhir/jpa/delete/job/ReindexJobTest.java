package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.jpa.batch.BatchJobsConfig;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.batch.job.MultiUrlJobParameterUtil;
import ca.uhn.fhir.jpa.batch.job.MultiUrlProcessorJobConfig;
import ca.uhn.fhir.jpa.batch.reader.CronologicalBatchAllResourcePidReader;
import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import org.apache.commons.lang3.time.DateUtils;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReindexJobTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexJobTest.class);

	@Autowired
	private IBatchJobSubmitter myBatchJobSubmitter;
	@Autowired
	@Qualifier(BatchJobsConfig.REINDEX_JOB_NAME)
	private Job myReindexJob;
	@Autowired
	@Qualifier(BatchJobsConfig.REINDEX_EVERYTHING_JOB_NAME)
	private Job myReindexEverythingJob;
	@Autowired
	private BatchJobHelper myBatchJobHelper;

	private ReindexTestHelper myReindexTestHelper;

	@PostConstruct
	public void postConstruct() {
		myReindexTestHelper = new ReindexTestHelper(myFhirCtx, myDaoRegistry, mySearchParamRegistry);
	}

	@Test
	public void testReindexJob() throws Exception {
		// setup

		IIdType obsFinalId = myReindexTestHelper.createObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);
		IIdType obsCancelledId = myReindexTestHelper.createObservationWithAlleleExtension(Observation.ObservationStatus.CANCELLED);

		myReindexTestHelper.createAlleleSearchParameter();

		assertEquals(2, myObservationDao.search(SearchParameterMap.newSynchronous()).size());
		// The searchparam value is on the observation, but it hasn't been indexed yet
		assertThat(myReindexTestHelper.getAlleleObservationIds(), hasSize(0));

		// Only reindex one of them
		JobParameters jobParameters = MultiUrlJobParameterUtil.buildJobParameters("Observation?status=final");

		// execute
		JobExecution jobExecution = myBatchJobSubmitter.runJob(myReindexJob, jobParameters);

		myBatchJobHelper.awaitJobCompletion(jobExecution);

		// validate
		assertEquals(2, myObservationDao.search(SearchParameterMap.newSynchronous()).size());
		// Now one of them should be indexed
		List<String> alleleObservationIds = myReindexTestHelper.getAlleleObservationIds();
		assertThat(alleleObservationIds, hasSize(1));
		assertEquals(obsFinalId.getIdPart(), alleleObservationIds.get(0));
	}

	@Test
	public void testReindexEverythingJob() throws Exception {
		// setup

		for (int i = 0; i < 50; ++i) {
			myReindexTestHelper.createObservationWithAlleleExtension(Observation.ObservationStatus.FINAL);
		}

		myReindexTestHelper.createAlleleSearchParameter();
		mySearchParamRegistry.forceRefresh();

		assertEquals(50, myObservationDao.search(SearchParameterMap.newSynchronous()).size());
		// The searchparam value is on the observation, but it hasn't been indexed yet
		assertThat(myReindexTestHelper.getAlleleObservationIds(), hasSize(0));

		JobParameters jobParameters = buildEverythingJobParameters(3L);

		// execute
		JobExecution jobExecution = myBatchJobSubmitter.runJob(myReindexEverythingJob, jobParameters);

		myBatchJobHelper.awaitJobCompletion(jobExecution);

		// validate
		assertEquals(50, myObservationDao.search(SearchParameterMap.newSynchronous()).size());
		// Now one of them should be indexed
		assertThat(myReindexTestHelper.getAlleleObservationIds(), hasSize(50));
	}

	private JobParameters buildEverythingJobParameters(Long theBatchSize) {
		Map<String, JobParameter> map = new HashMap<>();
		map.put(CronologicalBatchAllResourcePidReader.JOB_PARAM_START_TIME, new JobParameter(DateUtils.addMinutes(new Date(), MultiUrlProcessorJobConfig.MINUTES_IN_FUTURE_TO_PROCESS_FROM)));
		map.put(CronologicalBatchAllResourcePidReader.JOB_PARAM_BATCH_SIZE, new JobParameter(theBatchSize.longValue()));
		JobParameters parameters = new JobParameters(map);
		return parameters;
	}


}
