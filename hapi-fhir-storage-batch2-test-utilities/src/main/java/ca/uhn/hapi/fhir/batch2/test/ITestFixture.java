/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 specification tests
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.hapi.fhir.batch2.test.support.JobMaintenanceStateInformation;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobParameters;
import ca.uhn.test.concurrency.PointcutLatch;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

public interface ITestFixture {

	String createAndStoreJobInstance(JobDefinition<?> theJobDefinition);

	String createAndDequeueWorkChunk(String theJobInstanceId);

	WorkChunk freshFetchWorkChunk(String theChunkId);

	String storeWorkChunk(String theJobDefinitionId, String theTargetStepId, String theInstanceId, int theSequence, String theSerializedData, boolean theGatedExecution);

	void runInTransaction(Runnable theRunnable);

	void sleepUntilTimeChanges();

	JobDefinition<TestJobParameters> withJobDefinitionWithReductionStep();

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

	String createChunk(String theJobInstanceId, boolean theGatedExecution);

	/**
	 * Create chunk as the first chunk of a job.
	 * @return the id of the created chunk
	 */
	String createFirstChunk(JobDefinition<TestJobParameters> theJobDefinition, String theJobInstanceId);

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

	/**
	 * Uses the JobMaintenanceStateInformation to setup a test.
	 * @param theJobMaintenanceStateInformation
	 */
	void createChunksInStates(JobMaintenanceStateInformation theJobMaintenanceStateInformation);
}
