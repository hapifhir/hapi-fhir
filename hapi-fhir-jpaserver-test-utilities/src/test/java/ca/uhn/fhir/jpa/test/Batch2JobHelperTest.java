package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class Batch2JobHelperTest {


	private static final String JOB_ID = "Batch2JobHelperTest";
	private static final String JOB_DEFINITION_ID = "test-job-def";
	@Mock
	IJobMaintenanceService myJobMaintenanceService;
	@Mock
	IJobCoordinator myJobCoordinator;

	@InjectMocks
	Batch2JobHelper myBatch2JobHelper;
	static JobInstance ourIncompleteInstance = new JobInstance().setStatus(StatusEnum.IN_PROGRESS);
	static JobInstance ourCompleteInstance = new JobInstance().setStatus(StatusEnum.COMPLETED);

	@AfterEach
	void after() {
		verifyNoMoreInteractions(myJobCoordinator);
		verifyNoMoreInteractions(myJobMaintenanceService);
	}

	@Test
	void awaitJobCompletion_inProgress_callsMaintenance() {
		when(myJobCoordinator.getInstance(JOB_ID)).thenReturn(ourIncompleteInstance, ourIncompleteInstance, ourIncompleteInstance, ourCompleteInstance);

		myBatch2JobHelper.awaitJobCompletion(JOB_ID);
		verify(myJobMaintenanceService, times(1)).runMaintenancePass();

	}

	@Test
	void awaitJobCompletion_alreadyComplete_doesNotCallMaintenance() {
		when(myJobCoordinator.getInstance(JOB_ID)).thenReturn(ourCompleteInstance);

		myBatch2JobHelper.awaitJobCompletion(JOB_ID);
		verifyNoInteractions(myJobMaintenanceService);
	}

	@Test
	void hasRunningJobs_returnsFalse_whenOnlyTerminalJobsExist() {
		// setup
		JobInstance failedJob = createInstance("failed-1", StatusEnum.FAILED);
		JobInstance cancelledJob = createInstance("cancelled-1", StatusEnum.CANCELLED);
		JobInstance completedJob = createInstance("completed-1", StatusEnum.COMPLETED);
		when(myJobCoordinator.getInstances(1000, 1))
			.thenReturn(List.of(failedJob, cancelledJob, completedJob));

		// execute
		boolean result = myBatch2JobHelper.hasRunningJobs();

		// verify
		assertThat(result).isFalse();
		verify(myJobCoordinator).getInstances(1000, 1);
	}

	@Test
	void hasRunningJobs_returnsTrue_whenActiveJobExists() {
		// setup
		JobInstance failedJob = createInstance("failed-1", StatusEnum.FAILED);
		JobInstance activeJob = createInstance("active-1", StatusEnum.IN_PROGRESS);
		activeJob.setJobDefinitionId(JOB_DEFINITION_ID);
		when(myJobCoordinator.getInstances(1000, 1))
			.thenReturn(List.of(failedJob, activeJob));

		// execute
		boolean result = myBatch2JobHelper.hasRunningJobs();

		// verify
		assertThat(result).isTrue();
		verify(myJobCoordinator).getInstances(1000, 1);
	}

	@Test
	void awaitNoJobsRunning_succeeds_whenOnlyTerminalJobsExist() {
		// setup
		JobInstance failedJob = createInstance("failed-1", StatusEnum.FAILED);
		JobInstance completedJob = createInstance("completed-1", StatusEnum.COMPLETED);
		when(myJobCoordinator.getInstances(1000, 1))
			.thenReturn(List.of(failedJob, completedJob));

		// execute - should not hang or throw
		myBatch2JobHelper.awaitNoJobsRunning();

		// verify
		verify(myJobCoordinator, atLeastOnce()).getInstances(1000, 1);
		verify(myJobMaintenanceService, atLeastOnce()).runMaintenancePass();
	}

	@Test
	void awaitAllJobsOfJobDefinitionIdToComplete_ignoresTerminalJobs() {
		// setup
		String activeJobId = "active-1";
		JobInstance failedJob = createInstance("failed-1", StatusEnum.FAILED);
		JobInstance activeJob = createInstance(activeJobId, StatusEnum.IN_PROGRESS);
		when(myJobCoordinator.getJobInstancesByJobDefinitionId(JOB_DEFINITION_ID, 100, 0))
			.thenReturn(List.of(failedJob, activeJob));
		when(myJobCoordinator.getInstance(activeJobId))
			.thenReturn(new JobInstance().setStatus(StatusEnum.COMPLETED));

		// execute
		myBatch2JobHelper.awaitAllJobsOfJobDefinitionIdToComplete(JOB_DEFINITION_ID);

		// verify - failed job should never be awaited
		verify(myJobCoordinator).getJobInstancesByJobDefinitionId(JOB_DEFINITION_ID, 100, 0);
		verify(myJobCoordinator, atLeastOnce()).getInstance(activeJobId);
		verify(myJobCoordinator, never()).getInstance("failed-1");
	}

	@Test
	void awaitAllJobsOfJobDefinitionIdToComplete_succeedsImmediately_whenAllJobsTerminal() {
		// setup
		JobInstance failedJob = createInstance("failed-1", StatusEnum.FAILED);
		JobInstance cancelledJob = createInstance("cancelled-1", StatusEnum.CANCELLED);
		when(myJobCoordinator.getJobInstancesByJobDefinitionId(JOB_DEFINITION_ID, 100, 0))
			.thenReturn(List.of(failedJob, cancelledJob));

		// execute - should return immediately without awaiting any jobs
		myBatch2JobHelper.awaitAllJobsOfJobDefinitionIdToComplete(JOB_DEFINITION_ID);

		// verify - no getInstance calls since all jobs were filtered out
		verify(myJobCoordinator).getJobInstancesByJobDefinitionId(JOB_DEFINITION_ID, 100, 0);
	}

	private static JobInstance createInstance(String theId, StatusEnum theStatus) {
		JobInstance instance = new JobInstance().setStatus(theStatus);
		instance.setInstanceId(theId);
		return instance;
	}
}
