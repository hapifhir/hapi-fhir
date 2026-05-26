package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.batch2.mockjob.MockJobAppCtx;
import ca.uhn.fhir.jpa.batch2.mockjob.sendtostep.BaseMockSendToStepWorker;
import ca.uhn.fhir.jpa.batch2.mockjob.sendtostep.MockSendToStepJobModelJson;
import ca.uhn.fhir.jpa.batch2.mockjob.sendtostep.MockSendToStepJobParameters;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static ca.uhn.fhir.jpa.batch2.mockjob.MockJobAppCtx.SEND_TO_FUTURE_STEP_JOB_ASYNC;
import static ca.uhn.fhir.jpa.batch2.mockjob.MockJobAppCtx.SEND_TO_FUTURE_STEP_JOB_GATED;
import static ca.uhn.fhir.jpa.batch2.mockjob.MockJobAppCtx.STEP_0;
import static ca.uhn.fhir.jpa.batch2.mockjob.MockJobAppCtx.STEP_1;
import static ca.uhn.fhir.jpa.batch2.mockjob.MockJobAppCtx.STEP_2;
import static ca.uhn.fhir.jpa.batch2.mockjob.MockJobAppCtx.STEP_3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MockJobAppCtx.class)
public class Batch2JobFeaturesTest extends BaseJpaR5Test {

