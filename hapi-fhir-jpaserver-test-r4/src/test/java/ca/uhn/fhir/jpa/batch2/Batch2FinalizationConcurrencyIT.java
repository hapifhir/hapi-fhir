package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.*;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.Batch2JobHelper;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobParameters;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobStep2InputType;
import ca.uhn.hapi.fhir.batch2.test.support.TestJobStep3InputType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static ca.uhn.fhir.jpa.batch2.Batch2CoordinatorIT.*;
import static org.assertj.core.api.Assertions.assertThat;

public class Batch2FinalizationConcurrencyIT extends BaseJpaR4Test {
	private static final String CONCURRENT_FINAL_TEST = "CONCURRENT_FINAL_TEST";
	@Autowired
	JobDefinitionRegistry myJobDefinitionRegistry;
	@Autowired
	Batch2JobHelper myBatch2JobHelper;
	private JobDefinition<TestJobParameters> myJobDefinition;
	private final List<Integer> myCounters = Collections.synchronizedList(new ArrayList<>());

	@BeforeEach
	public void before() {
		if (myJobDefinition == null) {
			myJobDefinition = buildJobDefinition();
			myJobDefinitionRegistry.addJobDefinition(myJobDefinition);
		}
	}

	@Test
	public void testTwoRuns() {
		JobInstanceStartRequest startRequest1 = new JobInstanceStartRequest(CONCURRENT_FINAL_TEST, new TestJobParameters().setParam1("a").setParam2("100000"));
		JobInstanceStartRequest startRequest2 = new JobInstanceStartRequest(CONCURRENT_FINAL_TEST, new TestJobParameters().setParam1("b").setParam2("200000"));

		myJobCoordinator.startInstance(mySrd, startRequest1);
		myJobCoordinator.startInstance(mySrd, startRequest2);
		myBatch2JobHelper.awaitAllJobsOfJobDefinitionIdToComplete(CONCURRENT_FINAL_TEST);

		assertThat(myCounters).containsExactly(1, 1);
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
			.addFinalReducerStep(LAST_STEP_ID, "reduction step", TestResults.class, new TestReducer());
		return builder.build();
	}

	private class TestReducer implements IReductionStepWorker<TestJobParameters, TestJobStep3InputType, TestResults> {
		AtomicInteger counter = new AtomicInteger();

		@Nonnull
		@Override
		public ChunkOutcome consume(ChunkExecutionDetails<TestJobParameters, TestJobStep3InputType> theChunkDetails) {
			return ChunkOutcome.SUCCESS();
		}

		@Nonnull
		@Override
		public RunOutcome run(@Nonnull StepExecutionDetails<TestJobParameters, TestJobStep3InputType> theStepExecutionDetails, @Nonnull IJobDataSink<TestResults> theDataSink) throws JobExecutionFailedException {
			counter.incrementAndGet();
			TestResults results = new TestResults("count: "+ counter.get());
			myCounters.add(counter.get());
			theDataSink.accept(results);
			return RunOutcome.SUCCESS;
		}
	}

	private class TestResults implements IModelJson {
		@JsonProperty("message")
        private String myMessage;

        public TestResults(String theMessage) {
            myMessage = theMessage;
        }

        public String getMessage() {
            return myMessage;
        }

        public void setMessage(String theMessage) {
            myMessage = theMessage;
        }
    }
}
