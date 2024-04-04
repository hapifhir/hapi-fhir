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

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunkCreateEvent;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobParameters;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobStep2InputType;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobStep3InputType;
import ca.uhn.test.concurrency.PointcutLatch;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.Callable;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Specification tests for batch2 storage and event system.
 * These tests are abstract, and do not depend on JPA.
 * Test setups should use the public batch2 api to create scenarios.
 */
public abstract class AbstractIJobPersistenceSpecificationTest implements IInProgressActionsTests, IInstanceStateTransitions, IWorkChunkCommon, WorkChunkTestConstants {

	private static final Logger ourLog = LoggerFactory.getLogger(AbstractIJobPersistenceSpecificationTest.class);

	@Autowired
	private IJobPersistence mySvc;

	@Autowired
	private JobDefinitionRegistry myJobDefinitionRegistry;

	@Autowired
	private PlatformTransactionManager myTransactionManager;

	@Autowired
	private IJobMaintenanceService myMaintenanceService;

	@Autowired
	private BatchJobSender myBatchJobSender;

	public PlatformTransactionManager getTransactionManager() {
		return myTransactionManager;
	}

	public IJobPersistence getSvc() {
		return mySvc;
	}

	public JobDefinition<TestJobParameters> withJobDefinition(boolean theIsGatedBoolean) {
		JobDefinition.Builder<TestJobParameters, ?> builder = JobDefinition.newBuilder()
			.setJobDefinitionId(theIsGatedBoolean ? GATED_JOB_DEFINITION_ID : JOB_DEFINITION_ID)
			.setJobDefinitionVersion(JOB_DEF_VER)
			.setJobDescription("A job description")
			.setParametersType(TestJobParameters.class)
			.addFirstStep(TARGET_STEP_ID, "the first step", TestJobStep2InputType.class, (theStepExecutionDetails, theDataSink) -> new RunOutcome(0))
			.addIntermediateStep("2nd-step-id", "the second step", TestJobStep3InputType.class, (theStepExecutionDetails, theDataSink) -> new RunOutcome(0))
			.addLastStep("last-step-id", "the final step", (theStepExecutionDetails, theDataSink) -> new RunOutcome(0));
		if (theIsGatedBoolean) {
			builder.gatedExecution();
		}
		return builder.build();
	}

	@AfterEach
	public void after() {
		myJobDefinitionRegistry.removeJobDefinition(JOB_DEFINITION_ID, JOB_DEF_VER);

		// clear invocations on the batch sender from previous jobs that might be
		// kicking around
		Mockito.clearInvocations(myBatchJobSender);
	}

	@Nested
	class WorkChunkStorage implements IWorkChunkStorageTests {

		@Override
		public IWorkChunkCommon getTestManager() {
			return AbstractIJobPersistenceSpecificationTest.this;
		}

		@Nested
		class StateTransitions implements IWorkChunkStateTransitions {

			@Override
			public IWorkChunkCommon getTestManager() {
				return AbstractIJobPersistenceSpecificationTest.this;
			}

			@Nested
			class ErrorActions implements IWorkChunkErrorActionsTests {

				@Override
				public IWorkChunkCommon getTestManager() {
					return AbstractIJobPersistenceSpecificationTest.this;
				}
			}
		}
	}

	@Override
	public IWorkChunkCommon getTestManager() {
		return this;
	}

	@Nonnull
	public JobInstance createInstance(JobDefinition<?> theJobDefinition) {
		JobDefinition<?> jobDefinition = theJobDefinition == null ? withJobDefinition(false)
			: theJobDefinition;
		if (myJobDefinitionRegistry.getJobDefinition(jobDefinition.getJobDefinitionId(), jobDefinition.getJobDefinitionVersion()).isEmpty()) {
			myJobDefinitionRegistry.addJobDefinition(jobDefinition);
		}

		JobInstance instance = new JobInstance();
		instance.setJobDefinitionId(jobDefinition.getJobDefinitionId());
		instance.setJobDefinitionVersion(jobDefinition.getJobDefinitionVersion());
		instance.setStatus(StatusEnum.QUEUED);
		instance.setJobDefinitionVersion(JOB_DEF_VER);
		instance.setParameters(CHUNK_DATA);
		instance.setReport("TEST");
		return instance;
	}

	public String storeWorkChunk(String theJobDefinitionId, String theTargetStepId, String theInstanceId, int theSequence, String theSerializedData) {
		WorkChunkCreateEvent batchWorkChunk = new WorkChunkCreateEvent(theJobDefinitionId, JOB_DEF_VER, theTargetStepId, theInstanceId, theSequence, theSerializedData);
		return mySvc.onWorkChunkCreate(batchWorkChunk);
	}

	public abstract PlatformTransactionManager getTxManager();

	public JobInstance freshFetchJobInstance(String theInstanceId) {
		return runInTransaction(() -> mySvc.fetchInstance(theInstanceId).orElseThrow());
	}

	public TransactionTemplate newTxTemplate() {
		TransactionTemplate retVal = new TransactionTemplate(getTxManager());
		retVal.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		retVal.afterPropertiesSet();
		return retVal;
	}

	public void runInTransaction(Runnable theRunnable) {
		newTxTemplate().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(@Nonnull TransactionStatus theStatus) {
				theRunnable.run();
			}
		});
	}

	public <T> T runInTransaction(Callable<T> theRunnable) {
		return newTxTemplate().execute(t -> {
			try {
				return theRunnable.call();
			} catch (Exception theE) {
				throw new InternalErrorException(theE);
			}
		});
	}


	/**
	 * Sleep until at least 1 ms has elapsed
	 */
	public void sleepUntilTimeChanges() {
		StopWatch sw = new StopWatch();
		await().until(() -> sw.getMillis() > 0);
	}

	public String createAndStoreJobInstance(JobDefinition<?> theJobDefinition) {
		JobInstance jobInstance = createInstance(theJobDefinition);
		return mySvc.storeNewInstance(jobInstance);
	}

	public String createAndDequeueWorkChunk(String theJobInstanceId) {
		String chunkId = createChunk(theJobInstanceId);
		mySvc.onWorkChunkDequeue(chunkId);
		return chunkId;
	}

	public String createChunk(String theInstanceId) {
		return storeWorkChunk(JOB_DEFINITION_ID, TARGET_STEP_ID, theInstanceId, 0, CHUNK_DATA);
	}

	public PointcutLatch disableWorkChunkMessageHandler() {
		PointcutLatch latch = new PointcutLatch(new Exception().getStackTrace()[0].getMethodName());

		doAnswer(a -> {
			latch.call(1);
			return Void.class;
		}).when(myBatchJobSender).sendWorkChannelMessage(any(JobWorkNotification.class));
		return latch;
	}

	public void verifyWorkChunkMessageHandlerCalled(PointcutLatch theSendingLatch, int theNumberOfTimes) throws InterruptedException {
		theSendingLatch.awaitExpected();
		ArgumentCaptor<JobWorkNotification> notificationCaptor = ArgumentCaptor.forClass(JobWorkNotification.class);

		verify(myBatchJobSender, times(theNumberOfTimes))
			.sendWorkChannelMessage(notificationCaptor.capture());
	}
}