	@Autowired
	IJobMaintenanceService myJobMaintenanceService;
	@Autowired
	@Qualifier(SEND_TO_FUTURE_STEP_JOB_ASYNC)
	JobDefinition<MockSendToStepJobParameters> mySendToFutureStepJobDefinitionAsync;
	@Autowired
	@Qualifier(SEND_TO_FUTURE_STEP_JOB_GATED)
	JobDefinition<MockSendToStepJobParameters> mySendToFutureStepJobDefinitionGated;

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		clearAllSendToStepWorkers(true);
		clearAllSendToStepWorkers(false);
	}

	/**
	 * If a job definition indicates that the job has an initial status of BUILDING,
	 * the job shouldn't automatically start when it is submitted.
	 */
	@Test
	void testJobInBuildingStatusDoesntAutoStart() {

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(MockJobAppCtx.BUILDING_JOB);
		startRequest.setParameters(new ReindexJobParameters());
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(mySrd, startRequest);
		String instanceId = startResponse.getInstanceId();

		// Job should be in BUILDING status
		assertEquals(StatusEnum.BUILDING, myJobCoordinator.getBatchInstanceStatus(instanceId).status());


		myJobMaintenanceService.forceMaintenancePass();
		myJobMaintenanceService.forceMaintenancePass();
		myJobMaintenanceService.forceMaintenancePass();

		// Job should still be in building status
		assertEquals(StatusEnum.BUILDING, myJobCoordinator.getBatchInstanceStatus(instanceId).status());

		// Move to queued
		myJobCoordinator.enqueueBuildingJobForExecution(instanceId);
		assertEquals(StatusEnum.QUEUED, myJobCoordinator.getBatchInstanceStatus(instanceId).status());

		myBatch2JobHelper.awaitJobCompletion(instanceId);

		assertEquals(StatusEnum.COMPLETED, myJobCoordinator.getBatchInstanceStatus(instanceId).status());
	}


	@Test
	void testJobInBuildingStatusCantBeEnqueuedTwice() {

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(MockJobAppCtx.BUILDING_JOB);
		startRequest.setParameters(new ReindexJobParameters());
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(mySrd, startRequest);
		String instanceId = startResponse.getInstanceId();

		assertEquals(StatusEnum.BUILDING, myJobCoordinator.getBatchInstanceStatus(instanceId).status());

		myJobCoordinator.enqueueBuildingJobForExecution(instanceId);

		assertEquals(StatusEnum.QUEUED, myJobCoordinator.getBatchInstanceStatus(instanceId).status());

		assertThatThrownBy(() -> myJobCoordinator.enqueueBuildingJobForExecution(instanceId))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Job instance is in QUEUED status and cannot be enqueued for execution");
		assertEquals(StatusEnum.QUEUED, myJobCoordinator.getBatchInstanceStatus(instanceId).status());

		myBatch2JobHelper.awaitJobCompletion(instanceId);

		assertEquals(StatusEnum.COMPLETED, myJobCoordinator.getBatchInstanceStatus(instanceId).status());
	}


	@ParameterizedTest(name = "Async={0}")
	@ValueSource(booleans = {true, false})
	void testSendToFutureStep(boolean theAsync) {
		MockSendToStepJobParameters parameters = new MockSendToStepJobParameters();
		parameters.addSendMessage(STEP_0, STEP_1, "message-from-step-0-to-1");
		parameters.addSendMessage(STEP_0, STEP_2, "message-from-step-0-to-2");
		parameters.addSendMessage(STEP_0, STEP_3, "message-from-step-0-to-3");
		parameters.addSendMessage(STEP_1, STEP_3, "message-from-step-1-to-3");
		parameters.addSendMessage(STEP_2, STEP_3, "message-from-step-2-to-3");

		JobInstanceStartRequest startRequest = createSendToFutureStepStartRequest(theAsync, parameters);
		String instanceId = myJobCoordinator.startInstance(mySrd, startRequest).getInstanceId();

		// Test
		myBatch2JobHelper.awaitJobCompletion(instanceId);

		// Verify
		List<BaseMockSendToStepWorker> workers = getSendToStepWorkers(theAsync);
		assertThat(workers.get(1).getReceivedMessages()).containsExactlyInAnyOrder(
			new MockSendToStepJobModelJson(STEP_0, "message-from-step-0-to-1")
		);
		assertThat(workers.get(2).getReceivedMessages()).containsExactlyInAnyOrder(
			new MockSendToStepJobModelJson(STEP_0, "message-from-step-0-to-2")
		);
		assertThat(workers.get(3).getReceivedMessages()).containsExactlyInAnyOrder(
			new MockSendToStepJobModelJson(STEP_0, "message-from-step-0-to-3"),
			new MockSendToStepJobModelJson(STEP_1, "message-from-step-1-to-3"),
			new MockSendToStepJobModelJson(STEP_2, "message-from-step-2-to-3")
		);
	}

	@ParameterizedTest(name = "Async={0}")
	@ValueSource(booleans = {true, false})
	void testSendToFutureStep_StepAStep(boolean theAsync) {
		MockSendToStepJobParameters parameters = new MockSendToStepJobParameters();
		parameters.addSendMessage(STEP_0, STEP_3, "message-from-step-0-to-3");
		parameters.addSendMessage(STEP_0, STEP_2, "message-from-step-0-to-2");
		parameters.addSendMessage(STEP_2, STEP_3, "message-from-step-2-to-3");

		JobInstanceStartRequest startRequest = createSendToFutureStepStartRequest(theAsync, parameters);
		String instanceId = myJobCoordinator.startInstance(mySrd, startRequest).getInstanceId();

		// Test
		myBatch2JobHelper.awaitJobCompletion(instanceId);

		// Verify
		List<BaseMockSendToStepWorker> workers = getSendToStepWorkers(theAsync);
		assertThat(workers.get(1).getReceivedMessages()).isEmpty();
		assertThat(workers.get(2).getReceivedMessages()).containsExactlyInAnyOrder(
			new MockSendToStepJobModelJson(STEP_0, "message-from-step-0-to-2")
		);
		assertThat(workers.get(3).getReceivedMessages()).containsExactlyInAnyOrder(
			new MockSendToStepJobModelJson(STEP_0, "message-from-step-0-to-3"),
			new MockSendToStepJobModelJson(STEP_2, "message-from-step-2-to-3")
		);
	}

	@ParameterizedTest(name = "Async={0}")
	@ValueSource(booleans = {true, false})
	void testSendToFutureStep_InvalidStepOrder(boolean theAsync) {
		MockSendToStepJobParameters parameters = new MockSendToStepJobParameters();
		parameters.addSendMessage(STEP_0, STEP_2, "message-from-step-0-to-2");
		parameters.addSendMessage(STEP_2, STEP_1, "message-from-step-2-to-1");

		JobInstanceStartRequest startRequest = createSendToFutureStepStartRequest(theAsync, parameters);
		String instanceId = myJobCoordinator.startInstance(mySrd, startRequest).getInstanceId();

		// Test
		JobInstance instance = myBatch2JobHelper.awaitJobFailure(instanceId);

		// Verify
		assertEquals(StatusEnum.FAILED, instance.getStatus());
		assertThat(instance.getErrorMessage()).contains("Step step-1 is not after step step-2");
	}

	@ParameterizedTest(name = "Async={0}")
	@ValueSource(booleans = {true, false})
	void testSendToFutureStep_InvalidModelType(boolean theAsync) {
		MockSendToStepJobParameters parameters = new MockSendToStepJobParameters();
		parameters.addSendMessageUsingInvalidType(STEP_0, STEP_2);

		JobInstanceStartRequest startRequest = createSendToFutureStepStartRequest(theAsync, parameters);
		String instanceId = myJobCoordinator.startInstance(mySrd, startRequest).getInstanceId();

		// Test
		JobInstance instance = myBatch2JobHelper.awaitJobFailure(instanceId);

		// Verify
		assertEquals(StatusEnum.FAILED, instance.getStatus());
		assertThat(instance.getErrorMessage()).contains("Data type BulkImportJobFileJson for step step-2 is not compatible with expected type MockSendToStepJobModelJson");
	}

	private List<BaseMockSendToStepWorker> getSendToStepWorkers(boolean theAsync) {
		JobDefinition<MockSendToStepJobParameters> definition = theAsync ? mySendToFutureStepJobDefinitionAsync : mySendToFutureStepJobDefinitionGated;
		return definition
			.getSteps()
			.stream()
			.map(t -> (BaseMockSendToStepWorker) t.getJobStepWorker())
			.toList();
	}

	private void clearAllSendToStepWorkers(boolean async) {
		getSendToStepWorkers(async).forEach(t -> t.getReceivedMessages().clear());
	}

	@Nonnull
	private static JobInstanceStartRequest createSendToFutureStepStartRequest(boolean theAsync, MockSendToStepJobParameters parameters) {
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		if (theAsync) {
			startRequest.setJobDefinitionId(SEND_TO_FUTURE_STEP_JOB_ASYNC);
		} else {
			startRequest.setJobDefinitionId(SEND_TO_FUTURE_STEP_JOB_GATED);
		}
		startRequest.setParameters(parameters);
		return startRequest;
	}

}
