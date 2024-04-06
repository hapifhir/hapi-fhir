package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobParameters;
import ca.uhn.test.concurrency.PointcutLatch;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public interface ITestFixture {

	String createAndStoreJobInstance(JobDefinition<?> theJobDefinition);

	String createAndDequeueWorkChunk(String theJobInstanceId);

	WorkChunk freshFetchWorkChunk(String theChunkId);

	String storeWorkChunk(String theJobDefinitionId, String theTargetStepId, String theInstanceId, int theSequence, String theSerializedData);

	void runInTransaction(Runnable theRunnable);

	void sleepUntilTimeChanges();

	JobDefinition<TestJobParameters> withJobDefinition(boolean theIsGatedJob);

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
	String createChunk(String theJobInstanceId);

	/**
	 * Enable/disable the maintenance runner (So it doesn't run on a scheduler)
	 */
	void enableMaintenanceRunner(boolean theToEnable);

	/**
	 * Disables the workchunk message handler
	 * so that we do not actually send messages to the queue;
	 * useful if mocking state transitions and we don't want to test
	 * dequeuing.
	 * Returns a latch that will fire each time a message is sent.
	 */
	PointcutLatch disableWorkChunkMessageHandler();

	/**
	 *
	 * @param theSendingLatch the latch sent back from the disableWorkChunkMessageHandler method above
	 * @param theNumberOfTimes the number of invocations to expect
	 */
	void verifyWorkChunkMessageHandlerCalled(PointcutLatch theSendingLatch, int theNumberOfTimes) throws InterruptedException;
}
