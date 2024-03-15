package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobParameters;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public interface IWorkChunkCommon extends WorkChunkTestConstants {
	String createAndStoreJobInstance();

	String createAndDequeueWorkChunk(String theJobInstanceId);

	WorkChunk freshFetchWorkChunk(String theChunkId);

	JobInstance createInstance();

	String storeWorkChunk(String theJobDefinitionId, String theTargetStepId, String theInstanceId, int theSequence, String theSerializedData);

	void runInTransaction(Runnable theRunnable);

	public void sleepUntilTimeChanges();

	JobDefinition<TestJobParameters> withJobDefinition();

	TransactionTemplate newTxTemplate();

	JobInstance freshFetchJobInstance(String theInstanceId);

	void runMaintenancePass();

	PlatformTransactionManager getTransactionManager();

	IJobPersistence getSvc();

	/**
	 * This assumes a creation of JOB_DEFINITION already
	 * @param theJobInstanceId
	 * @return
	 */
	default String createChunk(String theJobInstanceId) {
		return storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, theJobInstanceId, 0, CHUNK_DATA);
	}
}
