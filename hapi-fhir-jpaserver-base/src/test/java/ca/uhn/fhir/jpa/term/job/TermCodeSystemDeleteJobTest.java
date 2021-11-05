package ca.uhn.fhir.jpa.term.job;

import ca.uhn.fhir.jpa.dao.r4.BaseJpaR4Test;
import ca.uhn.fhir.test.utilities.BatchJobHelper;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.JOB_PARAM_CODE_SYSTEM_ID;
import static ca.uhn.fhir.jpa.batch.config.BatchConstants.TERM_CODE_SYSTEM_DELETE_JOB_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@ContextConfiguration(classes = TermCodeSystemDeleteJobConfig.class)
public class TermCodeSystemDeleteJobTest extends BaseJpaR4Test {
	protected static final Logger ourLog = LoggerFactory.getLogger(TermCodeSystemDeleteJobTest.class);

	public static final int CONCEPT_QTY = 222;

	@Autowired
	private JobLauncher myJobLauncher;

	@Autowired
	private BatchJobHelper myBatchJobHelper;

	@Autowired @Qualifier(TERM_CODE_SYSTEM_DELETE_JOB_NAME)
	private Job myTermCodeSystemDeleteJob;


	@Test
	public void runJob() throws Exception {
		// fixme test with multiple versions

		IIdType id = createLargeCodeSystem(null);

		runInTransaction(() -> {
			assertEquals(1, myTermCodeSystemDao.count());
			assertNotNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(1, myTermCodeSystemVersionDao.count());
			assertEquals(CONCEPT_QTY, myTermConceptDao.count());
		});

		JobParameters jobParameters = new JobParameters(
			Collections.singletonMap(
				JOB_PARAM_CODE_SYSTEM_ID, new JobParameter(id.getIdPartAsLong(), true) ));


		JobExecution jobExecution = myJobLauncher.run(myTermCodeSystemDeleteJob, jobParameters);


		myBatchJobHelper.awaitJobCompletion(jobExecution);
		assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());

		runInTransaction(() -> {
			assertEquals(0, myTermCodeSystemDao.count());
			assertNull(myTermCodeSystemDao.findByCodeSystemUri("http://foo"));
			assertEquals(0, myTermCodeSystemVersionDao.count());
			assertEquals(0, myTermConceptDao.count());
		});
	}

	@Test
	public void runWithNoParameterFailsValidation() {
		JobParametersInvalidException thrown = Assertions.assertThrows(
			JobParametersInvalidException.class,
			() -> myJobLauncher.run(myTermCodeSystemDeleteJob, new JobParameters())
		);
		assertEquals("This job needs Parameter: '" + JOB_PARAM_CODE_SYSTEM_ID + "'", thrown.getMessage());
	}


	@Test
	public void runWithNullParameterFailsValidation() {
		JobParameters jobParameters = new JobParameters(
			Collections.singletonMap(
				JOB_PARAM_CODE_SYSTEM_ID, new JobParameter((Long) null, true) ));

		JobParametersInvalidException thrown = Assertions.assertThrows(
			JobParametersInvalidException.class,
			() -> myJobLauncher.run(myTermCodeSystemDeleteJob, jobParameters)
		);
		assertEquals("'" + JOB_PARAM_CODE_SYSTEM_ID + "' parameter is null", thrown.getMessage());
	}


	@Test
	public void runWithParameterZeroFailsValidation() {
		JobParameters jobParameters = new JobParameters(
			Collections.singletonMap(
				JOB_PARAM_CODE_SYSTEM_ID, new JobParameter(0L, true) ));

		JobParametersInvalidException thrown = Assertions.assertThrows(
			JobParametersInvalidException.class,
			() -> myJobLauncher.run(myTermCodeSystemDeleteJob, jobParameters)
		);
		assertEquals("Invalid parameter '" + JOB_PARAM_CODE_SYSTEM_ID + "' value: 0", thrown.getMessage());
	}


	private IIdType createLargeCodeSystem(String theVersion) {
		CodeSystem cs = new CodeSystem();
		cs.setContent(CodeSystem.CodeSystemContentMode.COMPLETE);
		cs.setUrl("http://foo");
		if (theVersion != null) {
			cs.setVersion(theVersion);
		}
		for (int i = 0; i < CONCEPT_QTY; i++) {
			cs.addConcept().setCode("CODE_" + i);
		}
		IIdType id = myCodeSystemDao.create(cs).getId().toUnqualifiedVersionless();

		myTerminologyDeferredStorageSvc.saveDeferred();

		return id;
	}





}
