package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

public interface IWorkChunkStorageTests extends IWorkChunkCommon, WorkChunkTestConstants {

	@Test
	default void testStoreAndFetchWorkChunk_NoData() {
		JobInstance instance = createInstance();
		String instanceId = getTestManager().getSvc().storeNewInstance(instance);

		String id = getTestManager().storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, instanceId, 0, null);

		getTestManager().runInTransaction(() -> {
			WorkChunk chunk = getTestManager().freshFetchWorkChunk(id);
			assertNull(chunk.getData());
		});
	}
}
