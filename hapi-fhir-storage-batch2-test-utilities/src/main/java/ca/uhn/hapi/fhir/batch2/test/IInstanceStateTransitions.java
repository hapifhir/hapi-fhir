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
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.maintenance.JobChunkProgressAccumulator;
import ca.uhn.fhir.batch2.maintenance.JobInstanceProcessor;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public interface IInstanceStateTransitions extends IWorkChunkCommon, WorkChunkTestConstants {
	Logger ourLog = LoggerFactory.getLogger(IInstanceStateTransitions.class);

	@Test
	default void createInstance_createsInQueuedWithChunkInReady() {
		// given
		JobDefinition<?> jd = getTestManager().withJobDefinition(false);

		// when
		IJobPersistence.CreateResult createResult =
			getTestManager().newTxTemplate().execute(status->
				getTestManager().getSvc().onCreateWithFirstChunk(jd, "{}"));

		// then
		ourLog.info("job and chunk created {}", createResult);
		assertNotNull(createResult);
		assertThat(createResult.jobInstanceId).isNotEmpty();
		assertThat(createResult.workChunkId).isNotEmpty();

		JobInstance jobInstance = getTestManager().freshFetchJobInstance(createResult.jobInstanceId);
		assertEquals(StatusEnum.QUEUED, jobInstance.getStatus());
		assertEquals("{}", jobInstance.getParameters());

		WorkChunk firstChunk = getTestManager().freshFetchWorkChunk(createResult.workChunkId);
		assertEquals(WorkChunkStatusEnum.READY, firstChunk.getStatus());
		assertNull(firstChunk.getData(), "First chunk data is null - only uses parameters");
	}

	@Test
	default void testCreateInstance_firstChunkDequeued_movesToInProgress() {
		// given
		JobDefinition<?> jd = getTestManager().withJobDefinition(false);
		IJobPersistence.CreateResult createResult = getTestManager().newTxTemplate().execute(status->
			getTestManager().getSvc().onCreateWithFirstChunk(jd, "{}"));
		assertNotNull(createResult);

		// when
		getTestManager().newTxTemplate().execute(status -> getTestManager().getSvc().onChunkDequeued(createResult.jobInstanceId));

		// then
		JobInstance jobInstance = getTestManager().freshFetchJobInstance(createResult.jobInstanceId);
		assertEquals(StatusEnum.IN_PROGRESS, jobInstance.getStatus());
	}

	@ParameterizedTest
	@EnumSource(StatusEnum.class)
	default void cancelRequest_cancelsJob_whenNotFinalState(StatusEnum theState) {
		// given
		JobInstance cancelledInstance = createInstance();
		cancelledInstance.setStatus(theState);
		String instanceId1 = getTestManager().getSvc().storeNewInstance(cancelledInstance);
		getTestManager().getSvc().cancelInstance(instanceId1);

		JobInstance normalInstance = createInstance();
		normalInstance.setStatus(theState);
		String instanceId2 = getTestManager().getSvc().storeNewInstance(normalInstance);

		JobDefinitionRegistry jobDefinitionRegistry = new JobDefinitionRegistry();
		jobDefinitionRegistry.addJobDefinitionIfNotRegistered(getTestManager().withJobDefinition(false));

		// when
		getTestManager().runInTransaction(()-> {
			new JobInstanceProcessor(
				getTestManager().getSvc(),
				null,
				instanceId1,
				new JobChunkProgressAccumulator(),
				null,
				jobDefinitionRegistry
			).process();
		});

		// then
		JobInstance freshInstance1 = getTestManager().getSvc().fetchInstance(instanceId1).orElseThrow();
		if (theState.isCancellable()) {
			assertEquals(StatusEnum.CANCELLED, freshInstance1.getStatus(), "cancel request processed");
			assertThat(freshInstance1.getErrorMessage()).contains("Job instance cancelled");
		} else {
			assertEquals(theState, freshInstance1.getStatus(), "cancel request ignored - state unchanged");
			assertNull(freshInstance1.getErrorMessage(), "no error message");
		}
		JobInstance freshInstance2 = getTestManager().getSvc().fetchInstance(instanceId2).orElseThrow();
		assertEquals(theState, freshInstance2.getStatus(), "cancel request ignored - cancelled not set");
	}
}
