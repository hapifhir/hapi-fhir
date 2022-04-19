package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Nonnull;
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

	@Mock
	protected IJobStepWorker<TestJobParameters, VoidModel, TestJobStep2InputType> myStep1Worker;
	@Mock
	protected IJobStepWorker<TestJobParameters, TestJobStep2InputType, TestJobStep3InputType> myStep2Worker;
	@Mock
	protected IJobStepWorker<TestJobParameters, TestJobStep3InputType, VoidModel> myStep3Worker;

	@Nonnull
	static JobInstance createInstance() {
		JobInstance instance = new JobInstance();
		instance.setInstanceId(INSTANCE_ID);
		instance.setStatus(StatusEnum.IN_PROGRESS);
		instance.setJobDefinitionId(JOB_DEFINITION_ID);
		instance.setJobDefinitionVersion(1);
		instance.setParameters(new TestJobParameters()
			.setParam1(PARAM_1_VALUE)
			.setParam2(PARAM_2_VALUE)
			.setPassword(PASSWORD_VALUE)
		);
		return instance;
	}

	@SafeVarargs
	final JobDefinition<TestJobParameters> createJobDefinition(Consumer<JobDefinition.Builder<TestJobParameters, ?>>... theModifiers) {
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

}
