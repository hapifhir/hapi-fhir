package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.model.sched.ISchedulerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class JobStepExecutorTest {

	@Mock
	private IJobPersistence myPersistence;
	@Mock
	private JobWorkCursor<VoidModel, VoidModel, VoidModel> myJobWorkCursor;
	@Mock
	private WorkChunkProcessor myWorkChunkProcessor;
	@Mock
	private IJobMaintenanceService myIJobMaintenanceService;
	@Mock
	private JobDefinitionRegistry myJobDefinitionRegistry;
	@Mock
	private IInterceptorService myIInterceptorService;
	@Mock
	private ISchedulerService myISchedulerService;

	@Test
	public void processStep_successfulExecution_closesJob() {
		// setup
		String instanceId = "instance";
		Duration duration = Duration.ofMillis(500);
		AtomicBoolean gate = new AtomicBoolean();

		// outputs
		JobStepExecutorOutput<VoidModel, VoidModel, VoidModel> output = new JobStepExecutorOutput<>(
			false, // we're only actually testing that the heartbeat job is unscheduled
			mock(BaseDataSink.class)
		);

		// inputs
		JobInstance instance = new JobInstance();
		instance.setInstanceId(instanceId);
		WorkChunk workChunk = new WorkChunk();
		workChunk.setId("chunk_id");

		JobStepExecutor<VoidModel, VoidModel, VoidModel> executor = getExecutor(instance, workChunk, duration);

		// when
		doAnswer(args -> {
			Thread.sleep(duration.toMillis() * 2);
			gate.set(true);
			return output;
		}).when(myWorkChunkProcessor).doExecution(myJobWorkCursor, instance, workChunk);

		// test
		executor.processStep();

		await()
			.atMost(2, TimeUnit.SECONDS)
			.until(gate::get);

		// verify
		verify(myISchedulerService).unscheduleLocalJobs(notNull());
	}

	@Test
	public void processStep_unsuccessfulExecution_closesJob() {
		// setup
		String instanceId = "instance";
		Duration duration = Duration.ofMillis(500);

		// outputs
		JobStepExecutorOutput<VoidModel, VoidModel, VoidModel> output = new JobStepExecutorOutput<>(
			false, // we're only actually testing that the heartbeat job is unscheduled
			mock(BaseDataSink.class)
		);

		// inputs
		JobInstance instance = new JobInstance();
		instance.setInstanceId(instanceId);
		WorkChunk workChunk = new WorkChunk();
		workChunk.setId("chunk_id");

		JobStepExecutor<VoidModel, VoidModel, VoidModel> executor = getExecutor(instance, workChunk, duration);

		// when
		doThrow(new RuntimeException("hello world")).when(myWorkChunkProcessor).doExecution(myJobWorkCursor, instance, workChunk);

		// test
		try {
			executor.processStep();
			fail();
		} catch (Exception ex) {
			assertTrue(ex.getMessage().contains("hello"));
		}

		// verify
		verify(myISchedulerService).unscheduleLocalJobs(notNull());
	}

	private JobStepExecutor<VoidModel, VoidModel, VoidModel> getExecutor(JobInstance theInstance, WorkChunk theWo, Duration theDuration) {
		JobStepExecutor<VoidModel, VoidModel, VoidModel> executor = new JobStepExecutor<>(
			myPersistence,
			theInstance,
			theWo,
			myJobWorkCursor,
			myWorkChunkProcessor,
			myIJobMaintenanceService,
			myJobDefinitionRegistry,
			myIInterceptorService,
			myISchedulerService,
			theDuration
		);
		return executor;
	}
}
