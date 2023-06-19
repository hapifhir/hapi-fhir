package ca.uhn.fhir.jpa.subscription.svc;

import ca.uhn.fhir.jpa.dao.tx.IHapiTransactionService;
import ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntity;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.submit.svc.ResourceModifiedSubmitterSvc;
import ca.uhn.fhir.jpa.svc.MockHapiTransactionService;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceModifiedSubmitterSvcTest {

	@Mock
	StorageSettings myStorageSettings;
	@Mock
	SubscriptionChannelFactory mySubscriptionChannelFactory;
	@Mock
	IResourceModifiedMessagePersistenceSvc myResourceModifiedMessagePersistenceSvc;
	@Captor
	ArgumentCaptor<ChannelProducerSettings> myArgumentCaptor;
	@Mock
	IChannelProducer myChannelProducer;

	ResourceModifiedSubmitterSvc myResourceModifiedSubmitterSvc;
	TransactionStatus myCapturingTransactionStatus;

	@BeforeEach
	public void beforeEach(){
		myCapturingTransactionStatus = new SimpleTransactionStatus();
		lenient().when(myStorageSettings.hasSupportedSubscriptionTypes()).thenReturn(true);
		lenient().when(mySubscriptionChannelFactory.newMatchingSendingChannel(anyString(), any())).thenReturn(myChannelProducer);

		IHapiTransactionService hapiTransactionService = new MockHapiTransactionService(myCapturingTransactionStatus);
		myResourceModifiedSubmitterSvc = new ResourceModifiedSubmitterSvc(
			myStorageSettings,
			mySubscriptionChannelFactory,
			myResourceModifiedMessagePersistenceSvc,
			hapiTransactionService);

	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testMethodStartIfNeeded_withQualifySubscriptionMatchingChannelNameProperty_mayQualifyChannelName(boolean theIsQualifySubMatchingChannelName){
		// given
		boolean expectedResult = theIsQualifySubMatchingChannelName;
		when(myStorageSettings.isQualifySubscriptionMatchingChannelName()).thenReturn(theIsQualifySubMatchingChannelName);

		// when
		myResourceModifiedSubmitterSvc.startIfNeeded();

		// then
		ChannelProducerSettings capturedChannelProducerSettings = getCapturedChannelProducerSettings();
		assertThat(capturedChannelProducerSettings.isQualifyChannelName(), is(expectedResult));

	}

	@Test
	public void testSubmitPersisedResourceModifiedMessage_withExistingPersistedResourceModifiedMessage_willSucceed(){
		// given
		// a successful deletion implies that the message did exist.
		when(myResourceModifiedMessagePersistenceSvc.deleteByPK(any())).thenReturn(true);
		when(myResourceModifiedMessagePersistenceSvc.inflatePersistedResourceModifiedMessage(any())).thenReturn(new ResourceModifiedMessage());

		// when
		boolean wasProcessed = myResourceModifiedSubmitterSvc.submitPersisedResourceModifiedMessage(new ResourceModifiedEntity());

		// then
		assertThat(wasProcessed, is(Boolean.TRUE));
		assertThat(myCapturingTransactionStatus.isRollbackOnly(), is(Boolean.FALSE));
		verify(myChannelProducer, times(1)).send(any());

	}

	@Test
	public void testSubmitPersisedResourceModifiedMessage_whenMessageWasAlreadyProcess_willSucceed(){
		// given
		// deletion fails, someone else was faster and processed the message
		when(myResourceModifiedMessagePersistenceSvc.deleteByPK(any())).thenReturn(false);
		when(myResourceModifiedMessagePersistenceSvc.inflatePersistedResourceModifiedMessage(any())).thenReturn(new ResourceModifiedMessage());

		// when
		boolean wasProcessed = myResourceModifiedSubmitterSvc.submitPersisedResourceModifiedMessage(new ResourceModifiedEntity());

		// then
		assertThat(wasProcessed, is(Boolean.TRUE));
		assertThat(myCapturingTransactionStatus.isRollbackOnly(), is(Boolean.FALSE));
		// we do not send a message which was already sent
		verify(myChannelProducer, times(0)).send(any());

	}

	@Test
	public void testSubmitPersisedResourceModifiedMessage_whitErrorOnSending_willRollbackDeletion(){
		// given
		when(myResourceModifiedMessagePersistenceSvc.deleteByPK(any())).thenReturn(true);
		when(myResourceModifiedMessagePersistenceSvc.inflatePersistedResourceModifiedMessage(any())).thenReturn(new ResourceModifiedMessage());

		// simulate failure writing to the channel
		when(myChannelProducer.send(any())).thenThrow(new MessageDeliveryException("sendingError"));

		// when
		boolean wasProcessed = myResourceModifiedSubmitterSvc.submitPersisedResourceModifiedMessage(new ResourceModifiedEntity());

		// then
		assertThat(wasProcessed, is(Boolean.FALSE));
		assertThat(myCapturingTransactionStatus.isRollbackOnly(), is(Boolean.TRUE));

	}

	private ChannelProducerSettings getCapturedChannelProducerSettings(){
		verify(mySubscriptionChannelFactory).newMatchingSendingChannel(anyString(), myArgumentCaptor.capture());
		return myArgumentCaptor.getValue();
	}

}
