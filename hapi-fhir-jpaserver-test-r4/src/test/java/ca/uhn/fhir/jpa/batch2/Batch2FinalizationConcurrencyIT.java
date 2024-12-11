package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobParameters;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobStep2InputType;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobStep3InputType;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.atomic.AtomicInteger;

import static ca.uhn.fhir.jpa.batch2.Batch2CoordinatorIT.FIRST_STEP_ID;
import static ca.uhn.fhir.jpa.batch2.Batch2CoordinatorIT.LAST_STEP_ID;
import static ca.uhn.fhir.jpa.batch2.Batch2CoordinatorIT.SECOND_STEP_ID;
import static org.assertj.core.api.Assertions.assertThat;

public class Batch2FinalizationConcurrencyIT extends BaseJpaR4Test {
	private static final String CONCURRENT_FINAL_TEST = "CONCURRENT_FINAL_TEST";
	@Autowired
	JobDefinitionRegistry myJobDefinitionRegistry;
	@Autowired
	Batch2JobHelper myBatch2JobHelper;
	private JobDefinition<TestJobParameters> myJobDefinition;

	@BeforeEach
	public void before() {
		if (myJobDefinition == null) {
			myJobDefinition = buildJobDefinition();
			myJobDefinitionRegistry.addJobDefinition(myJobDefinition);
		}
	}

	@Test
	public void testTwoRuns() {
		TestJobParameters params = new TestJobParameters();
		params.setParam1("foo");
		params.setParam1("bar");
		JobInstanceStartRequest startRequest1 = new JobInstanceStartRequest(CONCURRENT_FINAL_TEST, params.setParam1("a").setParam2("100000"));
		JobInstanceStartRequest startRequest2 = new JobInstanceStartRequest(CONCURRENT_FINAL_TEST, params.setParam1("b").setParam2("200000"));

		Batch2JobStartResponse response1 = myJobCoordinator.startInstance(mySrd, startRequest1);
		Batch2JobStartResponse response2 = myJobCoordinator.startInstance(mySrd, startRequest2);
		myBatch2JobHelper.awaitAllJobsOfJobDefinitionIdToComplete(CONCURRENT_FINAL_TEST);

		TestReducer reductionStep = (TestReducer) myJobDefinition.getStepById(LAST_STEP_ID).getJobStepWorker();
		assertThat(reductionStep.counter).hasValue(1);
	}

	private JobDefinition<TestJobParameters> buildJobDefinition() {
		JobDefinition.Builder<TestJobParameters, ?> builder = JobDefinition.newBuilder()
			.setJobDefinitionId(CONCURRENT_FINAL_TEST)
			.setJobDefinitionVersion(1)
			.gatedExecution()
			.setJobDescription("A job description")
			.setParametersType(TestJobParameters.class)
			.addFirstStep(FIRST_STEP_ID, "the first step", TestJobStep2InputType.class, (theStepExecutionDetails, theDataSink) -> new RunOutcome(0))
			.addIntermediateStep(SECOND_STEP_ID, "the second step", TestJobStep3InputType.class, (theStepExecutionDetails, theDataSink) -> new RunOutcome(0))
			.addFinalReducerStep(LAST_STEP_ID, "reduction step", VoidModel.class, new TestReducer());
		return builder.build();
	}

	private class TestReducer implements IReductionStepWorker<TestJobParameters, TestJobStep3InputType, VoidModel> {
		AtomicInteger counter = new AtomicInteger();

		@Nonnull
		@Override
		public ChunkOutcome consume(ChunkExecutionDetails<TestJobParameters, TestJobStep3InputType> theChunkDetails) {
			return ChunkOutcome.SUCCESS();
		}

		@Nonnull
		@Override
		public RunOutcome run(@Nonnull StepExecutionDetails<TestJobParameters, TestJobStep3InputType> theStepExecutionDetails, @Nonnull IJobDataSink<VoidModel> theDataSink) throws JobExecutionFailedException {
			counter.incrementAndGet();
			return RunOutcome.SUCCESS;
		}
	}
}
