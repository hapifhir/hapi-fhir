package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.Logs;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReductionStepDataSinkTest {

	private static final String INSTANCE_ID = "instanceId";
	@Mock
	private JobDefinitionRegistry myJobDefinitionRegistry;

	private static class TestJobParameters implements IModelJson { }

	private static class StepInputData implements IModelJson { }

	private static class StepOutputData implements IModelJson {
		@JsonProperty("data")
		final String myData;
		public StepOutputData(String theData) {
			myData = theData;
		}
	}

	private ReductionStepDataSink<TestJobParameters, StepInputData, StepOutputData> myDataSink;

	@Mock
	private JobWorkCursor<TestJobParameters, StepInputData, StepOutputData> myWorkCursor;

	@Mock
	private JobDefinition<TestJobParameters> myJobDefinition;

	@Mock
	private IJobPersistence myJobPersistence;

	@Mock
	private Appender<ILoggingEvent> myListAppender;

	private Logger ourLogger;


	@BeforeEach
	public void init() {
		when(myJobDefinition.getJobDefinitionId())
			.thenReturn("jobDefinition");
		when(myWorkCursor.getJobDefinition())
			.thenReturn(myJobDefinition);

		myDataSink = new ReductionStepDataSink<>(
			INSTANCE_ID,
			myWorkCursor,
			myJobPersistence,
			myJobDefinitionRegistry);
		ourLogger = (Logger) Logs.getBatchTroubleshootingLog();
		ourLogger.addAppender(myListAppender);
	}

	@Test
	public void accept_validInputSubmittedOnlyOnce_updatesInstanceWithData() {
		// setup
		String data = "data";
		StepOutputData stepData = new StepOutputData(data);
		WorkChunkData<StepOutputData> chunkData = new WorkChunkData<>(stepData);
		@SuppressWarnings("unchecked")
		JobDefinition<IModelJson> jobDefinition = mock(JobDefinition.class);

		// when
		JobInstance instance = JobInstance.fromInstanceId(INSTANCE_ID);
		instance.setStatus(StatusEnum.FINALIZE);
		stubUpdateInstanceCallback(instance);
		when(myJobPersistence.fetchAllWorkChunksIterator(any(), anyBoolean())).thenReturn(Collections.emptyIterator());
		when(myJobPersistence.fetchInstance(INSTANCE_ID)).thenReturn(Optional.of(instance));
		when(myJobDefinitionRegistry.getJobDefinitionOrThrowException(instance)).thenReturn(jobDefinition);

		// test
		myDataSink.accept(chunkData);

		// verify
		assertEquals(JsonUtil.serialize(stepData, false), instance.getReport());
	}

	@Test
	public void accept_multipleCalls_firstInWins() {
		// setup
		String data = "data";
		String data2 = "data2";
		WorkChunkData<StepOutputData> firstData = new WorkChunkData<>(new StepOutputData(data));
		WorkChunkData<StepOutputData> secondData = new WorkChunkData<>(new StepOutputData(data2));
		@SuppressWarnings("unchecked")
		JobDefinition<IModelJson> jobDefinition = mock(JobDefinition.class);

		ourLogger.setLevel(Level.ERROR);

		JobInstance instance = JobInstance.fromInstanceId(INSTANCE_ID);
		instance.setStatus(StatusEnum.FINALIZE);
		when(myJobPersistence.fetchAllWorkChunksIterator(any(), anyBoolean())).thenReturn(Collections.emptyIterator());
		stubUpdateInstanceCallback(instance);
		when(myJobPersistence.fetchInstance(INSTANCE_ID)).thenReturn(Optional.of(instance));
		when(myJobDefinitionRegistry.getJobDefinitionOrThrowException(instance)).thenReturn(jobDefinition);

		// test
		myDataSink.accept(firstData);
		assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() ->
				myDataSink.accept(secondData));

	}

	private void stubUpdateInstanceCallback(JobInstance theJobInstance) {
		when(myJobPersistence.updateInstance(eq(INSTANCE_ID), any())).thenAnswer(call->{
			IJobPersistence.JobInstanceUpdateCallback callback = call.getArgument(1);
			return callback.doUpdate(theJobInstance);
		});
	}

	@Test
	public void accept_noInstanceIdFound_throwsJobExecutionFailed() {
		// setup
		JobInstance jobInstance = mock(JobInstance.class);
		@SuppressWarnings("unchecked")
		JobDefinition<IModelJson> jobDefinition = (JobDefinition<IModelJson>) mock(JobDefinition.class);
		String data = "data";
		WorkChunkData<StepOutputData> chunkData = new WorkChunkData<>(new StepOutputData(data));
		when(myJobPersistence.updateInstance(any(), any())).thenReturn(false);
		when(myJobPersistence.fetchAllWorkChunksIterator(any(), anyBoolean())).thenReturn(Collections.emptyIterator());
		when(myJobPersistence.fetchInstance(INSTANCE_ID)).thenReturn(Optional.of(jobInstance));
		when(myJobDefinitionRegistry.getJobDefinitionOrThrowException(jobInstance)).thenReturn(jobDefinition);

		// test
		try {
			myDataSink.accept(chunkData);
			fail("Expected exception to be thrown");
		} catch (JobExecutionFailedException ex) {
			assertThat(ex.getMessage()).contains("No instance found with Id " + INSTANCE_ID);
		} catch (Exception anyOtherEx) {
			fail("Unexpected exception", anyOtherEx);
		}
	}
}
