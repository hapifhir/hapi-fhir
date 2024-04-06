package ca.uhn.hapi.fhir.batch2.test;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.maintenance.JobChunkProgressAccumulator;
import ca.uhn.fhir.batch2.maintenance.JobInstanceProcessor;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public interface IInstanceStateTransitions extends IWorkChunkCommon, WorkChunkTestConstants {
	Logger ourLog = LoggerFactory.getLogger(IInstanceStateTransitions.class);

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
		assertThat(jobInstance.getStatus(), equalTo(StatusEnum.IN_PROGRESS));
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
			assertThat(freshInstance1.getErrorMessage(), containsString("Job instance cancelled"));
		} else {
			assertEquals(theState, freshInstance1.getStatus(), "cancel request ignored - state unchanged");
			assertNull(freshInstance1.getErrorMessage(), "no error message");
		}
		JobInstance freshInstance2 = getTestManager().getSvc().fetchInstance(instanceId2).orElseThrow();
		assertEquals(theState, freshInstance2.getStatus(), "cancel request ignored - cancelled not set");
	}
}
