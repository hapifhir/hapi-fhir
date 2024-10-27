package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

@ExtendWith(MockitoExtension.class)
public abstract class BaseBatch2Test {

	public static final String JOB_DEFINITION_ID = "JOB_DEFINITION_ID";
	public static final String PARAM_1_VALUE = "PARAM 1 VALUE";
	public static final String PARAM_2_VALUE = "PARAM 2 VALUE";
	public static final String PASSWORD_VALUE = "PASSWORD VALUE";
	public static final String STEP_1 = "STEP_1";
	public static final String STEP_2 = "STEP_2";
	public static final String STEP_3 = "STEP_3";
	public static final String INSTANCE_ID = "INSTANCE-ID";
	public static final String CHUNK_ID = "CHUNK-ID";
	public static final String CHUNK_ID_2 = "CHUNK-ID-2";
	public static final String DATA_1_VALUE = "data 1 value";
	public static final String DATA_2_VALUE = "data 2 value";
	public static final String DATA_3_VALUE = "data 3 value";
	public static final String DATA_4_VALUE = "data 4 value";

	public static final String REDUCTION_JOB_ID = "REDUCTION_JOB_ID";

	@Mock
	protected IJobStepWorker<TestJobParameters, VoidModel, TestJobStep2InputType> myStep1Worker;
	@Mock
	protected IJobStepWorker<TestJobParameters, TestJobStep2InputType, TestJobStep3InputType> myStep2Worker;
	@Mock
	protected IJobStepWorker<TestJobParameters, TestJobStep3InputType, VoidModel> myStep3Worker;

	@Mock
	protected IReductionStepWorker<TestJobParameters, TestJobStep3InputType, TestJobReductionOutputType> myReductionStepWorker;

	@Nonnull
	protected static JobInstance createInstance() {
		return createInstance(JOB_DEFINITION_ID, StatusEnum.IN_PROGRESS);
	}

	static JobInstance createInstance(String theJobId, StatusEnum theStatus) {
		JobInstance instance = JobInstance.fromInstanceId(INSTANCE_ID);
		instance.setStatus(theStatus);
		instance.setJobDefinitionId(theJobId);
		instance.setJobDefinitionVersion(1);
		instance.setParameters(new TestJobParameters()
			.setParam1(PARAM_1_VALUE)
			.setParam2(PARAM_2_VALUE)
			.setPassword(PASSWORD_VALUE)
		);
		return instance;
	}

	@SafeVarargs
	protected final JobDefinition<TestJobParameters> createJobDefinition(Consumer<JobDefinition.Builder<TestJobParameters, ?>>... theModifiers) {
		JobDefinition.Builder<TestJobParameters, VoidModel> builder = JobDefinition
			.newBuilder()
			.setJobDefinitionId(JOB_DEFINITION_ID)
			.setJobDescription("This is a job description")
			.setJobDefinitionVersion(1)
			.setParametersType(TestJobParameters.class)
			.addFirstStep(STEP_1, "Step 1", TestJobStep2InputType.class, myStep1Worker)
			.addIntermediateStep(STEP_2, "Step 2", TestJobStep3InputType.class, myStep2Worker)
			.addLastStep(STEP_3, "Step 3", myStep3Worker);

		for (Consumer<JobDefinition.Builder<TestJobParameters, ?>> next : theModifiers) {
			next.accept(builder);
		}

		return builder.build();
	}

	@SafeVarargs
	protected final JobDefinition<TestJobParameters> createJobDefinitionWithReduction(Consumer<JobDefinition.Builder<TestJobParameters, ?>>... theModifiers) {
		// create a job reduction
		JobDefinition.Builder<TestJobParameters, TestJobReductionOutputType> builder = JobDefinition
			.newBuilder()
			.setJobDefinitionId(REDUCTION_JOB_ID)
			.setJobDescription("Some description")
			.setJobDefinitionVersion(1)
			.gatedExecution()
			.setParametersType(TestJobParameters.class)
			.addFirstStep(STEP_1, "Step 1", TestJobStep2InputType.class, myStep1Worker)
			.addIntermediateStep(STEP_2, "Step 2", TestJobStep3InputType.class, myStep2Worker)
			.addFinalReducerStep(STEP_3, "Step 3", TestJobReductionOutputType.class, myReductionStepWorker);

		for (Consumer<JobDefinition.Builder<TestJobParameters, ?>> next : theModifiers) {
			next.accept(builder);
		}

		return builder.build();
	}

}
