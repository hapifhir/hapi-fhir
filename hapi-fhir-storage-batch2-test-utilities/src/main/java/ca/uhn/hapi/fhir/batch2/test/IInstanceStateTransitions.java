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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public interface IInstanceStateTransitions extends IWorkChunkCommon, WorkChunkTestConstants {
	Logger ourLog = LoggerFactory.getLogger(IInstanceStateTransitions.class);

	@Test
	default void createInstance_createsInQueuedWithChunk() {
		// given
		JobDefinition<?> jd = withJobDefinition();

		// when
		IJobPersistence.CreateResult createResult =
			newTxTemplate().execute(status->
				getSvc().onCreateWithFirstChunk(jd, "{}"));

		// then
		ourLog.info("job and chunk created {}", createResult);
		assertNotNull(createResult);
		assertThat(createResult.jobInstanceId, not(emptyString()));
		assertThat(createResult.workChunkId, not(emptyString()));

		JobInstance jobInstance = freshFetchJobInstance(createResult.jobInstanceId);
		assertThat(jobInstance.getStatus(), equalTo(StatusEnum.QUEUED));
		assertThat(jobInstance.getParameters(), equalTo("{}"));

		WorkChunk firstChunk = freshFetchWorkChunk(createResult.workChunkId);
		assertThat(firstChunk.getStatus(), equalTo(WorkChunkStatusEnum.READY));
		assertNull(firstChunk.getData(), "First chunk data is null - only uses parameters");
	}

	@Test
	default void testCreateInstance_firstChunkDequeued_movesToInProgress() {
		// given
		JobDefinition<?> jd = withJobDefinition();
		IJobPersistence.CreateResult createResult = newTxTemplate().execute(status->
			getSvc().onCreateWithFirstChunk(jd, "{}"));
		assertNotNull(createResult);

		// when
		newTxTemplate().execute(status -> getSvc().onChunkDequeued(createResult.jobInstanceId));

		// then
		JobInstance jobInstance = freshFetchJobInstance(createResult.jobInstanceId);
		assertThat(jobInstance.getStatus(), equalTo(StatusEnum.IN_PROGRESS));
	}

	@ParameterizedTest
	@EnumSource(StatusEnum.class)
	default void cancelRequest_cancelsJob_whenNotFinalState(StatusEnum theState) {
		// given
		JobInstance cancelledInstance = createInstance();
		cancelledInstance.setStatus(theState);
		String instanceId1 = getSvc().storeNewInstance(cancelledInstance);
		getSvc().cancelInstance(instanceId1);

		JobInstance normalInstance = createInstance();
		normalInstance.setStatus(theState);
		String instanceId2 = getSvc().storeNewInstance(normalInstance);

		JobDefinitionRegistry jobDefinitionRegistry = new JobDefinitionRegistry();
		jobDefinitionRegistry.addJobDefinitionIfNotRegistered(withJobDefinition());

		// when
		runInTransaction(()-> {
			new JobInstanceProcessor(
				getSvc(),
				null,
				instanceId1,
				new JobChunkProgressAccumulator(),
				null,
				jobDefinitionRegistry,
				getTransactionManager()
			).process();
		});

		// then
		JobInstance freshInstance1 = getSvc().fetchInstance(instanceId1).orElseThrow();
		if (theState.isCancellable()) {
			assertEquals(StatusEnum.CANCELLED, freshInstance1.getStatus(), "cancel request processed");
			assertThat(freshInstance1.getErrorMessage(), containsString("Job instance cancelled"));
		} else {
			assertEquals(theState, freshInstance1.getStatus(), "cancel request ignored - state unchanged");
			assertNull(freshInstance1.getErrorMessage(), "no error message");
		}
		JobInstance freshInstance2 = getSvc().fetchInstance(instanceId2).orElseThrow();
		assertEquals(theState, freshInstance2.getStatus(), "cancel request ignored - cancelled not set");
	}

	default JobInstance createInstance() {
		JobInstance instance = new JobInstance();
		instance.setJobDefinitionId(JOB_DEFINITION_ID);
		instance.setStatus(StatusEnum.QUEUED);
		instance.setJobDefinitionVersion(JOB_DEF_VER);
		instance.setParameters(CHUNK_DATA);
		instance.setReport("TEST");
		return instance;
	}
}
