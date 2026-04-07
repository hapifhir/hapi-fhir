package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.jobs.reindex.ReindexJobParameters;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.batch2.mockjob.MockJobAppCtx;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.r5.BaseJpaR5Test;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
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
		runInTransaction(()->{
			Batch2JobInstanceEntity instance = myJobInstanceRepository.findById(instanceId).orElseThrow();
			assertEquals(StatusEnum.BUILDING, instance.getStatus());
		});

		myJobMaintenanceService.forceMaintenancePass();
		myJobMaintenanceService.forceMaintenancePass();
		myJobMaintenanceService.forceMaintenancePass();

		// Job should still be in building status
		runInTransaction(()-> {
				Batch2JobInstanceEntity instance = myJobInstanceRepository.findById(instanceId).orElseThrow();
				assertEquals(StatusEnum.BUILDING, instance.getStatus());
			});

		// Move to queued
		myJobCoordinator.enqueueBuildingJobForExecution(instanceId);
		runInTransaction(()-> {
				Batch2JobInstanceEntity instance = myJobInstanceRepository.findById(instanceId).orElseThrow();
				assertEquals(StatusEnum.QUEUED, instance.getStatus());
			});

		myBatch2JobHelper.awaitJobCompletion(instanceId);

		runInTransaction(()-> {
				Batch2JobInstanceEntity instance = myJobInstanceRepository.findById(instanceId).orElseThrow();
				assertEquals(StatusEnum.COMPLETED, instance.getStatus());
			});
	}



	@Test
	void testJobInBuildingStatusCantBeEnqueuedTwice() {

		// execute
		JobInstanceStartRequest startRequest = new JobInstanceStartRequest();
		startRequest.setJobDefinitionId(MockJobAppCtx.BUILDING_JOB);
		startRequest.setParameters(new ReindexJobParameters());
		Batch2JobStartResponse startResponse = myJobCoordinator.startInstance(mySrd, startRequest);
		String instanceId = startResponse.getInstanceId();

		myJobCoordinator.enqueueBuildingJobForExecution(instanceId);
		assertThatThrownBy(()->myJobCoordinator.enqueueBuildingJobForExecution(instanceId))
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Job instance is in BUILDING status and cannot be enqueued for execution");

		myBatch2JobHelper.awaitJobCompletion(instanceId);

	}



}
