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

class StepProgressDataTest {

	@Test
	void testAddChunk_tracksCountsCorrectly() {
		StepProgressData data = new StepProgressData("step1");

		data.addChunk(createChunk(WorkChunkStatusEnum.COMPLETED, 100, 1000L, 2000L));
		data.addChunk(createChunk(WorkChunkStatusEnum.COMPLETED, 150, 2000L, 3000L));
		data.addChunk(createChunk(WorkChunkStatusEnum.IN_PROGRESS, null, 3000L, null));

		assertEquals(3, data.getChunkCount());
		assertEquals(2, data.getCompleteChunkCount());
		assertEquals(1, data.getIncompleteChunkCount());
		assertEquals(0, data.getErroredChunkCount());
		assertEquals(0, data.getFailedChunkCount());
		assertEquals(250, data.getRecordsProcessed());
	}

	@Test
	void testThroughputCalculation() {
		StepProgressData data = new StepProgressData("step1");

		// 1000 records in 2 seconds = 500/sec
		data.addChunk(createChunk(WorkChunkStatusEnum.COMPLETED, 500, 1000L, 2000L));
		data.addChunk(createChunk(WorkChunkStatusEnum.COMPLETED, 500, 1500L, 3000L));

		assertEquals(2000L, data.getElapsedMillis());
		assertEquals(500.0, data.getThroughputPerSecond(), 0.1);
	}

	@Test
	void testThroughput_zeroWhenNoTimingData() {
		StepProgressData data = new StepProgressData("step1");

		WorkChunk chunk = new WorkChunk();
		chunk.setStatus(WorkChunkStatusEnum.QUEUED);
		chunk.setTargetStepId("step1");
		data.addChunk(chunk);

		assertEquals(0L, data.getElapsedMillis());
		assertEquals(0.0, data.getThroughputPerSecond());
	}

	@Test
	void testCompletionPercentage() {
		StepProgressData data = new StepProgressData("step1");

		data.addChunk(createChunk(WorkChunkStatusEnum.COMPLETED, 10, 1000L, 2000L));
		data.addChunk(createChunk(WorkChunkStatusEnum.COMPLETED, 10, 2000L, 3000L));
		data.addChunk(createChunk(WorkChunkStatusEnum.IN_PROGRESS, null, 3000L, null));
		data.addChunk(createChunk(WorkChunkStatusEnum.QUEUED, null, null, null));

		// 2 complete out of 4 total = 50%
		assertEquals(0.5, data.getCompletionPercentage(), 0.001);
	}

	@Test
	void testCompletionPercentage_zeroWhenEmpty() {
		StepProgressData data = new StepProgressData("step1");
		assertEquals(0.0, data.getCompletionPercentage());
	}

	@Test
	void testEarliestAndLatestTimes() {
		StepProgressData data = new StepProgressData("step1");

		data.addChunk(createChunk(WorkChunkStatusEnum.COMPLETED, 10, 5000L, 8000L));
		data.addChunk(createChunk(WorkChunkStatusEnum.COMPLETED, 10, 2000L, 10000L));
		data.addChunk(createChunk(WorkChunkStatusEnum.COMPLETED, 10, 3000L, 7000L));

		assertEquals(new Date(2000L), data.getEarliestStartTime());
		assertEquals(new Date(10000L), data.getLatestEndTime());
	}

	@Test
	void testErroredAndFailedCounts() {
		StepProgressData data = new StepProgressData("step1");

		data.addChunk(createChunk(WorkChunkStatusEnum.ERRORED, 5, 1000L, 2000L));
		data.addChunk(createChunk(WorkChunkStatusEnum.FAILED, 3, 2000L, 3000L));
		data.addChunk(createChunk(WorkChunkStatusEnum.COMPLETED, 10, 1000L, 2000L));

		assertEquals(1, data.getErroredChunkCount());
		assertEquals(1, data.getFailedChunkCount());
		assertEquals(1, data.getCompleteChunkCount());
		assertEquals(18, data.getRecordsProcessed());
	}

	@Test
	void testToString_containsStepId() {
		StepProgressData data = new StepProgressData("myStep");
		data.addChunk(createChunk(WorkChunkStatusEnum.COMPLETED, 100, 1000L, 2000L));

		String result = data.toString();
		assertTrue(result.contains("myStep"));
		assertTrue(result.contains("100"));
	}

	private WorkChunk createChunk(WorkChunkStatusEnum theStatus, Integer theRecordsProcessed,
			Long theStartTimeMs, Long theEndTimeMs) {
		WorkChunk chunk = new WorkChunk();
		chunk.setStatus(theStatus);
		chunk.setTargetStepId("step1");
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
