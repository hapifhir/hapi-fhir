package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.hapi.fhir.batch2.test.support.JobMaintenanceStateInformation;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobParameters;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public interface IWorkChunkCommon extends WorkChunkTestConstants {

	/**
	 * Returns the concrete class that is implementing this stuff.
	 * Used primarily for structure
	 */
	IWorkChunkCommon getTestManager();

	default String createAndStoreJobInstance(JobDefinition<?> theJobDefinition) {
		return getTestManager().createAndStoreJobInstance(theJobDefinition);
	}

	default String createAndDequeueWorkChunk(String theJobInstanceId) {
		return getTestManager().createAndDequeueWorkChunk(theJobInstanceId);
	}

	default WorkChunk freshFetchWorkChunk(String theChunkId) {
		return getTestManager().freshFetchWorkChunk(theChunkId);
	}

	default JobInstance createInstance() {
		return getTestManager().createInstance();
	}

	default String storeWorkChunk(String theJobDefinitionId, String theTargetStepId, String theInstanceId, int theSequence, String theSerializedData) {
		return getTestManager().storeWorkChunk(theJobDefinitionId, theTargetStepId, theInstanceId, theSequence, theSerializedData);
	}

	default void runInTransaction(Runnable theRunnable) {
		getTestManager().runInTransaction(theRunnable);
	}

	default void sleepUntilTimeChanges() {
		getTestManager().sleepUntilTimeChanges();
	}

	default JobDefinition<TestJobParameters> withJobDefinition(boolean theIsGatedJob) {
		return getTestManager().withJobDefinition(theIsGatedJob);
	}

	default TransactionTemplate newTxTemplate() {
		return getTestManager().newTxTemplate();
	}

	default JobInstance freshFetchJobInstance(String theInstanceId) {
		return getTestManager().freshFetchJobInstance(theInstanceId);
	}

	default void runMaintenancePass() {
		getTestManager().runMaintenancePass();
	}

	default PlatformTransactionManager getTransactionManager() {
		return getTestManager().getTransactionManager();
	}

	default IJobPersistence getSvc() {
		return getTestManager().getSvc();
	}

	/**
	 * This assumes a creation of JOB_DEFINITION already
	 * @param theJobInstanceId
	 * @return
	 */
	default String createChunk(String theJobInstanceId) {
		return getTestManager().createChunk(theJobInstanceId);
	}

	/**
	 * Enable/disable the maintenance runner (So it doesn't run on a scheduler)
	 */
	default void enableMaintenanceRunner(boolean theToEnable) {
		getTestManager().enableMaintenanceRunner(theToEnable);
	}

	/**
	 * Uses the JobMaintenanceState information and the format:
	 * "step_number|initialstate,step_number|finalstate" to construct
	 * an initial state for a test scenario
	 */
	default void createChunksInStates(JobMaintenanceStateInformation theInitialState) {
		getTestManager().createChunksInStates(theInitialState);
	}

	/**
	 * Disables the workchunk message handler
	 * so that we do not actually send messages to the queue;
	 * useful if mocking state transitions and we don't want to test
	 * dequeuing.
	 */
	default void disableWorkChunkMessageHandler() {
		getTestManager().disableWorkChunkMessageHandler();
	}

	default void verifyWorkChunkMessageHandlerCalled(int theNumberOfTimes) {
		getTestManager().verifyWorkChunkMessageHandlerCalled(theNumberOfTimes);
	}
}
