package ca.uhn.hapi.fhir.batch2.test.inline;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.coordinator.JobDefinitionRegistry;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.hl7.fhir.r4.model.MeasureReport;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class InlineJobCoordinatorTest {

    private static final String STEP_1 = "create-subject-chunks";
    private static final String STEP_2 = "evaluate-subject-chunk";
    private static final String STEP_3 = "aggregate-subject-chunks";
	private static final String JOB_INSTANCE_ID = "jobInstanceId";
	private static final String JOB_DEFINITION_ID = "job-definition-id";

	@Test
    void testWithStubJobs() {
		final List<MeasureReport> measureReportsPopulatedByBatch = new ArrayList<>();

        final ListMultimap<String, IModelJson> expectedBatchOutput = ImmutableListMultimap.<String, IModelJson>builder()
                .put(STEP_1, new Step1TestOutput(30))
                .put(STEP_1, new Step1TestOutput(31))
                .put(STEP_1, new Step1TestOutput(32))
                .put(STEP_1, new Step1TestOutput(33))
                .put(STEP_1, new Step1TestOutput(34))
                .put(STEP_1, new Step1TestOutput(35))
                .put(STEP_1, new Step1TestOutput(36))
                .put(STEP_1, new Step1TestOutput(37))
                .put(STEP_1, new Step1TestOutput(38))
                .put(STEP_1, new Step1TestOutput(39))
                .put(STEP_1, new Step1TestOutput(40))
                .put(STEP_1, new Step1TestOutput(41))
                .put(STEP_1, new Step1TestOutput(42))
                .put(STEP_1, new Step1TestOutput(43))
                .put(STEP_1, new Step1TestOutput(44))
                .put(STEP_1, new Step1TestOutput(45))
                .put(STEP_1, new Step1TestOutput(46))
                .put(STEP_1, new Step1TestOutput(47))
                .put(STEP_1, new Step1TestOutput(48))
                .put(STEP_1, new Step1TestOutput(49))
                .put(STEP_1, new Step1TestOutput(50))
                .put(STEP_1, new Step1TestOutput(51))
                .put(STEP_1, new Step1TestOutput(52))
                .put(STEP_1, new Step1TestOutput(53))
                .put(STEP_1, new Step1TestOutput(54))
                .put(STEP_1, new Step1TestOutput(55))
                .put(STEP_1, new Step1TestOutput(56))
                .put(STEP_1, new Step1TestOutput(57))
                .put(STEP_1, new Step1TestOutput(58))
                .put(STEP_1, new Step1TestOutput(59))
                .put(STEP_2, new Step2TestOutput("hello30"))
                .put(STEP_2, new Step2TestOutput("hello31"))
                .put(STEP_2, new Step2TestOutput("hello32"))
                .put(STEP_2, new Step2TestOutput("hello33"))
                .put(STEP_2, new Step2TestOutput("hello34"))
                .put(STEP_2, new Step2TestOutput("hello35"))
                .put(STEP_2, new Step2TestOutput("hello36"))
                .put(STEP_2, new Step2TestOutput("hello37"))
                .put(STEP_2, new Step2TestOutput("hello38"))
                .put(STEP_2, new Step2TestOutput("hello39"))
                .put(STEP_2, new Step2TestOutput("hello40"))
                .put(STEP_2, new Step2TestOutput("hello41"))
                .put(STEP_2, new Step2TestOutput("hello42"))
                .put(STEP_2, new Step2TestOutput("hello43"))
                .put(STEP_2, new Step2TestOutput("hello44"))
                .put(STEP_2, new Step2TestOutput("hello45"))
                .put(STEP_2, new Step2TestOutput("hello46"))
                .put(STEP_2, new Step2TestOutput("hello47"))
                .put(STEP_2, new Step2TestOutput("hello48"))
                .put(STEP_2, new Step2TestOutput("hello49"))
                .put(STEP_2, new Step2TestOutput("hello50"))
                .put(STEP_2, new Step2TestOutput("hello51"))
                .put(STEP_2, new Step2TestOutput("hello52"))
                .put(STEP_2, new Step2TestOutput("hello53"))
                .put(STEP_2, new Step2TestOutput("hello54"))
                .put(STEP_2, new Step2TestOutput("hello55"))
                .put(STEP_2, new Step2TestOutput("hello56"))
                .put(STEP_2, new Step2TestOutput("hello57"))
                .put(STEP_2, new Step2TestOutput("hello58"))
                .put(STEP_2, new Step2TestOutput("hello59"))
                .put(STEP_3, new VoidModel())
                .build();

        final JobDefinition<InlineTestJobParams> jobDefinition = JobDefinition.newBuilder()
                .setJobDefinitionId(JOB_DEFINITION_ID)
                .setJobDescription("FHIR Distributed Multi-Measure Evaluation")
                .setJobDefinitionVersion(1)
                .setParametersType(InlineTestJobParams.class)
                // validator
                .gatedExecution()
                .addFirstStep(
                        STEP_1,
                        "Split the set of subjects into work chunks",
                        Step1TestOutput.class, // output of step 1
                        new Step1Runner())
                .addIntermediateStep(
                        STEP_2,
                        "Evaluates each submitted Measure for the subject chunk",
                        Step2TestOutput.class, // output of step 2
                        new Step2Runner())
                .addFinalReducerStep(
                        STEP_3,
                        "Aggregates all the MeasureReports produced by the evaluate-subject-chunk step",
                        VoidModel.class,
                        new Step3Runner(measureReportsPopulatedByBatch))
                .build();

		final InlineJobCoordinator<InlineTestJobParams> testSubject =
			new InlineJobCoordinator<>(jobDefinition, new JobDefinitionRegistry());

		// Some sort of batch service would trigger this:
		final InlineTestJobParams jobParams = new InlineTestJobParams(30);
		final JobInstanceStartRequest jobInstanceStartRequest = new JobInstanceStartRequest().setParameters(jobParams).setJobDefinitionId(JOB_DEFINITION_ID);
		// This is the contract for the tests to pass their own job instance ID:
		final SystemRequestDetails requestDetails = new SystemRequestDetails();
		requestDetails.addHeader(InlineJobCoordinator.JOB_INSTANCE_ID_FOR_TESTING, JOB_INSTANCE_ID);
		testSubject.startInstance(requestDetails, jobInstanceStartRequest);

		// The job has already been triggered, retrieve the results with the job instance ID we passed into the RequestDetails
		final ListMultimap<String, IModelJson> actualBatchOutput = testSubject.runJobAndRetrieveJobRunResults(JOB_INSTANCE_ID);

        assertBatchSteps(expectedBatchOutput, actualBatchOutput);

        assertThat(measureReportsPopulatedByBatch).hasSize(1);

        final MeasureReport measureReport = measureReportsPopulatedByBatch.get(0);
        assertThat(measureReport).isNotNull();

        assertThat(measureReport.getMeasure())
                .isEqualTo(
                        "hello30,hello31,hello32,hello33,hello34,hello35,hello36,hello37,hello38,hello39,hello40,hello41,hello42,hello43,hello44,hello45,hello46,hello47,hello48,hello49,hello50,hello51,hello52,hello53,hello54,hello55,hello56,hello57,hello58,hello59");
    }

    static class Step1TestOutput implements IModelJson {
        private final int myNum;
        // random instant to test step data transformation
        @Nullable
        private final Instant myInstant;

        public Step1TestOutput(int theNum) {
            this(theNum, Instant.now());
        }

        public Step1TestOutput(int theNum, @Nullable Instant theInstant) {
            myNum = theNum;
            myInstant = theInstant;
        }

        public int getNum() {
            return myNum;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Step1TestOutput that = (Step1TestOutput) o;
            return myNum == that.myNum && Objects.equals(myInstant, that.myInstant);
        }

        @Override
        public int hashCode() {
            return Objects.hash(myNum, myInstant);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Step1TestOutput.class.getSimpleName() + "[", "]")
                    .add("num=" + myNum)
                    .add("instant=" + myInstant)
                    .toString();
        }
    }

    static class Step2TestOutput implements IModelJson {
        private final String myText;
        // random instant to test step data transformation
        @Nullable
        private final Instant myInstant;

        public Step2TestOutput(String theText) {
            this(theText, Instant.now());
        }

        public Step2TestOutput(String theText, @Nullable Instant theInstant) {
            myText = theText;
            myInstant = theInstant;
        }

        public String getText() {
            return myText;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            Step2TestOutput that = (Step2TestOutput) o;
            return Objects.equals(myText, that.myText) && Objects.equals(myInstant, that.myInstant);
        }

        @Override
        public int hashCode() {
            return Objects.hash(myText, myInstant);
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Step2TestOutput.class.getSimpleName() + "[", "]")
                    .add("text='" + myText + "'")
                    .add("instant=" + myInstant)
                    .toString();
        }
    }

	private static class InlineTestJobParams implements IModelJson {
		@JsonProperty(value = "baseNum", required = true)
		private int myBaseNum;

		private InlineTestJobParams() {

		}

		public InlineTestJobParams(int theBaseNum) {
			this.myBaseNum = theBaseNum;
		}

		public int getBaseNum() {
			return myBaseNum;
		}

		public void setBaseNum(int baseNum) {
			this.myBaseNum = baseNum;
		}
	}

    private static class Step1Runner implements IJobStepWorker<InlineTestJobParams, VoidModel, Step1TestOutput> {
        @Override
        @Nonnull
        public RunOutcome run(
                @Nonnull StepExecutionDetails<InlineTestJobParams, VoidModel> theStepExecutionDetails,
                @Nonnull IJobDataSink<Step1TestOutput> theDataSink) {

            IntStream.range(0, theStepExecutionDetails.getParameters().getBaseNum())
                    .forEach(num -> theDataSink.accept(new Step1TestOutput(
                            theStepExecutionDetails.getParameters().getBaseNum() + num)));

            return RunOutcome.SUCCESS;
        }
    }

	private static class Step2Runner implements IJobStepWorker<InlineTestJobParams, Step1TestOutput, Step2TestOutput> {
        @Override
        @Nonnull
        public RunOutcome run(
                @Nonnull StepExecutionDetails<InlineTestJobParams, Step1TestOutput> theStepExecutionDetails,
                @Nonnull IJobDataSink<Step2TestOutput> theDataSink) {

            theDataSink.accept(new Step2TestOutput("hello" + theStepExecutionDetails.getData().myNum));

            return new RunOutcome(5);
        }
    }

	private static class Step3Runner implements IReductionStepWorker<InlineTestJobParams, Step2TestOutput, VoidModel> {
		private final List<MeasureReport> myMeasureReports;
        private final List<String> myTexts = new ArrayList<>();

        public Step3Runner(List<MeasureReport> theMeasureReports) {
			myMeasureReports = theMeasureReports;
        }

		@Nonnull
        @Override
        public ChunkOutcome consume(ChunkExecutionDetails<InlineTestJobParams, Step2TestOutput> chunkExecutionDetails) {
            myTexts.add(chunkExecutionDetails.getData().getText());

            return ChunkOutcome.SUCCESS();
        }

        @Nonnull
        @Override
        public RunOutcome run(
                @Nonnull StepExecutionDetails<InlineTestJobParams, Step2TestOutput> stepExecutionDetails,
                @Nonnull IJobDataSink<VoidModel> dataSink)
                throws JobExecutionFailedException {

			String unifiedString = String.join(",", myTexts);
			myMeasureReports.add(new MeasureReport().setMeasure(unifiedString));

            dataSink.accept(new VoidModel());

            return RunOutcome.SUCCESS;
        }
	}

    private static void assertBatchSteps(
            ListMultimap<String, IModelJson> expectedOutputPerStep,
            ListMultimap<String, IModelJson> jobCoordinationResults) {
        for (String expectedEntryKey : expectedOutputPerStep.keys()) {
            final List<IModelJson> expectedResults = expectedOutputPerStep.get(expectedEntryKey);
            final List<IModelJson> actualResults = jobCoordinationResults.get(expectedEntryKey);

            assertThat(actualResults).isNotNull().hasSize(expectedResults.size());

            for (int i = 0; i < expectedResults.size(); i++) {
                final IModelJson expectedResult = expectedResults.get(i);
                final IModelJson actualResult = actualResults.get(i);

                assertThat(actualResult).isNotNull();
                assertThat(expectedResult).isNotNull();

                if (actualResult instanceof VoidModel) {
                    assertThat(actualResult).isInstanceOf(VoidModel.class);
                } else {
                    assertThat(transformStepOutputIfNeeded(actualResult))
                            .isEqualTo(transformStepOutputIfNeeded(expectedResult));
                }
            }
        }
    }

    private static IModelJson transformStepOutputIfNeeded(IModelJson modelJson) {
        if (modelJson instanceof Step1TestOutput step1Output) {
            return new Step1TestOutput(step1Output.getNum(), null);
        }
        if (modelJson instanceof Step2TestOutput step2Output) {
            return new Step2TestOutput(step2Output.getText(), null);
        }
        return modelJson;
    }
}
