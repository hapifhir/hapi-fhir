package ca.uhn.fhir.jpa.batch2.jobs.term.valueset.preexpand;

import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.JobCompletionDetails;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.jpa.term.api.ITermValueSetStorageSvc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

// Created by Claude Fable 5.2
@ExtendWith(MockitoExtension.class)
class PreExpandValueSetErrorHandlerTest {

	private static final String THE_VS_URL = "http://foo";
	private static final String THE_VS_VERSION = "1.0";
	private static final String THE_JOB_INSTANCE_ID = "the-job-instance-id";

	@Mock
	private ITermValueSetStorageSvc myValueSetStorageSvc;
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;

	private PreExpandValueSetErrorHandler mySvc;

	@BeforeEach
	void before() {
		mySvc = new PreExpandValueSetErrorHandler(myValueSetStorageSvc);
	}

	@AfterEach
	void afterClearSynchronization() {
		if (TransactionSynchronizationManager.isSynchronizationActive()) {
			TransactionSynchronizationManager.clearSynchronization();
		}
	}

	/**
	 * batch2 calls the handler inside the transaction that stores the ended status of the job. Handling the
	 * ended job before that status is committed would leave a window in which a cancel that arrives while
	 * Step 1 is staging is noticed by neither the handler nor Step 1.
	 */
	@Test
	void jobComplete_inTransaction_handlesTheEndedJobOnlyAfterTheCommit() {
		// Setup
		TransactionSynchronizationManager.initSynchronization();

		// Test
		mySvc.jobComplete(newJobCompletionDetails());

		// Verify
		verifyNoInteractions(myValueSetStorageSvc);

		TransactionSynchronizationManager.getSynchronizations().forEach(TransactionSynchronization::afterCommit);

		verify(myValueSetStorageSvc).handlePreExpansionJobIfEnded(any(), eq(THE_VS_URL), eq(THE_VS_VERSION), eq(THE_JOB_INSTANCE_ID));
	}

	@Test
	void jobComplete_noTransaction_handlesTheEndedJobAtOnce() {
		// Test
		mySvc.jobComplete(newJobCompletionDetails());

		// Verify
		verify(myValueSetStorageSvc).handlePreExpansionJobIfEnded(any(), eq(THE_VS_URL), eq(THE_VS_VERSION), eq(THE_JOB_INSTANCE_ID));
	}

	/**
	 * A failure thrown out of the handler after the commit would surface in the batch2 maintenance pass, which
	 * can neither roll the status back nor retry.
	 */
	@Test
	void jobComplete_handlingFails_doesNotPropagateTheFailure() {
		// Setup
		when(myValueSetStorageSvc.handlePreExpansionJobIfEnded(any(), any(), any(), any()))
			.thenThrow(new IllegalStateException("the database is down"));

		// Test - a failure thrown out of the handler fails this test
		mySvc.jobComplete(newJobCompletionDetails());

		// Verify
		verify(myValueSetStorageSvc).handlePreExpansionJobIfEnded(any(), eq(THE_VS_URL), eq(THE_VS_VERSION), eq(THE_JOB_INSTANCE_ID));
	}

	private JobCompletionDetails<PreExpandValueSetParameters> newJobCompletionDetails() {
		PreExpandValueSetParameters params = new PreExpandValueSetParameters();
		params.setUrl(THE_VS_URL);
		params.setVersion(THE_VS_VERSION);

		JobInstance jobInstance = new JobInstance();
		jobInstance.setInstanceId(THE_JOB_INSTANCE_ID);

		return new JobCompletionDetails<>(params, jobInstance, myJobStepExecutionServices);
	}
}
