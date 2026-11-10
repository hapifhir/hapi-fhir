package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.DelayChunkException;
import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.api.IJobStepExecutionServices;
import ca.uhn.fhir.batch2.api.IReductionStepExecutorService;
import ca.uhn.fhir.batch2.channel.BatchJobSender;
import ca.uhn.fhir.batch2.maintenance.WorkChunkHeartbeatService;
import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.batch2.model.WorkChunk;
import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.util.Logs;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.transaction.support.TransactionCallback;

import java.time.Duration;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static ca.uhn.fhir.batch2.coordinator.WorkChannelMessageListener.BatchJobTracingContext;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WorkChannelMessageListenerTest extends BaseBatch2Test {
	@Mock
	private IChannelConsumer<JobWorkNotification> myChannelConsumer;
	@Mock
	private BatchJobSender myBatchJobSender;
	@Mock
	private IJobPersistence myJobInstancePersister;
	@Mock
	private JobDefinitionRegistry myJobDefinitionRegistry;
	@Mock
	private IJobMaintenanceService myJobMaintenanceService;
	@Mock
	private IInterceptorBroadcaster myInterceptorBroadcaster;
	@Mock
	private IInterceptorService myInterceptorService;
	@Mock
	private WorkChunkHeartbeatService myWorkChunkHeartbeatService;
	@Mock
	private IJobStepExecutionServices myJobStepExecutionServices;
	@Mock
	private IReductionStepExecutorService myReductionStepExecutorService;
	private final IHapiTransactionService myTransactionService = new NonTransactionalHapiTransactionService();
	private WorkChunkProcessor jobStepExecutorSvc;

	@Mock
	private Appender<ILoggingEvent> myAppender;
	@Captor
	private ArgumentCaptor<ILoggingEvent> myLoggingEvent;

	@BeforeEach
	public void beforeEach() {
		jobStepExecutorSvc = new WorkChunkProcessor(myJobInstancePersister, myBatchJobSender, new NonTransactionalHapiTransactionService(), myJobStepExecutionServices, myReductionStepExecutorService);
	}

	@Test
	public void testWorkChannelMessageHandlerLogging_containsJobAndBatchIdInLoggingContext() {
		// Setup
		((Logger) Logs.getBatchTroubleshootingLog()).addAppender(myAppender);

		// When
		WorkChannelMessageListener listener = new WorkChannelMessageListener(myJobInstancePersister, myJobDefinitionRegistry, myBatchJobSender, jobStepExecutorSvc, myJobMaintenanceService, myTransactionService, myInterceptorBroadcaster, myInterceptorService, myWorkChunkHeartbeatService,
			myJobStepExecutionServices);
		listener.handleMessage(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_1)));

		// Then
		verify(myAppender, atLeastOnce()).doAppend(myLoggingEvent.capture());
		myLoggingEvent.getAllValues()
			.forEach(event -> {
				Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();
				assertThat(mdcPropertyMap).containsEntry(BatchJobTracingContext.CHUNK_ID, CHUNK_ID);
				assertThat(mdcPropertyMap).containsEntry(BatchJobTracingContext.INSTANCE_ID, INSTANCE_ID);
			});
	}

	@Test
	void handleMessage_redeliveredChunkStillHeartbeating_doesNotHoldDequeueTransactionForAckTimeout() {
		// ackTimeout is how long the broker waits on a consumer before handing its message to another consumer.
		// Each broker defines it its own way. A consumer that receives a chunk already IN_PROGRESS uses it to decide
		// whether a heartbeat is recent (within 2 x ackTimeout) and how long to delay (a third of ackTimeout) before
		// retrying the redelivered chunk.
		Duration ackTimeout = Duration.ofSeconds(2);

		// Setup - a first worker dequeued this chunk and is still processing it: the chunk is IN_PROGRESS and the
		// worker just heartbeated, ie., wrote LAST_HEARTBEAT to the database. While busy with the chunk, that worker
		// has not acknowledged the message. The broker therefore redelivers the chunk's message to another worker
		// through the 'listener'.
		WorkChunk workChunk = JobCoordinatorImplTest.createWorkChunkStep1();
		workChunk.setPreviousStatus(WorkChunkStatusEnum.IN_PROGRESS);
		workChunk.setLastHeartbeat(new Date());
		doReturn(createJobDefinition()).when(myJobDefinitionRegistry).getJobDefinitionOrThrowException(eq(JOB_DEFINITION_ID), eq(1));
		when(myJobInstancePersister.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myJobInstancePersister.onWorkChunkDequeue(eq(CHUNK_ID))).thenReturn(Optional.of(workChunk));

		// Stands in for the database transaction and records how long each one ran. In production, the row lock
		// that onWorkChunkDequeue takes is held until this transaction ends.
		TransactionDurationRecordingService transactionService = new TransactionDurationRecordingService();

		// Broker delivery mechanism
		WorkChannelMessageListener listener = new WorkChannelMessageListener(myJobInstancePersister, myJobDefinitionRegistry, myBatchJobSender, jobStepExecutorSvc, myJobMaintenanceService, transactionService, myInterceptorBroadcaster, myInterceptorService, myWorkChunkHeartbeatService);
		listener.setAckTimeout(ackTimeout);

		// Execute - the redelivered message arrives. Inside one transaction, the listener dequeues the chunk
		// (onWorkChunkDequeue locks its row). It sees the chunk as IN_PROGRESS with a heartbeat newer than
		// 2 x ackTimeout: the conditions to assume that the first worker is still alive and busy processing the chunk.
		// The listener delays the message by a third of ackTimeout and throws 'DelayChunkException', which indicates to the broker
		// that the redelivered message was not processed and needs to be retried.
		Throwable thrown = catchThrowable(() -> listener.handleMessage(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_1))));

		// Verify - DelayChunkException is thrown by the listener on the redelivery because the chunk is IN_PROGRESS and
		// its heartbeat is within 2 x ackTimeout: it assumes that the first worker is still alive and processing it. The
		// exception leaves the redelivered message as not acknowledged and the broker delivers it again later. If the
		// first worker has finished by then, the chunk is COMPLETED and that later delivery is discarded.
		assertThat(thrown).isInstanceOf(DelayChunkException.class);

		// Verify - the listener processing the redelivered message dequeues the chunk within a transaction, acquiring a
		// row lock. Analysing the chunk, it determines that another worker is currently processing the chunk and delays
		// the message by a third of ackTimeout. While delaying, it must not hold the row lock: that would prevent the
		// first worker from writing its heartbeat or its completion to the chunk row.
		// We assert that the longest transaction is under a third of ackTimeout. On this path the listener opens a
		// single transaction, the one that dequeues the chunk and holds its row lock. The mocked calls take a few
		// milliseconds, while a transaction that includes the delay lasts at least a third of ackTimeout.
		assertThat(transactionService.getLongestTransaction()).isLessThan(ackTimeout.dividedBy(3));
	}

	@Test
	void handleMessage_redeliveredChunkStillHeartbeating_delaysForAThirdOfAckTimeout() {
		// Setup
		Duration ackTimeout = Duration.ofSeconds(3);
		mockChunkStillProcessedByAnotherWorker();
		WorkChannelMessageListener listener = new WorkChannelMessageListener(myJobInstancePersister, myJobDefinitionRegistry, myBatchJobSender, jobStepExecutorSvc, myJobMaintenanceService, myTransactionService, myInterceptorBroadcaster, myInterceptorService, myWorkChunkHeartbeatService);
		listener.setAckTimeout(ackTimeout);

		// Execute
		long start = System.nanoTime();
		Throwable thrown = catchThrowable(() -> listener.handleMessage(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_1))));
		Duration elapsed = Duration.ofNanos(System.nanoTime() - start);

		// Verify - a third of ackTimeout fits two retries within ackTimeout. When the other worker completes the chunk
		// in that time, a retry discards the message and this consumer meets its broker deadline. The upper bound
		// leaves a full second for the rest of the message handling.
		assertThat(thrown).isInstanceOf(DelayChunkException.class);
		assertThat(elapsed).isGreaterThanOrEqualTo(ackTimeout.dividedBy(3));
		assertThat(elapsed).isLessThan(ackTimeout.multipliedBy(2).dividedBy(3));
	}

	/**
	 * The chunk is IN_PROGRESS and its worker just heartbeated: another worker is still processing it.
	 */
	private void mockChunkStillProcessedByAnotherWorker() {
		WorkChunk workChunk = JobCoordinatorImplTest.createWorkChunkStep1();
		workChunk.setPreviousStatus(WorkChunkStatusEnum.IN_PROGRESS);
		workChunk.setLastHeartbeat(new Date());
		doReturn(createJobDefinition()).when(myJobDefinitionRegistry).getJobDefinitionOrThrowException(eq(JOB_DEFINITION_ID), eq(1));
		when(myJobInstancePersister.fetchInstance(eq(INSTANCE_ID))).thenReturn(Optional.of(createInstance()));
		when(myJobInstancePersister.onWorkChunkDequeue(eq(CHUNK_ID))).thenReturn(Optional.of(workChunk));
	}

	@Nonnull
	private JobWorkNotification createWorkNotification(String theStepId) {
		JobWorkNotification payload = new JobWorkNotification();
		payload.setJobDefinitionId(JOB_DEFINITION_ID);
		payload.setJobDefinitionVersion(1);
		payload.setInstanceId(INSTANCE_ID);
		payload.setChunkId(BaseBatch2Test.CHUNK_ID);
		payload.setTargetStepId(theStepId);
		return payload;
	}

	/**
	 * Records how long the longest transaction callback ran for.
	 */
	private static class TransactionDurationRecordingService extends NonTransactionalHapiTransactionService {
		private Duration myLongestTransaction = Duration.ZERO;

		@Override
		protected <T> T doExecute(ExecutionBuilder theExecutionBuilder, TransactionCallback<T> theCallback) {
			long start = System.nanoTime();
			try {
				return super.doExecute(theExecutionBuilder, theCallback);
			} finally {
				Duration duration = Duration.ofNanos(System.nanoTime() - start);
				if (duration.compareTo(myLongestTransaction) > 0) {
					myLongestTransaction = duration;
				}
			}
		}

		Duration getLongestTransaction() {
			return myLongestTransaction;
		}
	}
}
