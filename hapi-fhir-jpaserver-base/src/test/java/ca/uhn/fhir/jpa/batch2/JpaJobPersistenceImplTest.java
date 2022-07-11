package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.JobOperationResultJson;
import ca.uhn.fhir.batch2.model.MarkWorkChunkAsErrorParameters;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.dao.data.IBatch2JobInstanceRepository;
import ca.uhn.fhir.jpa.dao.data.IBatch2WorkChunkRepository;
import ca.uhn.fhir.jpa.entity.Batch2JobInstanceEntity;
import ca.uhn.fhir.jpa.entity.Batch2WorkChunkEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaJobPersistenceImplTest {
	private static final String TEST_INSTANCE_ID = "test-instance-id";
	@Mock
	IBatch2JobInstanceRepository myJobInstanceRepository;
	@Mock
	IBatch2WorkChunkRepository myWorkChunkRepository;
	@InjectMocks
	JpaJobPersistenceImpl mySvc;

	@Test
	public void markWorkChunkAsErroredAndIncrementErrorCountAndReturn_basicCheck_works() {
		// setup
		Batch2WorkChunkEntity entity = new Batch2WorkChunkEntity();
		entity.setId("id");
		entity.setJobDefinitionId("jobdef");
		entity.setJobDefinitionVersion(1);
		entity.setTargetStepId("stepid");
		MarkWorkChunkAsErrorParameters params = new MarkWorkChunkAsErrorParameters();
		params.setChunkId(entity.getId());

		// when
		when(myWorkChunkRepository.findById(anyString()))
			.thenReturn(Optional.of(entity));

		// test
		Optional<WorkChunk> chunkReturn = mySvc.markWorkChunkAsErroredAndIncrementErrorCountAndReturn(params);

		// verify
		assertTrue(chunkReturn.isPresent());
		assertEquals(entity.getId(), chunkReturn.get().getId());
	}

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
}
