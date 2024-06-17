package ca.uhn.fhir.jpa.subscription.svc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.entity.PersistedResourceModifiedMessageEntityPK;
import ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntity;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.model.config.SubscriptionSettings;
import ca.uhn.fhir.jpa.subscription.submit.svc.ResourceModifiedSubmitterSvc;
import ca.uhn.fhir.jpa.svc.MockHapiTransactionService;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceModifiedSubmitterSvcTest {

	private final ch.qos.logback.classic.Logger ourLogger = (Logger) LoggerFactory.getLogger(ResourceModifiedSubmitterSvc.class);

	@Mock
	SubscriptionSettings mySubscriptionSettings;
	@Mock
	SubscriptionChannelFactory mySubscriptionChannelFactory;
	@Mock
	IResourceModifiedMessagePersistenceSvc myResourceModifiedMessagePersistenceSvc;
	@Captor
	ArgumentCaptor<ChannelProducerSettings> myArgumentCaptor;
	@Mock
	IChannelProducer myChannelProducer;

	@Mock
	ListAppender<ILoggingEvent> myListAppender;

	ResourceModifiedSubmitterSvc myResourceModifiedSubmitterSvc;
	TransactionStatus myCapturingTransactionStatus;

	@BeforeEach
	public void beforeEach(){
		myCapturingTransactionStatus = new SimpleTransactionStatus();
		lenient().when(mySubscriptionSettings.hasSupportedSubscriptionTypes()).thenReturn(true);
		lenient().when(mySubscriptionChannelFactory.newMatchingSendingChannel(anyString(), any())).thenReturn(myChannelProducer);

		IHapiTransactionService hapiTransactionService = new MockHapiTransactionService(myCapturingTransactionStatus);
		myResourceModifiedSubmitterSvc = new ResourceModifiedSubmitterSvc(
			mySubscriptionSettings,
			mySubscriptionChannelFactory,
			myResourceModifiedMessagePersistenceSvc,
			hapiTransactionService);

	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testMethodStartIfNeeded_withQualifySubscriptionMatchingChannelNameProperty_mayQualifyChannelName(boolean theIsQualifySubMatchingChannelName){
		// given
		boolean expectedResult = theIsQualifySubMatchingChannelName;
		when(mySubscriptionSettings.isQualifySubscriptionMatchingChannelName()).thenReturn(theIsQualifySubMatchingChannelName);

		// when
		myResourceModifiedSubmitterSvc.startIfNeeded();

		// then
		ChannelProducerSettings capturedChannelProducerSettings = getCapturedChannelProducerSettings();
		assertEquals(expectedResult, capturedChannelProducerSettings.isQualifyChannelName());

	}

	@Test
	public void testSubmitPersistedResourceModifiedMessage_withExistingPersistedResourceModifiedMessage_willSucceed(){
		// given
		// a successful deletion implies that the message did exist.
		when(myResourceModifiedMessagePersistenceSvc.deleteByPK(any())).thenReturn(true);
		when(myResourceModifiedMessagePersistenceSvc.createResourceModifiedMessageFromEntityWithoutInflation(any())).thenReturn(new ResourceModifiedMessage());

		// when
		boolean wasProcessed = myResourceModifiedSubmitterSvc.submitPersisedResourceModifiedMessage(new ResourceModifiedEntity());

		// then
		assertEquals(Boolean.TRUE, wasProcessed);
		assertEquals(Boolean.FALSE, myCapturingTransactionStatus.isRollbackOnly());
		verify(myChannelProducer, times(1)).send(any());
	}

	@Test
	public void testSubmitPersistedResource_logsDeleteAndInflationExceptions() {
		// setup
		String deleteExMsg = "Delete Exception";
		String inflationExMsg = "Inflation Exception";
		String patientId = "/Patient/123/_history/1";
		ResourceModifiedEntity resourceModified = new ResourceModifiedEntity();
		PersistedResourceModifiedMessageEntityPK rpm = new PersistedResourceModifiedMessageEntityPK();
		rpm.setResourcePid(patientId);
		rpm.setResourceVersion("1");
		resourceModified.setResourceModifiedEntityPK(rpm);

		ourLogger.addAppender(myListAppender);
		ourLogger.setLevel(Level.ERROR);

		// when
		when(myResourceModifiedMessagePersistenceSvc.deleteByPK(any()))
			.thenThrow(new RuntimeException(deleteExMsg));
		when(myResourceModifiedMessagePersistenceSvc.createResourceModifiedMessageFromEntityWithoutInflation(any()))
			.thenThrow(new RuntimeException(inflationExMsg));

		// test
		boolean processed = myResourceModifiedSubmitterSvc.submitPersisedResourceModifiedMessage(resourceModified);

		// verify
		assertTrue(processed);

		ArgumentCaptor<ILoggingEvent> logEvent = ArgumentCaptor.forClass(ILoggingEvent.class);
		verify(myListAppender, atLeast(2))
			.doAppend(logEvent.capture());

		List<ILoggingEvent> logs = logEvent.getAllValues();
		boolean hasDeleteException = false;
		boolean hasInflationException = false;
		for (ILoggingEvent log : logs) {
			if (log.getThrowableProxy().getMessage().contains(deleteExMsg)) {
				hasDeleteException = true;
			}
			if (log.getThrowableProxy().getMessage().contains(inflationExMsg)) {
				hasInflationException = true;
			}
		}
		assertTrue(hasDeleteException);
		assertTrue(hasInflationException);
	}

	@Test
	public void testSubmitPersistedResource_withMissingResource_processes() {
		// setup
		String patientId = "Patient/123";
		String exceptionString = "A random exception";
		ResourceModifiedEntity resourceModified = new ResourceModifiedEntity();
		PersistedResourceModifiedMessageEntityPK rpm = new PersistedResourceModifiedMessageEntityPK();
		rpm.setResourcePid(patientId);
		rpm.setResourceVersion("1");
		resourceModified.setResourceModifiedEntityPK(rpm);
		ResourceModifiedMessage msg = new ResourceModifiedMessage();

		ourLogger.addAppender(myListAppender);
		ourLogger.setLevel(Level.ERROR);

		// when
		when(myResourceModifiedMessagePersistenceSvc.deleteByPK(any()))
			.thenReturn(true);
		when(myResourceModifiedMessagePersistenceSvc.createResourceModifiedMessageFromEntityWithoutInflation(any()))
			.thenReturn(msg);
		when(myChannelProducer.send(any()))
			.thenThrow(new RuntimeException(exceptionString));

		// test
		boolean processed = myResourceModifiedSubmitterSvc.submitPersisedResourceModifiedMessage(resourceModified);

		// then
		assertTrue(processed);

		// verify
		verify(myChannelProducer)
			.send(any());
		ArgumentCaptor<ILoggingEvent> loggingCaptor = ArgumentCaptor.forClass(ILoggingEvent.class);
		verify(myListAppender).doAppend(loggingCaptor.capture());
		ILoggingEvent event = loggingCaptor.getValue();
		assertNotNull(event);
		assertThat(event.getThrowableProxy().getMessage()).contains(exceptionString);
	}

	@Test
	public void testSubmitPersistedResourceModifiedMessage_whenMessageWasAlreadyProcess_willSucceed(){
		// given
		// deletion fails, someone else was faster and processed the message
		when(myResourceModifiedMessagePersistenceSvc.deleteByPK(any())).thenReturn(false);
		when(myResourceModifiedMessagePersistenceSvc.createResourceModifiedMessageFromEntityWithoutInflation(any())).thenReturn(new ResourceModifiedMessage());

		// when
		boolean wasProcessed = myResourceModifiedSubmitterSvc.submitPersisedResourceModifiedMessage(new ResourceModifiedEntity());

		// then
		assertEquals(Boolean.TRUE, wasProcessed);
		assertEquals(Boolean.FALSE, myCapturingTransactionStatus.isRollbackOnly());
		// we do not send a message which was already sent
		verify(myChannelProducer, times(0)).send(any());

	}

	@Test
	public void testSubmitPersistedResourceModifiedMessage_whitErrorOnSending_willRollbackDeletion(){
		// given
		when(myResourceModifiedMessagePersistenceSvc.deleteByPK(any())).thenReturn(true);
		when(myResourceModifiedMessagePersistenceSvc.createResourceModifiedMessageFromEntityWithoutInflation(any())).thenReturn(new ResourceModifiedMessage());

		// simulate failure writing to the channel
		when(myChannelProducer.send(any())).thenThrow(new MessageDeliveryException("sendingError"));

		// when
		boolean wasProcessed = myResourceModifiedSubmitterSvc.submitPersisedResourceModifiedMessage(new ResourceModifiedEntity());

		// then
		assertEquals(Boolean.FALSE, wasProcessed);
		assertEquals(Boolean.TRUE, myCapturingTransactionStatus.isRollbackOnly());

	}

	private ChannelProducerSettings getCapturedChannelProducerSettings(){
		verify(mySubscriptionChannelFactory).newMatchingSendingChannel(anyString(), myArgumentCaptor.capture());
		return myArgumentCaptor.getValue();
	}

}
