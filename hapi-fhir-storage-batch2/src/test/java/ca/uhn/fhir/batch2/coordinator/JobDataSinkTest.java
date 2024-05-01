package ca.uhn.fhir.batch2.coordinator;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobDefinitionStep;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.WorkChunkCreateEvent;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JobDataSinkTest {
	private static final String JOB_DEF_ID = "Jeff";
	private static final String JOB_DESC = "Jeff is curious";
	private static final int JOB_DEF_VERSION = 1;
	private static final int PID_COUNT = 729;
	private static final String JOB_INSTANCE_ID = "17";
	private static final String CHUNK_ID = "289";
	public static final String FIRST_STEP_ID = "firstStep";
	public static final String LAST_STEP_ID = "lastStep";

	@Mock
	private BatchJobSender myBatchJobSender;
	@Mock
	private IJobPersistence myJobPersistence;
	@Captor
	private ArgumentCaptor<JobWorkNotification> myJobWorkNotificationCaptor;
	@Captor
	private ArgumentCaptor<WorkChunkCreateEvent> myBatchWorkChunkCaptor;
	private final IHapiTransactionService myHapiTransactionService = new NonTransactionalHapiTransactionService();

	@Test
	public void test_sink_accept() {
		// setup

		IJobStepWorker<TestJobParameters, VoidModel, Step1Output> firstStepWorker = new IJobStepWorker<>() {
			@Nonnull
			@Override
			public RunOutcome run(@Nonnull StepExecutionDetails<TestJobParameters, VoidModel> theStepExecutionDetails, @Nonnull IJobDataSink<Step1Output> theDataSink) throws JobExecutionFailedException {
				TestJobParameters params = theStepExecutionDetails.getParameters();
				int numPidsToGenerate = Integer.parseInt(params.getParam1());
				Step1Output output = new Step1Output();
				for (long i = 0; i < numPidsToGenerate; ++i) {
					output.addPid(i);
				}
				theDataSink.accept(output);
				return new RunOutcome(numPidsToGenerate);
			}
		};

		IJobStepWorker<TestJobParameters, Step1Output, VoidModel> lastStepWorker = (details, sink) -> {
			// Our test does not call this worker
			fail("");
			return null;
		};

		JobDefinition<TestJobParameters> job = JobDefinition.newBuilder()
			.setJobDefinitionId(JOB_DEF_ID)
			.setJobDescription(JOB_DESC)
			.setJobDefinitionVersion(JOB_DEF_VERSION)
			.setParametersType(TestJobParameters.class)
			.addFirstStep(FIRST_STEP_ID, "s1desc", Step1Output.class, firstStepWorker)
			.addLastStep(LAST_STEP_ID, "s2desc", lastStepWorker)
			.build();

		JobDefinitionStep<TestJobParameters, VoidModel, Step1Output> firstStep = (JobDefinitionStep<TestJobParameters, VoidModel, Step1Output>) job.getSteps().get(0);
		JobDefinitionStep<TestJobParameters, Step1Output, VoidModel> lastStep = (JobDefinitionStep<TestJobParameters, Step1Output, VoidModel>) job.getSteps().get(1);

		// execute
		// Let's test our first step worker by calling run on it:
		when(myJobPersistence.onWorkChunkCreate(myBatchWorkChunkCaptor.capture())).thenReturn(CHUNK_ID);
		JobInstance instance = JobInstance.fromInstanceId(JOB_INSTANCE_ID);
		StepExecutionDetails<TestJobParameters, VoidModel> details = new StepExecutionDetails<>(new TestJobParameters().setParam1("" + PID_COUNT), null, instance, CHUNK_ID);
		JobWorkCursor<TestJobParameters, VoidModel, Step1Output> cursor = new JobWorkCursor<>(job, true, firstStep, lastStep);
		JobDataSink<TestJobParameters, VoidModel, Step1Output> sink = new JobDataSink<>(myBatchJobSender, myJobPersistence, job, JOB_INSTANCE_ID, cursor, myHapiTransactionService);

		RunOutcome result = firstStepWorker.run(details, sink);

		// verify
		assertThat(result.getRecordsProcessed()).isEqualTo(PID_COUNT);

		// theDataSink.accept(output) called by firstStepWorker above calls two services.  Let's validate them both.

		verify(myBatchJobSender).sendWorkChannelMessage(myJobWorkNotificationCaptor.capture());

		JobWorkNotification notification = myJobWorkNotificationCaptor.getValue();
		assertThat(notification.getJobDefinitionId()).isEqualTo(JOB_DEF_ID);
		assertThat(notification.getInstanceId()).isEqualTo(JOB_INSTANCE_ID);
		assertThat(notification.getChunkId()).isEqualTo(CHUNK_ID);
		assertThat(notification.getJobDefinitionVersion()).isEqualTo(JOB_DEF_VERSION);
		assertThat(notification.getTargetStepId()).isEqualTo(LAST_STEP_ID);

		WorkChunkCreateEvent batchWorkChunk = myBatchWorkChunkCaptor.getValue();
		assertThat(batchWorkChunk.jobDefinitionVersion).isEqualTo(JOB_DEF_VERSION);
		assertThat(batchWorkChunk.sequence).isEqualTo(0);
		assertThat(batchWorkChunk.jobDefinitionId).isEqualTo(JOB_DEF_ID);
		assertThat(batchWorkChunk.instanceId).isEqualTo(JOB_INSTANCE_ID);
		assertThat(batchWorkChunk.targetStepId).isEqualTo(LAST_STEP_ID);
		assertNotNull(batchWorkChunk.serializedData);
		Step1Output stepOutput = JsonUtil.deserialize(batchWorkChunk.serializedData, Step1Output.class);
		assertThat(stepOutput.getPids()).hasSize(PID_COUNT);
	}

	private static class Step1Output implements IModelJson {
		@JsonProperty("pids")
		private List<Long> myPids;

		public List<Long> getPids() {
			if (myPids == null) {
				myPids = new ArrayList<>();
			}
			return myPids;
		}

		public Step1Output setPids(List<Long> thePids) {
			myPids = thePids;
			return this;
		}

		public void addPid(long thePid) {
			getPids().add(thePid);
		}
	}
}
