package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.util.Logs;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
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
		private final String myData;
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

		// when
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID)))
			.thenReturn(Optional.of(JobInstance.fromInstanceId(INSTANCE_ID)));
		when(myJobPersistence.fetchAllWorkChunksIterator(any(), anyBoolean())).thenReturn(Collections.emptyIterator());

		// test
		myDataSink.accept(chunkData);

		// verify
		ArgumentCaptor<JobInstance> instanceCaptor = ArgumentCaptor.forClass(JobInstance.class);
		verify(myJobPersistence)
			.updateInstance(instanceCaptor.capture());

		assertEquals(JsonUtil.serialize(stepData, false), instanceCaptor.getValue().getReport());
	}

	@Test
	public void accept_multipleCalls_firstInWins() {
		// setup
		String data = "data";
		String data2 = "data2";
		WorkChunkData<StepOutputData> firstData = new WorkChunkData<>(new StepOutputData(data));
		WorkChunkData<StepOutputData> secondData = new WorkChunkData<>(new StepOutputData(data2));

		ourLogger.setLevel(Level.ERROR);

		// when
		when(myJobPersistence.fetchInstance(eq(INSTANCE_ID)))
			.thenReturn(Optional.of(JobInstance.fromInstanceId(INSTANCE_ID)));
		when(myJobPersistence.fetchAllWorkChunksIterator(any(), anyBoolean())).thenReturn(Collections.emptyIterator());

		// test
		myDataSink.accept(firstData);
		myDataSink.accept(secondData);

		// verify
		ArgumentCaptor<ILoggingEvent> logCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
		verify(myListAppender).doAppend(logCaptor.capture());
		assertEquals(1, logCaptor.getAllValues().size());
		ILoggingEvent log = logCaptor.getValue();
		assertTrue(log.getFormattedMessage().contains(
			"Report has already been set. Now it is being overwritten. Last in will win!"
		));
	}

	@Test
	public void accept_noInstanceIdFound_throwsJobExecutionFailed() {
		// setup
		String data = "data";
		WorkChunkData<StepOutputData> chunkData = new WorkChunkData<>(new StepOutputData(data));

		// when
		when(myJobPersistence.fetchInstance(anyString()))
			.thenReturn(Optional.empty());

		// test
		try {
			myDataSink.accept(chunkData);
			fail("Expected exception to be thrown");
		} catch (JobExecutionFailedException ex) {
			assertTrue(ex.getMessage().contains("No instance found with Id " + INSTANCE_ID));
		} catch (Exception anyOtherEx) {
			fail(anyOtherEx.getMessage());
		}
	}

}
