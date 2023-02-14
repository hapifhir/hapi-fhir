package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.maintenance.JobInstanceProcessor;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;


/**
 * The on-enter actions are defined in
 * {@link ca.uhn.fhir.batch2.progress.JobInstanceStatusUpdater#handleStatusChange}
 * {@link ca.uhn.fhir.batch2.progress.InstanceProgress#updateStatus(JobInstance)}
 * {@link JobInstanceProcessor#cleanupInstance()}

 * For chunks:
 *   {@link ca.uhn.fhir.jpa.batch2.JpaJobPersistenceImpl#storeWorkChunk}
 *   {@link JpaJobPersistenceImpl#fetchWorkChunkSetStartTimeAndMarkInProgress(String)}
 *   Chunk execution {@link ca.uhn.fhir.batch2.coordinator.StepExecutor#executeStep}
 *   FIXME MB figure this out
 */
public class Batch2StateIT {

	@Test
	void createJobInstance_inQueuedState() {
	    // given

		// create job def + instance
		// queue it for execution.
		// on-enter actions
		// state transition triggers.
		// on-exit actions
		// activities in state

		// instance-created -> STATE:QUEUED
		// first-work-chunk received -> STATE:QUEUED->IN_PROGRESS
		//

	    // when
	    // then
	    fail();
	}

}
