package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.batch2.mockjob.MockJobAppCtx;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ContextConfiguration(classes = MockJobAppCtx.class)
public class BuildingJobTest extends BaseJpaR5Test {

	@Autowired
	protected IBatch2JobInstanceRepository myJobInstanceRepository;
	@Autowired
	protected IJobMaintenanceService myJobMaintenanceService;

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


}
