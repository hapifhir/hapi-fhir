package ca.uhn.fhir.batch2.impl;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.util.JsonUtil;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
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
	private ArgumentCaptor<BatchWorkChunk> myBatchWorkChunkCaptor;

	@Test
	public void test_sink_accept() {
		// setup

		IJobStepWorker<TestJobParameters, VoidModel, Step1Output> firstStepWorker = new IJobStepWorker<TestJobParameters, VoidModel, Step1Output>() {
			@NotNull
			@Override
			public RunOutcome run(@NotNull StepExecutionDetails<TestJobParameters, VoidModel> theStepExecutionDetails, @NotNull IJobDataSink<Step1Output> theDataSink) throws JobExecutionFailedException {
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
			fail();
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

		// execute
		// Let's test our first step worker by calling run on it:
		when(myJobPersistence.storeWorkChunk(myBatchWorkChunkCaptor.capture())).thenReturn(CHUNK_ID);
		StepExecutionDetails<TestJobParameters, VoidModel> details = new StepExecutionDetails<>(new TestJobParameters().setParam1("" + PID_COUNT), null, JOB_INSTANCE_ID, CHUNK_ID);
		JobDataSink<Step1Output> sink = new JobDataSink<>(myBatchJobSender, myJobPersistence, JOB_DEF_ID, JOB_DEF_VERSION, job.getSteps().get(1), JOB_INSTANCE_ID, FIRST_STEP_ID, false);

		RunOutcome result = firstStepWorker.run(details, sink);

		// verify
		assertEquals(PID_COUNT, result.getRecordsProcessed());

		// theDataSink.accept(output) called by firstStepWorker above calls two services.  Let's validate them both.

		verify(myBatchJobSender).sendWorkChannelMessage(myJobWorkNotificationCaptor.capture());

		JobWorkNotification notification = myJobWorkNotificationCaptor.getValue();
		assertEquals(JOB_DEF_ID, notification.getJobDefinitionId());
		assertEquals(JOB_INSTANCE_ID, notification.getInstanceId());
		assertEquals(CHUNK_ID, notification.getChunkId());
		assertEquals(JOB_DEF_VERSION, notification.getJobDefinitionVersion());
		assertEquals(LAST_STEP_ID, notification.getTargetStepId());

		BatchWorkChunk batchWorkChunk = myBatchWorkChunkCaptor.getValue();
		assertEquals(JOB_DEF_VERSION, batchWorkChunk.jobDefinitionVersion);
		assertEquals(0, batchWorkChunk.sequence);
		assertEquals(JOB_DEF_ID, batchWorkChunk.jobDefinitionId);
		assertEquals(JOB_INSTANCE_ID, batchWorkChunk.instanceId);
		assertEquals(LAST_STEP_ID, batchWorkChunk.targetStepId);
		Step1Output stepOutput = JsonUtil.deserialize(batchWorkChunk.serializedData, Step1Output.class);
		assertThat(stepOutput.getPids(), hasSize(PID_COUNT));
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
