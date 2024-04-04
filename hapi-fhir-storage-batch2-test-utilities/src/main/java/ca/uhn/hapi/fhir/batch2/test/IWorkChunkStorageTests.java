package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public interface IWorkChunkStorageTests extends IWorkChunkCommon, WorkChunkTestConstants {

	@Test
	default void testStoreAndFetchWorkChunk_NoData() {
		JobInstance instance = createInstance();
		String instanceId = getSvc().storeNewInstance(instance);

		String id = storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, null);

		runInTransaction(() -> {
			WorkChunk chunk = freshFetchWorkChunk(id);
			assertNull(chunk.getData());
		});
	}
}
