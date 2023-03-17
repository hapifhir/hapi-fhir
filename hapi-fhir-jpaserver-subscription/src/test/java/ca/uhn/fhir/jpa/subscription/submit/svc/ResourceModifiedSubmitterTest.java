package ca.uhn.fhir.jpa.subscription.submit.svc;

import ca.uhn.fhir.jpa.model.entity.ResourceModifiedEntityPK;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.subscription.api.IResourceModifiedMessagePersistenceSvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.SimpleTransactionStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResourceModifiedSubmitterTest {

	@Mock
	StorageSettings myStorageSettings;
	@Mock
	SubscriptionChannelFactory mySubscriptionChannelFactory;
	@Mock
	IResourceModifiedMessagePersistenceSvc myResourceModifiedMessagePersistenceSvc;
	@Mock
	PlatformTransactionManager myTxManager;
	@InjectMocks
	ResourceModifiedSubmitterSvc myUnitUnderTest;
	@Captor
	ArgumentCaptor<ChannelProducerSettings> myArgumentCaptor;

	@BeforeEach
	public void beforeEach(){
		lenient().when(myStorageSettings.hasSupportedSubscriptionTypes()).thenReturn(true);
	}

	@ParameterizedTest
	@ValueSource(booleans = {false, true})
	public void testMethodStartIfNeeded_withQualifySubscriptionMatchingChannelNameProperty_mayQualifyChannelName(boolean theIsQualifySubMatchingChannelName){
		// given
		boolean expectedResult = theIsQualifySubMatchingChannelName;
		when(myStorageSettings.isQualifySubscriptionMatchingChannelName()).thenReturn(theIsQualifySubMatchingChannelName);

		// when
		myUnitUnderTest.startIfNeeded();

		// then
		ChannelProducerSettings capturedChannelProducerSettings = getCapturedChannelProducerSettings();
		assertThat(capturedChannelProducerSettings.isQualifyChannelName(), is(expectedResult));

	}

	@Test
	public void testMethodProcessResourceModified_withExistingPersistedResourceModifiedMessage_willSucceed(){
		// given
		TransactionStatus capturingTransactionStatus = new SimpleTransactionStatus();
		IChannelProducer producerChannel = mock(IChannelProducer.class);
		when(mySubscriptionChannelFactory.newMatchingSendingChannel(anyString(), any())).thenReturn(producerChannel);
		when(myTxManager.getTransaction(any())).thenReturn(capturingTransactionStatus);

		// a successful deletion implies that the message did exist.
		when(myResourceModifiedMessagePersistenceSvc.deleteByPK(any())).thenReturn(true);
		when(myResourceModifiedMessagePersistenceSvc.inflateResourceModifiedMessageFromPK(any())).thenReturn(new ResourceModifiedMessage());

		// when
		boolean wasProcessed = myUnitUnderTest.processResourceModified(new ResourceModifiedEntityPK());

		// then
		assertThat(wasProcessed, is(Boolean.TRUE));
		assertThat(capturingTransactionStatus.isRollbackOnly(), is(Boolean.FALSE));
		verify(producerChannel, times(1)).send(any());

	}

	@Test
	public void testMethodProcessResourceModified_whenMessageHasWasAlreadyProcess_willSucceed(){
		// given
		TransactionStatus capturingTransactionStatus = new SimpleTransactionStatus();
		IChannelProducer producerChannel = mock(IChannelProducer.class);
		myUnitUnderTest.setMatchingChannel(producerChannel);
		when(myTxManager.getTransaction(any())).thenReturn(capturingTransactionStatus);

		// deletion fails, someone else was faster and processed the message
		when(myResourceModifiedMessagePersistenceSvc.deleteByPK(any())).thenReturn(false);

		// when
		boolean wasProcessed = myUnitUnderTest.processResourceModified(new ResourceModifiedEntityPK());

		// then
		assertThat(wasProcessed, is(Boolean.TRUE));
		assertThat(capturingTransactionStatus.isRollbackOnly(), is(Boolean.FALSE));
		// we do not send a message which was already sent
		verify(producerChannel, times(0)).send(any());

	}

	@Test
	public void testMethodProcessResourceModified_whithErrorOnSending_willRollback(){
		// given
		TransactionStatus capturingTransactionStatus = new SimpleTransactionStatus();
		IChannelProducer producerChannel = mock(IChannelProducer.class);
		when(mySubscriptionChannelFactory.newMatchingSendingChannel(anyString(), any())).thenReturn(producerChannel);
		when(myTxManager.getTransaction(any())).thenReturn(capturingTransactionStatus);
		when(myResourceModifiedMessagePersistenceSvc.inflateResourceModifiedMessageFromPK(any())).thenReturn(new ResourceModifiedMessage());
		when(myResourceModifiedMessagePersistenceSvc.deleteByPK(any())).thenReturn(true);

		// simulate failure writing to the channel
		when(producerChannel.send(any())).thenThrow(new RuntimeException());

		// when
		boolean wasProcessed = myUnitUnderTest.processResourceModified(new ResourceModifiedEntityPK());

		// then
		assertThat(wasProcessed, is(Boolean.FALSE));
		assertThat(capturingTransactionStatus.isRollbackOnly(), is(Boolean.TRUE));

	}

	private ChannelProducerSettings getCapturedChannelProducerSettings(){
		verify(mySubscriptionChannelFactory).newMatchingSendingChannel(anyString(), myArgumentCaptor.capture());
		return myArgumentCaptor.getValue();
	}



}
