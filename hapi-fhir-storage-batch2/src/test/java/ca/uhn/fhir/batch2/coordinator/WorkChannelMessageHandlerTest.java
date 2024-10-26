package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.batch2.api.IJobPersistence;
import ca.uhn.fhir.batch2.channel.BatchJobSender;

import static ca.uhn.fhir.batch2.coordinator.WorkChannelMessageHandler.*;

import ca.uhn.fhir.batch2.model.JobWorkNotification;
import ca.uhn.fhir.batch2.model.JobWorkNotificationJsonMessage;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.dao.tx.NonTransactionalHapiTransactionService;
import ca.uhn.fhir.util.Logs;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import jakarta.annotation.Nonnull;

import java.util.Collection;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import ch.qos.logback.classic.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

class WorkChannelMessageHandlerTest extends BaseBatch2Test {
	@Mock
	private BatchJobSender myBatchJobSender;
	@Mock
	private IJobPersistence myJobInstancePersister;
	@Mock
	private JobDefinitionRegistry myJobDefinitionRegistry;
	@Mock
	private IJobMaintenanceService myJobMaintenanceService;
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
		WorkChannelMessageHandler handler = new WorkChannelMessageHandler(myJobInstancePersister, myJobDefinitionRegistry, myBatchJobSender, jobStepExecutorSvc, myJobMaintenanceService, myTransactionService);
		handler.handleMessage(new JobWorkNotificationJsonMessage(createWorkNotification(STEP_1)));

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
