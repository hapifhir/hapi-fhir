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
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkCreateEvent;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.hapi.fhir.batch2.test.models.JobMaintenanceStateInformation;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobParameters;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobStep2InputType;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobStep3InputType;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Specification tests for batch2 storage and event system.
 * These tests are abstract, and do not depend on JPA.
 * Test setups should use the public batch2 api to create scenarios.
 */
public abstract class AbstractIJobPersistenceSpecificationTest implements IJobMaintenanceActions, IInProgressActionsTests, IInstanceStateTransitions, IWorkChunkStateTransitions, IWorkChunkStorageTests, IWorkChunkErrorActionsTests, WorkChunkTestConstants {

	@Autowired
	private IJobPersistence mySvc;

	@Autowired
	private JobDefinitionRegistry myJobDefinitionRegistry;

	@Autowired
	private PlatformTransactionManager myTransactionManager;

	@Autowired
	private IJobMaintenanceService myMaintenanceService;

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
		myMaintenanceService.enableMaintenancePass(true);
	}

	@Nonnull
	public JobInstance createInstance(JobDefinition<?> theJobDefinition) {
		JobDefinition<?> jobDefinition = theJobDefinition == null ? withJobDefinition(false)
			: theJobDefinition;
		if (myJobDefinitionRegistry.getJobDefinition(theJobDefinition.getJobDefinitionId(), theJobDefinition.getJobDefinitionVersion()).isEmpty()) {
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

	public void enableMaintenanceRunner(boolean theToEnable) {
		myMaintenanceService.enableMaintenancePass(theToEnable);
	}

	public void createChunksInStates(JobMaintenanceStateInformation theJobMaintenanceStateInformation) {
		Set<String> stepIds = new HashSet<>();
		for (WorkChunk workChunk : theJobMaintenanceStateInformation.getInitialWorkChunks()) {
			mySvc.createWorkChunk(workChunk);
			stepIds.add(workChunk.getTargetStepId());
		}
		// if it's a gated job, we'll manually set the step id for the instance
		JobDefinition<?> jobDef = theJobMaintenanceStateInformation.getJobDefinition();
		if (jobDef.isGatedExecution()) {
			AtomicReference<String> latestStepId = new AtomicReference<>();
			int totalSteps = jobDef.getSteps().size();
			for (int i = totalSteps - 1; i >= 0; i--) {
				JobDefinitionStep<?, ?, ?> step = jobDef.getSteps().get(i);
				if (stepIds.contains(step.getStepId())) {
					latestStepId.set(step.getStepId());
					break;
				}
			}
			// should def have a value
			assertNotNull(latestStepId.get());
			String instanceId = theJobMaintenanceStateInformation.getInstanceId();
			mySvc.updateInstance(instanceId, instance -> {
				instance.setCurrentGatedStepId(latestStepId.get());
				return true;
			});
		}
	}
}
