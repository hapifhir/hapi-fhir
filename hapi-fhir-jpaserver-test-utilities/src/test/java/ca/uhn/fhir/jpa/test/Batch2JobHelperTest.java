package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.coordinator.ReductionStepExecutorServiceImpl;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.broker.jms.SpringMessagingReceiverAdapter;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannel;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
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
	private static final Set<StatusEnum> NOT_ENDED = StatusEnum.getNotEndedStatuses();
	@Mock
	IJobMaintenanceService myJobMaintenanceService;
	@Mock
	IJobCoordinator myJobCoordinator;
	@Mock
	IJobPersistence myJobPersistence;
	@Mock
	SpringMessagingReceiverAdapter<JobWorkNotification> myWorkChannelConsumer;
	@Mock
	LinkedBlockingChannel myWorkChannel;
	@Mock
	ThreadPoolTaskExecutor myWorkChannelExecutor;
	@Mock
	ReductionStepExecutorServiceImpl myReductionStepExecutorService;

	Batch2JobHelper myBatch2JobHelper;
	static JobInstance ourIncompleteInstance = new JobInstance().setStatus(StatusEnum.IN_PROGRESS);
	static JobInstance ourCompleteInstance = new JobInstance().setStatus(StatusEnum.COMPLETED);

	@BeforeEach
	void before() {
		myBatch2JobHelper = new Batch2JobHelper(
			myJobMaintenanceService,
			myJobCoordinator,
			myJobPersistence,
			myWorkChannelConsumer,
			myReductionStepExecutorService);
	}

	@AfterEach
	void after() {
		verifyNoMoreInteractions(myJobCoordinator);
		verifyNoMoreInteractions(myJobMaintenanceService);
	}

	@Test
	void awaitJobCompletion_inProgress_callsMaintenance() {
		when(myJobCoordinator.getInstance(JOB_ID)).thenReturn(ourIncompleteInstance, ourIncompleteInstance, ourIncompleteInstance, ourCompleteInstance);

		myBatch2JobHelper.awaitJobCompletion(JOB_ID);
		verify(myJobMaintenanceService, times(1)).runActiveJobMaintenancePass();

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
		when(myJobPersistence.fetchInstances(1000, 0, NOT_ENDED))
			.thenReturn(List.of(failedJob, cancelledJob, completedJob));

		// execute
		boolean result = myBatch2JobHelper.hasRunningJobs();

		// verify
		assertThat(result).isFalse();
		verify(myJobPersistence).fetchInstances(1000, 0, NOT_ENDED);
	}

	@Test
	void hasRunningJobs_returnsTrue_whenActiveJobExists() {
		// setup
		JobInstance failedJob = createInstance("failed-1", StatusEnum.FAILED);
		JobInstance activeJob = createInstance("active-1", StatusEnum.IN_PROGRESS);
		activeJob.setJobDefinitionId(JOB_DEFINITION_ID);
		when(myJobPersistence.fetchInstances(1000, 0, NOT_ENDED))
			.thenReturn(List.of(failedJob, activeJob));

		// execute
		boolean result = myBatch2JobHelper.hasRunningJobs();

		// verify
		assertThat(result).isTrue();
		verify(myJobPersistence).fetchInstances(1000, 0, NOT_ENDED);
	}

	@Test
	void hasRunningJobs_runningJobOnSecondPage_returnsTrue() {
		// setup
		when(myJobPersistence.fetchInstances(1000, 0, NOT_ENDED))
			.thenReturn(List.of(createInstance("completed-1", StatusEnum.COMPLETED)));
		when(myJobPersistence.fetchInstances(1000, 1, NOT_ENDED))
			.thenReturn(List.of(createInstance("active-1", StatusEnum.IN_PROGRESS)));

		// execute
		boolean result = myBatch2JobHelper.hasRunningJobs();

		// verify
		assertThat(result).isTrue();
	}

	@Test
	void awaitNoJobsRunning_succeeds_whenOnlyTerminalJobsExist() {
		// setup
		JobInstance failedJob = createInstance("failed-1", StatusEnum.FAILED);
		JobInstance completedJob = createInstance("completed-1", StatusEnum.COMPLETED);
		when(myJobCoordinator.getInstances(1000, 0))
			.thenReturn(List.of(failedJob, completedJob));
		when(myJobCoordinator.getInstances(1000, 1))
			.thenReturn(List.of());

		// execute - should not hang or throw
		myBatch2JobHelper.awaitNoJobsRunning();

		// verify
		verify(myJobCoordinator, times(1)).getInstances(1000, 0);
		verify(myJobCoordinator, times(1)).getInstances(1000, 1);
		verify(myJobMaintenanceService, atLeastOnce()).runActiveJobMaintenancePass();
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

	@Test
	void cancelAllJobs_instancesOnTwoPages_cancelsAllWithoutWaiting() {
		// setup
		when(myJobPersistence.fetchInstances(1000, 0, NOT_ENDED))
			.thenReturn(List.of(createInstance("active-1", StatusEnum.IN_PROGRESS)));
		when(myJobPersistence.fetchInstances(1000, 1, NOT_ENDED))
			.thenReturn(List.of(createInstance("active-2", StatusEnum.QUEUED)));

		// execute
		myBatch2JobHelper.cancelAllJobs();

		// verify
		verify(myJobPersistence).cancelInstance("active-1");
		verify(myJobPersistence).cancelInstance("active-2");
		verifyNoInteractions(myWorkChannelConsumer, myReductionStepExecutorService, myJobMaintenanceService);
	}

	@Test
	@SuppressWarnings("removal")
	void cancelAllJobsAndAwaitCancellation_activeInstance_cancelsItLikeCancelAllJobs() {
		// setup
		when(myJobPersistence.fetchInstances(1000, 0, NOT_ENDED))
			.thenReturn(List.of(createInstance("active-1", StatusEnum.IN_PROGRESS)));

		// execute
		myBatch2JobHelper.cancelAllJobsAndAwaitCancellation();

		// verify
		verify(myJobPersistence).cancelInstance("active-1");
	}

	@Test
	void awaitNoInFlightWork_workStillRunning_returnsOnceWorkersAndReducerAreIdle() {
		// setup
		setUpWorkChannel();
		when(myWorkChannelExecutor.getActiveCount()).thenReturn(1, 1, 0);
		when(myWorkChannelExecutor.getQueueSize()).thenReturn(2, 0);
		when(myReductionStepExecutorService.isIdleForUnitTest()).thenReturn(false, true);

		// execute
		myBatch2JobHelper.awaitNoInFlightWork();

		// verify
		verify(myWorkChannelExecutor, atLeast(3)).getActiveCount();
		verify(myReductionStepExecutorService, atLeast(2)).isIdleForUnitTest();
		verifyNoInteractions(myJobMaintenanceService);
	}

	@Test
	void awaitNoInFlightWork_idleForOnePollOnly_waitsUntilIdleHolds() {
		// setup
		setUpWorkChannel();
		when(myWorkChannelExecutor.getActiveCount()).thenReturn(0, 1, 0);
		when(myReductionStepExecutorService.isIdleForUnitTest()).thenReturn(true);

		// execute
		myBatch2JobHelper.awaitNoInFlightWork();

		// verify
		verify(myWorkChannelExecutor, atLeast(3)).getActiveCount();
	}

	@Test
	void awaitNoInFlightWork_workNeverStops_failsNamingTheRunningWork() {
		// setup
		setUpWorkChannel();
		when(myWorkChannelExecutor.getActiveCount()).thenReturn(1);
		when(myWorkChannelExecutor.getQueueSize()).thenReturn(3);
		when(myReductionStepExecutorService.isIdleForUnitTest()).thenReturn(false);

		// execute & verify
		assertThatThrownBy(() -> myBatch2JobHelper.awaitNoInFlightWork(Duration.ofMillis(500)))
			.hasMessageContaining("\"work channel has 1 running and 3 queued work chunks\"")
			.hasMessageContaining("\"reducer is running or waiting to run a reduction step\"");
	}

	@Test
	void awaitNoInFlightWork_builtWithoutWorkChannelAndReducer_fails() {
		// setup
		Batch2JobHelper helper = new Batch2JobHelper(myJobMaintenanceService, myJobCoordinator, myJobPersistence);

		// execute & verify
		assertThatThrownBy(helper::awaitNoInFlightWork)
			.isInstanceOf(IllegalStateException.class)
			.hasMessageContaining("work channel consumer and reduction step executor");
	}

	@Test
	void awaitMaintenancePassToFinish_holdAcquired_releasesHold() throws IOException {
		// setup
		Closeable hold = mock(Closeable.class);
		when(myJobMaintenanceService.holdJobMaintenanceForExpunge()).thenReturn(hold);

		// execute
		myBatch2JobHelper.awaitMaintenancePassToFinish();

		// verify
		verify(myJobMaintenanceService).holdJobMaintenanceForExpunge();
		verify(hold).close();
	}

	@Test
	void awaitMaintenancePassToFinish_holdTimesOut_throws() {
		// setup
		when(myJobMaintenanceService.holdJobMaintenanceForExpunge())
			.thenThrow(new InternalErrorException("Timed out waiting to acquire maintenance hold"));

		// execute & verify
		assertThatThrownBy(myBatch2JobHelper::awaitMaintenancePassToFinish)
			.isInstanceOf(InternalErrorException.class)
			.hasMessageContaining("Timed out");
		verify(myJobMaintenanceService).holdJobMaintenanceForExpunge();
	}

	@Test
	void runCleanupWithBatch2Stopped_batch2Stops_runsStopCleanupAndRestartInOrder() {
		// setup
		List<String> steps = new ArrayList<>();

		// execute
		Batch2JobHelper.runCleanupWithBatch2Stopped(
			step(steps, "stop"), step(steps, "cleanup"), step(steps, "restart"));

		// verify
		assertThat(steps).containsExactly("stop", "cleanup", "restart");
	}

	@Test
	void runCleanupWithBatch2Stopped_stopTimesOut_runsCleanupThenRethrowsTimeout() {
		// setup
		List<String> steps = new ArrayList<>();
		ConditionTimeoutException timeout = new ConditionTimeoutException("batch2 work still in flight");

		// execute & verify
		assertThatThrownBy(() -> Batch2JobHelper.runCleanupWithBatch2Stopped(
				failingStep(steps, "stop", timeout), step(steps, "cleanup"), step(steps, "restart")))
			.isSameAs(timeout);
		assertThat(steps).containsExactly("stop", "cleanup", "restart");
	}

	@Test
	void runCleanupWithBatch2Stopped_stopThrowsIllegalState_runsCleanupThenRethrows() {
		// setup
		List<String> steps = new ArrayList<>();
		IllegalStateException failure = new IllegalStateException("Cannot see the worker threads");

		// execute & verify
		assertThatThrownBy(() -> Batch2JobHelper.runCleanupWithBatch2Stopped(
				failingStep(steps, "stop", failure), step(steps, "cleanup"), step(steps, "restart")))
			.isSameAs(failure);
		assertThat(steps).containsExactly("stop", "cleanup", "restart");
	}

	@Test
	void runCleanupWithBatch2Stopped_stopAndCleanupFail_throwsCleanupFailureWithStopFailureSuppressed() {
		// setup
		List<String> steps = new ArrayList<>();
		ConditionTimeoutException timeout = new ConditionTimeoutException("batch2 work still in flight");
		IllegalStateException cleanupFailure = new IllegalStateException("expunge failed");

		// execute & verify
		assertThatThrownBy(() -> Batch2JobHelper.runCleanupWithBatch2Stopped(
				failingStep(steps, "stop", timeout), failingStep(steps, "cleanup", cleanupFailure), step(steps, "restart")))
			.isSameAs(cleanupFailure)
			.hasSuppressedException(timeout);
		assertThat(steps).containsExactly("stop", "cleanup", "restart");
	}

	@Test
	void runCleanupWithBatch2Stopped_cleanupThrowsError_restartsSchedulers() {
		// setup
		List<String> steps = new ArrayList<>();
		AssertionError cleanupFailure = new AssertionError("purge failed");

		// execute & verify
		assertThatThrownBy(() -> Batch2JobHelper.runCleanupWithBatch2Stopped(
				step(steps, "stop"), failingStep(steps, "cleanup", cleanupFailure), step(steps, "restart")))
			.isSameAs(cleanupFailure);
		assertThat(steps).containsExactly("stop", "cleanup", "restart");
	}

	private static Runnable step(List<String> theSteps, String theName) {
		return () -> theSteps.add(theName);
	}

	private static Runnable failingStep(List<String> theSteps, String theName, Throwable theFailure) {
		return () -> {
			theSteps.add(theName);
			if (theFailure instanceof Error error) {
				throw error;
			}
			throw (RuntimeException) theFailure;
		};
	}

	private void setUpWorkChannel() {
		when(myWorkChannelConsumer.getSpringMessagingChannelReceiver()).thenReturn(myWorkChannel);
		when(myWorkChannel.getExecutor()).thenReturn(myWorkChannelExecutor);
	}

	private static JobInstance createInstance(String theId, StatusEnum theStatus) {
		JobInstance instance = new JobInstance().setStatus(theStatus);
		instance.setInstanceId(theId);
		return instance;
	}
}
