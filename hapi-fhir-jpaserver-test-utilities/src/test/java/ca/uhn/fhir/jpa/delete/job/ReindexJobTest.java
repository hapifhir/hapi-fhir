package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.batch.CommonBatchJobConfig;
import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.batch.job.MultiUrlJobParameterUtil;
import ca.uhn.fhir.jpa.batch.reader.CronologicalBatchAllResourcePidReader;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.TemporalPrecisionEnum;
import ca.uhn.fhir.model.primitive.DateTimeDt;
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
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReindexJobTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ReindexJobTest.class);

	@Autowired
	private IBatchJobSubmitter myBatchJobSubmitter;
	@Autowired
	@Qualifier(BatchConstants.REINDEX_JOB_NAME)
	private Job myReindexJob;
	@Autowired
	@Qualifier(BatchConstants.REINDEX_EVERYTHING_JOB_NAME)
	private Job myReindexEverythingJob;
	@Autowired
	private BatchJobHelper myBatchJobHelper;

	private ReindexTestHelper myReindexTestHelper;

	@PostConstruct
	public void postConstruct() {
		myReindexTestHelper = new ReindexTestHelper(myFhirContext, myDaoRegistry, mySearchParamRegistry);
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

	private long generateAndReturnTimeGap() {
		long start_time = System.currentTimeMillis();
		sleepUntilTimeChanges();
		long end_time = System.currentTimeMillis();
		return end_time - start_time;
	}

	@Test
	public void testReindexJobLastUpdatedFilter() throws Exception {
		// Given
		DaoMethodOutcome T1_Patient = myReindexTestHelper.createEyeColourPatient(true);
		long timeGap1 = generateAndReturnTimeGap();
		DaoMethodOutcome T3_Patient = myReindexTestHelper.createEyeColourPatient(true);
		long timeGap2 = generateAndReturnTimeGap();
		DaoMethodOutcome T6_Patient = myReindexTestHelper.createEyeColourPatient(true);

		// Setup cutoff
		Date firstPatientLastUpdated = T1_Patient.getResource().getMeta().getLastUpdated();
		Date secondPatientLastUpdated = T3_Patient.getResource().getMeta().getLastUpdated();
		Date T2_Date = DateUtils.addMilliseconds(firstPatientLastUpdated, (int) (timeGap1 / 2));
		Date T4_Date = DateUtils.addMilliseconds(secondPatientLastUpdated, (int) (timeGap2 / 2));
		ourLog.info("Older lastUpdated: {}", firstPatientLastUpdated);
		ourLog.info("Newer lastUpdated: {}", secondPatientLastUpdated);
		ourLog.info("Cutoff Lowerbound: {}", T2_Date);
		ourLog.info("Cutoff Upperbound: {}", T4_Date);
		assertTrue(T2_Date.after(firstPatientLastUpdated));
		assertTrue(T2_Date.before(secondPatientLastUpdated));
		assertTrue(T4_Date.after(secondPatientLastUpdated));

		//Create our new SP.
		myReindexTestHelper.createEyeColourSearchParameter();

		//There exists 3 patients
		assertEquals(3, myPatientDao.search(SearchParameterMap.newSynchronous()).size());
		// The searchparam value is on the patient, but it hasn't been indexed yet, so the call to search for all with eye-colour returns 0
		assertThat(myReindexTestHelper.getEyeColourPatientIds(), hasSize(0));

		// Only reindex one of them
		String T2_DateString = new DateTimeDt(T2_Date).setPrecision(TemporalPrecisionEnum.MILLI).getValueAsString();
		String T4_DateString = new DateTimeDt(T4_Date).setPrecision(TemporalPrecisionEnum.MILLI).getValueAsString();
		JobParameters T3_Patient_JobParams = MultiUrlJobParameterUtil.buildJobParameters("Patient?_lastUpdated=ge" +
			T2_DateString + "&_lastUpdated=le" + T4_DateString);
		JobParameters T1_Patient_JobParams = MultiUrlJobParameterUtil.buildJobParameters("Patient?_lastUpdated=le" + T2_DateString);
		JobParameters T6_Patient_JobParams = MultiUrlJobParameterUtil.buildJobParameters("Patient?_lastUpdated=ge" + T4_DateString);

		// execute
		JobExecution jobExecution = myBatchJobSubmitter.runJob(myReindexJob, T3_Patient_JobParams);
		myBatchJobHelper.awaitJobCompletion(jobExecution);

		// Now one of them should be indexed for the eye colour SP
		List<String> eyeColourPatientIds = myReindexTestHelper.getEyeColourPatientIds();
		assertThat(eyeColourPatientIds, hasSize(1));
		assertEquals(T3_Patient.getId().getIdPart(), eyeColourPatientIds.get(0));

		// execute
		JobExecution jobExecutionT1 = myBatchJobSubmitter.runJob(myReindexJob, T1_Patient_JobParams);
		myBatchJobHelper.awaitJobCompletion(jobExecution);

		// Now one of them should be indexed for the eye colour SP
		eyeColourPatientIds = myReindexTestHelper.getEyeColourPatientIds();
		assertThat(eyeColourPatientIds, hasSize(2));
		assertThat(eyeColourPatientIds, hasItem(T3_Patient.getId().getIdPart()));

		// execute
		JobExecution jobExecutionT6 = myBatchJobSubmitter.runJob(myReindexJob, T6_Patient_JobParams);
		myBatchJobHelper.awaitJobCompletion(jobExecution);

		// Now one of them should be indexed for the eye colour SP
		eyeColourPatientIds = myReindexTestHelper.getEyeColourPatientIds();
		assertThat(eyeColourPatientIds, hasSize(3));
		assertThat(eyeColourPatientIds, hasItem(T6_Patient.getId().getIdPart()));
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
		// Now all of them should be indexed
		assertThat(myReindexTestHelper.getAlleleObservationIds(), hasSize(50));
	}

	private JobParameters buildEverythingJobParameters(Long theBatchSize) {
		Map<String, JobParameter> map = new HashMap<>();
		map.put(CronologicalBatchAllResourcePidReader.JOB_PARAM_START_TIME, new JobParameter(DateUtils.addMinutes(new Date(), CommonBatchJobConfig.MINUTES_IN_FUTURE_TO_PROCESS_FROM)));
		map.put(CronologicalBatchAllResourcePidReader.JOB_PARAM_BATCH_SIZE, new JobParameter(theBatchSize.longValue()));
		JobParameters parameters = new JobParameters(map);
		return parameters;
	}


}
