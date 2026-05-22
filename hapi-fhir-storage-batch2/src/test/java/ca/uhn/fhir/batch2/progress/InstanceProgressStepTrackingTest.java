package ca.uhn.fhir.batch2.progress;

import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InstanceProgressStepTrackingTest {

	@Test
	void testStepProgressMap_populatedByAddChunk() {
		InstanceProgress progress = new InstanceProgress();

		progress.addChunk(createChunk("stepA", WorkChunkStatusEnum.COMPLETED, 100, 1000L, 2000L));
		progress.addChunk(createChunk("stepA", WorkChunkStatusEnum.COMPLETED, 200, 2000L, 3000L));
		progress.addChunk(createChunk("stepB", WorkChunkStatusEnum.IN_PROGRESS, null, 3000L, null));

		Map<String, StepProgressData> stepMap = progress.getStepProgressMap();
		assertEquals(2, stepMap.size());
		assertTrue(stepMap.containsKey("stepA"));
		assertTrue(stepMap.containsKey("stepB"));

		StepProgressData stepA = stepMap.get("stepA");
		assertEquals(2, stepA.getChunkCount());
		assertEquals(2, stepA.getCompleteChunkCount());
		assertEquals(300, stepA.getRecordsProcessed());

		StepProgressData stepB = stepMap.get("stepB");
		assertEquals(1, stepB.getChunkCount());
		assertEquals(0, stepB.getCompleteChunkCount());
		assertEquals(1, stepB.getIncompleteChunkCount());
	}

	@Test
	void testGetStepProgress_returnsNullForUnknownStep() {
		InstanceProgress progress = new InstanceProgress();
		progress.addChunk(createChunk("stepA", WorkChunkStatusEnum.COMPLETED, 10, 1000L, 2000L));

		assertNull(progress.getStepProgress("nonExistentStep"));
		assertNotNull(progress.getStepProgress("stepA"));
	}

	@Test
	void testStepProgressMap_isUnmodifiable() {
		InstanceProgress progress = new InstanceProgress();
		progress.addChunk(createChunk("stepA", WorkChunkStatusEnum.COMPLETED, 10, 1000L, 2000L));

		Map<String, StepProgressData> stepMap = progress.getStepProgressMap();
		try {
			stepMap.put("illegal", new StepProgressData("illegal"));
			// Should not reach here
			assertTrue(false, "Expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
			// expected
		}
	}

	@Test
	void testMultipleSteps_independentTracking() {
		InstanceProgress progress = new InstanceProgress();

		// Step A: fast, lots of records
		progress.addChunk(createChunk("generateIds", WorkChunkStatusEnum.COMPLETED, 10000, 0L, 1000L));

		// Step B: slow, fewer records
		progress.addChunk(createChunk("processRecords", WorkChunkStatusEnum.COMPLETED, 50, 1000L, 11000L));
		progress.addChunk(createChunk("processRecords", WorkChunkStatusEnum.COMPLETED, 50, 11000L, 21000L));
		progress.addChunk(createChunk("processRecords", WorkChunkStatusEnum.IN_PROGRESS, null, 21000L, null));

		StepProgressData generateStep = progress.getStepProgress("generateIds");
		StepProgressData processStep = progress.getStepProgress("processRecords");

		// generateIds: 10000 records in 1 second = 10000/sec
		assertEquals(10000.0, generateStep.getThroughputPerSecond(), 1.0);
		assertEquals(1.0, generateStep.getCompletionPercentage());

		// processRecords: 100 records in 20 seconds = 5/sec, 2/3 complete
		assertEquals(5.0, processStep.getThroughputPerSecond(), 0.1);
		assertEquals(2.0 / 3.0, processStep.getCompletionPercentage(), 0.01);
	}

	@Test
	void testNullStepId_notTracked() {
		InstanceProgress progress = new InstanceProgress();

		WorkChunk chunk = new WorkChunk();
		chunk.setStatus(WorkChunkStatusEnum.COMPLETED);
		chunk.setTargetStepId(null);
		chunk.setRecordsProcessed(100);
		progress.addChunk(chunk);

		assertTrue(progress.getStepProgressMap().isEmpty());
	}

	private WorkChunk createChunk(String theStepId, WorkChunkStatusEnum theStatus,
			Integer theRecordsProcessed, Long theStartTimeMs, Long theEndTimeMs) {
		WorkChunk chunk = new WorkChunk();
		chunk.setTargetStepId(theStepId);
		chunk.setStatus(theStatus);
		chunk.setRecordsProcessed(theRecordsProcessed);
		if (theStartTimeMs != null) {
			chunk.setStartTime(new Date(theStartTimeMs));
		}
		if (theEndTimeMs != null) {
			chunk.setEndTime(new Date(theEndTimeMs));
		}
		return chunk;
	}
}
