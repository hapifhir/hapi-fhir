package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.channel.BatchJobSender;

import static ca.uhn.fhir.batch2.coordinator.WorkChannelMessageListener.*;

import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.util.Logs;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import jakarta.annotation.Nonnull;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import ch.qos.logback.classic.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

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
	private final IHapiTransactionService myTransactionService = new NonTransactionalHapiTransactionService();
	private WorkChunkProcessor jobStepExecutorSvc;

	@Mock
	private Appender<ILoggingEvent> myAppender;
	@Captor
	private ArgumentCaptor<ILoggingEvent> myLoggingEvent;

	@BeforeEach
	public void beforeEach() {
		jobStepExecutorSvc = new WorkChunkProcessor(myJobInstancePersister, myBatchJobSender, new NonTransactionalHapiTransactionService());
	}

	@Test
	public void testWorkChannelMessageHandlerLogging_containsJobAndBatchIdInLoggingContext(){
		// Setup
		((Logger) Logs.getBatchTroubleshootingLog()).addAppender(myAppender);

		// When
		WorkChannelMessageListener listener = new WorkChannelMessageListener(myJobInstancePersister, myJobDefinitionRegistry, myBatchJobSender, jobStepExecutorSvc, myJobMaintenanceService, myTransactionService, myInterceptorBroadcaster);
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
}
