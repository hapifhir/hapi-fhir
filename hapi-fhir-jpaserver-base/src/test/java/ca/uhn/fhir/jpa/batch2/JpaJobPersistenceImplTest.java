package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.JobOperationResultJson;
import ca.uhn.fhir.batch2.model.FetchJobInstancesRequest;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaJobPersistenceImplTest {
	private static final String TEST_INSTANCE_ID = "test-instance-id";
	@Mock
	IBatch2JobInstanceRepository myJobInstanceRepository;
	@Mock
	IBatch2WorkChunkRepository myWorkChunkRepository;
	@SuppressWarnings("unused") // injected into mySvc
	@Spy
	IHapiTransactionService myTxManager = new NonTransactionalHapiTransactionService();
	@InjectMocks
	JpaJobPersistenceImpl mySvc;


	@Test
	void cancelSuccess() {
		// setup
		when(myJobInstanceRepository.updateInstanceCancelled(TEST_INSTANCE_ID, true)).thenReturn(1);

		// execute
		JobOperationResultJson result = mySvc.cancelInstance(TEST_INSTANCE_ID);

		// validate
		assertTrue(result.getSuccess());
		assertEquals("Job instance <test-instance-id> successfully cancelled.", result.getMessage());
	}

	@Test
	void cancelNotFound() {
		// setup
		when(myJobInstanceRepository.updateInstanceCancelled(TEST_INSTANCE_ID, true)).thenReturn(0);
		when(myJobInstanceRepository.findById(TEST_INSTANCE_ID)).thenReturn(Optional.empty());

		// execute
		JobOperationResultJson result = mySvc.cancelInstance(TEST_INSTANCE_ID);

		// validate
		assertFalse(result.getSuccess());
		assertEquals("Job instance <test-instance-id> not found.", result.getMessage());
	}

	@Test
	void cancelAlreadyCancelled() {
		// setup
		when(myJobInstanceRepository.updateInstanceCancelled(TEST_INSTANCE_ID, true)).thenReturn(0);
		when(myJobInstanceRepository.findById(TEST_INSTANCE_ID)).thenReturn(Optional.of(new Batch2JobInstanceEntity()));

		// execute
		JobOperationResultJson result = mySvc.cancelInstance(TEST_INSTANCE_ID);

		// validate
		assertFalse(result.getSuccess());
		assertEquals("Job instance <test-instance-id> was already cancelled.  Nothing to do.", result.getMessage());
	}

	@Test
	public void deleteChunks_withInstanceId_callsChunkRepoDelete() {
		// setup
		String jobId = "jobid";

		// test
		mySvc.deleteChunksAndMarkInstanceAsChunksPurged(jobId);

		// verify
		verify(myWorkChunkRepository)
			.deleteAllForInstance(jobId);
	}

	@Test
	public void deleteInstanceAndChunks_withInstanceId_callsBothWorkchunkAndJobRespositoryDeletes() {
		// setup
		String jobid = "jobid";

		// test
		mySvc.deleteInstanceAndChunks(jobid);

		// verify
		verify(myWorkChunkRepository)
			.deleteAllForInstance(jobid);
		verify(myJobInstanceRepository)
			.deleteById(jobid);
	}

	@Test
	public void fetchInstances_validRequest_returnsFoundInstances() {
		// setup
		int pageStart = 1;
		int pageSize = 132;
		Batch2JobInstanceEntity job1 = createBatch2JobInstanceEntity();
		FetchJobInstancesRequest req = new FetchJobInstancesRequest(job1.getId(), "params");
		Batch2JobInstanceEntity job2 = createBatch2JobInstanceEntity();

		List<Batch2JobInstanceEntity> instances = Arrays.asList(job1, job2);

		// when
		when(myJobInstanceRepository
			.findInstancesByJobIdAndParams(eq(req.getJobDefinition()),
				eq(req.getParameters()),
				any(Pageable.class)))
			.thenReturn(instances);

		// test
		List<JobInstance> retInstances = mySvc.fetchInstances(req, pageStart, pageSize);

		// verify
		assertThat(retInstances).hasSize(instances.size());
		assertEquals(instances.get(0).getId(), retInstances.get(0).getInstanceId());
		assertEquals(instances.get(1).getId(), retInstances.get(1).getInstanceId());

		ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
		verify(myJobInstanceRepository)
			.findInstancesByJobIdAndParams(
				eq(req.getJobDefinition()),
				eq(req.getParameters()),
				pageableCaptor.capture()
			);

		Pageable pageable = pageableCaptor.getValue();
		assertEquals(pageStart, pageable.getPageNumber());
		assertEquals(pageSize, pageable.getPageSize());
	}

	@Test
	public void fetchInstance_validId_returnsInstance() {
		// setup
		Batch2JobInstanceEntity entity = createBatch2JobInstanceEntity();
		JobInstance instance = createJobInstanceFromEntity(entity);

		// when
		when(myJobInstanceRepository.findById(instance.getInstanceId()))
			.thenReturn(Optional.of(entity));

		// test
		Optional<JobInstance> retInstance = mySvc.fetchInstance(entity.getId());

		// verify
		assertThat(retInstance).isPresent();
		assertEquals(instance.getInstanceId(), retInstance.get().getInstanceId());
	}

	private JobInstance createJobInstanceFromEntity(Batch2JobInstanceEntity theEntity) {
		return JobInstanceUtil.fromEntityToInstance(theEntity);
	}

	private Batch2JobInstanceEntity createBatch2JobInstanceEntity() {
		Batch2JobInstanceEntity entity = new Batch2JobInstanceEntity();
		entity.setId("id");
		entity.setStartTime(Date.from(LocalDateTime.of(2000, 1, 2, 0, 0).toInstant(ZoneOffset.UTC)));
		entity.setEndTime(Date.from(LocalDateTime.of(2000, 2, 3, 0, 0).toInstant(ZoneOffset.UTC)));
		entity.setStatus(StatusEnum.COMPLETED);
		entity.setCancelled(true);
		entity.setFastTracking(true);
		entity.setCombinedRecordsProcessed(12);
		entity.setCombinedRecordsProcessedPerSecond(2d);
		entity.setTotalElapsedMillis(1000);
		entity.setWorkChunksPurged(true);
		entity.setProgress(22d);
		entity.setErrorMessage("1232");
		entity.setErrorCount(9);
		entity.setEstimatedTimeRemaining("blah");
		entity.setCurrentGatedStepId("stepid");
		entity.setReport("report");
		return entity;
	}
}
