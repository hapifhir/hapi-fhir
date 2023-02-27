package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.JobOperationResultJson;
import ca.uhn.fhir.batch2.model.FetchJobInstancesRequest;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import ca.uhn.fhir.jpa.util.JobInstanceUtil;
import ca.uhn.fhir.model.api.PagingIterator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaJobPersistenceImplTest {
	private static final String TEST_INSTANCE_ID = "test-instance-id";
	@Mock
	IBatch2JobInstanceRepository myJobInstanceRepository;
	@Mock
	IBatch2WorkChunkRepository myWorkChunkRepository;
	@Mock
	PlatformTransactionManager myTxManager;
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
	public void markInstanceAsCompleted_withInstanceId_updatesToCompleted() {
		// setup
		String jobId = "jobid";

		// test
		mySvc.markInstanceAsCompleted(jobId);

		// verify
		ArgumentCaptor<StatusEnum> statusCaptor = ArgumentCaptor.forClass(StatusEnum.class);
		verify(myJobInstanceRepository)
			.updateInstanceStatus(eq(jobId), statusCaptor.capture());
		assertEquals(StatusEnum.COMPLETED, statusCaptor.getValue());
	}

	@Test
	public void deleteChunks_withInstanceId_callsChunkRepoDelete() {
		// setup
		String jobId = "jobid";

		// test
		mySvc.deleteChunksAndMarkInstanceAsChunksPurged(jobId);

		// verify
		verify(myWorkChunkRepository)
			.deleteAllForInstance(eq(jobId));
	}

	@Test
	public void deleteInstanceAndChunks_withInstanceId_callsBothWorkchunkAndJobRespositoryDeletes() {
		// setup
		String jobid = "jobid";

		// test
		mySvc.deleteInstanceAndChunks(jobid);

		// verify
		verify(myWorkChunkRepository)
			.deleteAllForInstance(eq(jobid));
		verify(myJobInstanceRepository)
			.deleteById(eq(jobid));
	}

	@Test
	public void updateInstance_withInstance_checksInstanceExistsAndCallsSave() {
		// setup
		JobInstance toSave = createJobInstanceWithDemoData();
		Batch2JobInstanceEntity entity = new Batch2JobInstanceEntity();
		entity.setId(toSave.getInstanceId());

		// when
		when(myJobInstanceRepository.findById(eq(toSave.getInstanceId())))
			.thenReturn(Optional.of(entity));

		// test
		mySvc.updateInstance(toSave);

		// verify
		ArgumentCaptor<Batch2JobInstanceEntity> entityCaptor = ArgumentCaptor.forClass(Batch2JobInstanceEntity.class);
		verify(myJobInstanceRepository)
			.save(entityCaptor.capture());
		Batch2JobInstanceEntity saved = entityCaptor.getValue();
		assertEquals(toSave.getInstanceId(), saved.getId());
		assertEquals(toSave.getStatus(), saved.getStatus());
		assertEquals(toSave.getStartTime(), entity.getStartTime());
		assertEquals(toSave.getEndTime(), entity.getEndTime());
		assertEquals(toSave.isCancelled(), entity.isCancelled());
		assertEquals(toSave.getCombinedRecordsProcessed(), entity.getCombinedRecordsProcessed());
		assertEquals(toSave.getCombinedRecordsProcessedPerSecond(), entity.getCombinedRecordsProcessedPerSecond());
		assertEquals(toSave.getTotalElapsedMillis(), entity.getTotalElapsedMillis());
		assertEquals(toSave.isWorkChunksPurged(), entity.getWorkChunksPurged());
		assertEquals(toSave.getProgress(), entity.getProgress());
		assertEquals(toSave.getErrorMessage(), entity.getErrorMessage());
		assertEquals(toSave.getErrorCount(), entity.getErrorCount());
		assertEquals(toSave.getEstimatedTimeRemaining(), entity.getEstimatedTimeRemaining());
		assertEquals(toSave.getCurrentGatedStepId(), entity.getCurrentGatedStepId());
		assertEquals(toSave.getReport(), entity.getReport());
	}

	@Test
	public void updateInstance_invalidId_throwsIllegalArgumentException() {
		// setup
		JobInstance instance = createJobInstanceWithDemoData();

		// when
		when(myJobInstanceRepository.findById(anyString()))
			.thenReturn(Optional.empty());

		// test
		try {
			mySvc.updateInstance(instance);
			fail();
		} catch (IllegalArgumentException ex) {
			assertTrue(ex.getMessage().contains("Unknown instance ID: " + instance.getInstanceId()));
		}
	}

	@Test
	public void fetchAllWorkChunksIterator_withValidIdAndBoolToSayToIncludeData_returnsPagingIterator() {
		// setup
		String instanceId = "instanceId";
		String jobDefinition = "definitionId";
		int version = 1;
		String targetStep = "step";

		List<Batch2WorkChunkEntity> workChunkEntityList = new ArrayList<>();
		Batch2WorkChunkEntity chunk1 = new Batch2WorkChunkEntity();
		chunk1.setId("id1");
		chunk1.setJobDefinitionVersion(version);
		chunk1.setJobDefinitionId(jobDefinition);
		chunk1.setSerializedData("serialized data 1");
		chunk1.setTargetStepId(targetStep);
		workChunkEntityList.add(chunk1);
		Batch2WorkChunkEntity chunk2 = new Batch2WorkChunkEntity();
		chunk2.setId("id2");
		chunk2.setSerializedData("serialized data 2");
		chunk2.setJobDefinitionId(jobDefinition);
		chunk2.setJobDefinitionVersion(version);
		chunk2.setTargetStepId(targetStep);
		workChunkEntityList.add(chunk2);

		for (boolean includeData : new boolean[] { true , false }) {
			// when
			when(myWorkChunkRepository.fetchChunks(any(PageRequest.class), eq(instanceId)))
				.thenReturn(workChunkEntityList);

			// test
			Iterator<WorkChunk> chunkIterator = mySvc.fetchAllWorkChunksIterator(instanceId, includeData);

			// verify
			assertTrue(chunkIterator instanceof PagingIterator);
			verify(myWorkChunkRepository, never())
				.fetchChunks(any(PageRequest.class), anyString());

			// now try the iterator out...
			WorkChunk chunk = chunkIterator.next();
			assertEquals(chunk1.getId(), chunk.getId());
			if (includeData) {
				assertEquals(chunk1.getSerializedData(), chunk.getData());
			} else {
				assertNull(chunk.getData());
			}
			chunk = chunkIterator.next();
			assertEquals(chunk2.getId(), chunk.getId());
			if (includeData) {
				assertEquals(chunk2.getSerializedData(), chunk.getData());
			} else {
				assertNull(chunk.getData());
			}

			verify(myWorkChunkRepository)
				.fetchChunks(any(PageRequest.class), eq(instanceId));

			reset(myWorkChunkRepository);
		}
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
		assertEquals(instances.size(), retInstances.size());
		assertEquals(instances.get(0).getId(),  retInstances.get(0).getInstanceId());
		assertEquals(instances.get(1).getId(),  retInstances.get(1).getInstanceId());

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
		when(myJobInstanceRepository.findById(eq(instance.getInstanceId())))
			.thenReturn(Optional.of(entity));

		// test
		Optional<JobInstance> retInstance = mySvc.fetchInstance(entity.getId());

		// verify
		assertTrue(retInstance.isPresent());
		assertEquals(instance.getInstanceId(), retInstance.get().getInstanceId());
	}

	private JobInstance createJobInstanceWithDemoData() {
		return createJobInstanceFromEntity(createBatch2JobInstanceEntity());
	}

	private JobInstance createJobInstanceFromEntity(Batch2JobInstanceEntity theEntity) {
		return JobInstanceUtil.fromEntityToInstance(theEntity);
	}

	private Batch2JobInstanceEntity createBatch2JobInstanceEntity() {
		Batch2JobInstanceEntity entity = new Batch2JobInstanceEntity();
		entity.setId("id");
		entity.setStartTime(new Date(2000, 1, 2));
		entity.setEndTime(new Date(2000, 2, 3));
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
