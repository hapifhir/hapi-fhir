package ca.uhn.fhir.jpa.delete.job;

import ca.uhn.fhir.jpa.batch.api.IBatchJobSubmitter;
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import ca.uhn.fhir.jpa.batch.job.MultiUrlJobParameterUtil;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DeleteExpungeJobTest extends BaseJpaR4Test {
	@Autowired
	private IBatchJobSubmitter myBatchJobSubmitter;
	@Autowired
	@Qualifier(BatchConstants.DELETE_EXPUNGE_JOB_NAME)
	private Job myDeleteExpungeJob;
	@Autowired
	private BatchJobHelper myBatchJobHelper;

	@Test
	public void testDeleteExpunge() throws Exception {
		// setup
		Patient patientActive = new Patient();
		patientActive.setActive(true);
		IIdType pKeepId = myPatientDao.create(patientActive).getId().toUnqualifiedVersionless();

		Patient patientInactive = new Patient();
		patientInactive.setActive(false);
		IIdType pDelId = myPatientDao.create(patientInactive).getId().toUnqualifiedVersionless();

		Observation obsActive = new Observation();
		obsActive.setSubject(new Reference(pKeepId));
		IIdType oKeepId = myObservationDao.create(obsActive).getId().toUnqualifiedVersionless();

		Observation obsInactive = new Observation();
		obsInactive.setSubject(new Reference(pDelId));
		IIdType oDelId = myObservationDao.create(obsInactive).getId().toUnqualifiedVersionless();

		// validate precondition
		assertEquals(2, myPatientDao.search(SearchParameterMap.newSynchronous()).size());
		assertEquals(2, myObservationDao.search(SearchParameterMap.newSynchronous()).size());

		JobParameters jobParameters = MultiUrlJobParameterUtil.buildJobParameters("Observation?subject.active=false", "Patient?active=false");

		// execute
		JobExecution jobExecution = myBatchJobSubmitter.runJob(myDeleteExpungeJob, jobParameters);

		myBatchJobHelper.awaitJobCompletion(jobExecution);

		// validate
		assertEquals(1, myPatientDao.search(SearchParameterMap.newSynchronous()).size());
		assertEquals(1, myObservationDao.search(SearchParameterMap.newSynchronous()).size());
	}
}
