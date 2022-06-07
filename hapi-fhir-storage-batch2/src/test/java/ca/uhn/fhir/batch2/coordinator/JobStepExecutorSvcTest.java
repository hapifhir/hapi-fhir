package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.maintenance.JobChunkProgressAccumulator;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionReductionStep;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.batch2.progress.JobInstanceProgressCalculator;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JobStepExecutorSvcTest {
	private static final String INSTANCE_ID = "instanceId";

	private static class TestJobParameters implements IModelJson {

	}

	private static class StepInputData implements IModelJson {

	}

	private static class StepOutputData implements IModelJson {

	}

	private class TestDataSink extends BaseDataSink<TestJobParameters, StepInputData, StepOutputData> {

		private BaseDataSink<?, ?, ?> myActualDataSink;

		TestDataSink() {
			super(null, null, null);
		}

		public void setDataSink(BaseDataSink<?, ?, ?> theSink) {
			myActualDataSink = theSink;
		}

		@Override
		public void accept(WorkChunkData<StepOutputData> theData) {

		}

		@Override
		public int getWorkChunkCount() {
			return 0;
		}
	}

	private class TestJobStepExecutorSvc extends JobStepExecutorSvc {

		public TestJobStepExecutorSvc(IJobPersistence thePersistence, BatchJobSender theSender) {
			super(thePersistence, theSender);
		}

		@Override
		protected JobInstanceProgressCalculator getProgressCalculator(JobInstance theInstance, JobChunkProgressAccumulator theAccumulator) {
			return myCalculator;
		}

		@Override
		protected  <PT extends IModelJson, IT extends IModelJson, OT extends IModelJson> BaseDataSink<PT, IT, OT> getDataSink(
			JobWorkCursor<PT, IT, OT> theCursor,
			JobDefinition<PT> theJobDefinition,
			String theInstanceId
		) {
			// cause we don't want to test this class here
			myDataSink.setDataSink(super.getDataSink(theCursor, theJobDefinition, theInstanceId));
			return (BaseDataSink<PT, IT, OT>) myDataSink;
		}
	}

	// general mocks
	@Mock
	private JobInstanceProgressCalculator myCalculator;

	@Spy
	private TestDataSink myDataSink = new TestDataSink();

	// class specific mocks
	@Mock
	private IJobPersistence myJobPersistence;

	@Mock
	private BatchJobSender myJobSender;

	private TestJobStepExecutorSvc myExecutorSvc;

	@BeforeEach
	public void init() {
		myExecutorSvc = new TestJobStepExecutorSvc(myJobPersistence, myJobSender);
	}

	@Test
	public void doExecution_reductionStepWithValidInput_executesAsExpected() {
		// setup
		List<String> chunkIds = Arrays.asList("chunk1", "chunk2");
		String newChunkId = "newChunk";
		JobInstance jobInstance = getTestJobInstance();
		JobDefinition<TestJobParameters> jobDefinition = createTestJobDefinition();
		JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData> step = getJobDefinitionStep(
			"stepId",
			true
		);
		JobWorkCursor<TestJobParameters, StepInputData, StepOutputData> stepJobWorker = mock(JobWorkCursor.class);
		JobChunkProgressAccumulator accumulator = mock(JobChunkProgressAccumulator.class);

		// when
		when(stepJobWorker.getCurrentStep())
			.thenReturn(step);
		when(stepJobWorker.getJobDefinition())
			.thenReturn(jobDefinition);
		when(accumulator.getChunkIdsWithStatus(eq(jobInstance.getInstanceId()),
			eq(step.getStepId()),
			any())
		).thenReturn(chunkIds);
		when(myJobPersistence.fetchWorkChunkSetStartTimeAndMarkInProgress(anyString()))
			.thenAnswer(a -> {
				String id = a.getArgument(0);
				return Optional.of(createWorkChunk(id));
			});
		when(myJobPersistence.reduceWorkChunksToSingleChunk(eq(jobInstance.getInstanceId()), anyList(), any(BatchWorkChunk.class)))
			.thenReturn(newChunkId);

		// test
		myExecutorSvc.doExecution(
			stepJobWorker,
			jobInstance,
			null,
			accumulator
		);

		// verify
		assertTrue(myDataSink.myActualDataSink instanceof ReductionStepDataSink);
		verify(myCalculator).calculateAndStoreInstanceProgress();
	}

	private JobInstance getTestJobInstance() {
		JobInstance instance = new JobInstance();
		instance.setInstanceId(INSTANCE_ID);
		instance.setParameters(new TestJobParameters());

		return instance;
	}

	private WorkChunk createWorkChunk(String theId) {
		WorkChunk chunk = new WorkChunk();
		chunk.setInstanceId(INSTANCE_ID);
		chunk.setId(theId);
		chunk.setData(JsonUtil.serialize(
			new StepInputData()
		));
		return chunk;
	}

	private JobDefinition<TestJobParameters> createTestJobDefinition() {
		JobDefinition def = JobDefinition.newBuilder()
			.setJobDefinitionId("jobId")
			.setJobDefinitionVersion(1)
			.setParametersType(TestJobParameters.class)
			.build();
		return def;
	}

	private JobDefinitionStep<TestJobParameters, StepInputData, StepOutputData> getJobDefinitionStep(
		String theId,
		boolean theIsReductionStep
	) {
		if (theIsReductionStep) {
			return new JobDefinitionReductionStep<>(
				theId,
				"i'm a reduction step",
				mock(IReductionStepWorker.class),
				StepInputData.class,
				StepOutputData.class
			);
		} else {
			return new JobDefinitionStep<>(
				theId,
				"i'm a step - many like me, but i'm unique",
				mock(IJobStepWorker.class),
				StepInputData.class,
				StepOutputData.class
			);
		}
	}
}
