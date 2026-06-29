package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
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
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReductionStepDataSinkTest {

	private static final String INSTANCE_ID = "instanceId";
	private static final String JOB_DEFINITION_ID = "jobDefinitionId";

	@Mock(strictness = Mock.Strictness.STRICT_STUBS)
	private JobDefinitionRegistry myJobDefinitionRegistry;

	private JobDefinition<MyModel> jobDefinition;
	@Mock
	private IJobStepWorker<MyModel, VoidModel, MyModel> myStep1;
	@Mock
	private IJobStepWorker<MyModel, MyModel, VoidModel> myStep2;


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

	@Spy
	private IInterceptorService myInterceptorService = new InterceptorService("testIS");

	private Logger ourLogger;


	@BeforeEach
	public void init() {
		jobDefinition = JobDefinition
			.newBuilder()
			.setJobDefinitionId(JOB_DEFINITION_ID)
			.setJobDefinitionVersion(1)
			.setJobDescription("A job")
			.setParametersType(MyModel.class)
			.addFirstStep("first-step", "Step", MyModel.class, myStep1)
			.addLastStep("last-step", "Step", myStep2)
				.build();

		when(myJobDefinition.getJobDefinitionId())
			.thenReturn("jobDefinition");
		when(myWorkCursor.getJobDefinition())
			.thenReturn(myJobDefinition);

		myDataSink = new ReductionStepDataSink<>(
			INSTANCE_ID,
			myWorkCursor,
			myJobPersistence,
			myJobDefinitionRegistry,
			myInterceptorService);
		ourLogger = (Logger) Logs.getBatchTroubleshootingLog();
		ourLogger.addAppender(myListAppender);
	}

	@Test
	public void accept_validInputSubmittedOnlyOnce_updatesInstanceWithData_withInterceptor() {
		IAnonymousInterceptor jobCompletionInterceptor = (pointcut, params) -> {
			JobInstance jobInstance = params.get(JobInstance.class);
			assertEquals(INSTANCE_ID, jobInstance.getInstanceId());
		};
		// setup
		String data = "data";
		StepOutputData stepData = new StepOutputData(data);
		WorkChunkData<StepOutputData> chunkData = new WorkChunkData<>(stepData);
		myInterceptorService.registerAnonymousInterceptor(Pointcut.STORAGE_POSTCOMPLETE_BATCH_JOB, jobCompletionInterceptor);

		// when
		JobInstance instance = JobInstance.fromInstanceId(INSTANCE_ID);
		instance.setStatus(StatusEnum.FINALIZE);
		stubUpdateInstanceCallback(instance);
		when(myJobPersistence.fetchAllWorkChunksIterator(any(), anyBoolean())).thenReturn(Collections.emptyIterator());
		when(myJobPersistence.fetchInstance(INSTANCE_ID)).thenReturn(Optional.of(instance));
		@SuppressWarnings("rawtypes")
		JobDefinition jobDefinitionUncast = jobDefinition;
		when(myJobDefinitionRegistry.getJobDefinitionOrThrowException(instance)).thenReturn(jobDefinitionUncast);
		when(myJobDefinitionRegistry.getJobDefinitionOrThrowException(any(), anyInt())).thenReturn(jobDefinitionUncast);

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

		ourLogger.setLevel(Level.ERROR);

		JobInstance instance = JobInstance.fromInstanceId(INSTANCE_ID);
		instance.setStatus(StatusEnum.FINALIZE);
		when(myJobPersistence.fetchAllWorkChunksIterator(any(), anyBoolean())).thenReturn(Collections.emptyIterator());
		stubUpdateInstanceCallback(instance);
		when(myJobPersistence.fetchInstance(INSTANCE_ID)).thenReturn(Optional.of(instance));
		@SuppressWarnings("rawtypes")
		JobDefinition jobDefinitionUncast = jobDefinition;
		when(myJobDefinitionRegistry.getJobDefinitionOrThrowException(instance)).thenReturn(jobDefinitionUncast);
		when(myJobDefinitionRegistry.getJobDefinitionOrThrowException(any(), anyInt())).thenReturn(jobDefinitionUncast);

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

	public static class MyModel implements IModelJson {

	}

}
