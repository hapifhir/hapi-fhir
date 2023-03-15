package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import ca.uhn.fhir.jpa.subscription.submit.svc.ResourceModifiedSubmitterSvc;
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

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hl7.fhir.dstu2.model.Subscription.SubscriptionChannelType.RESTHOOK;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
	IResourceModifiedMessagePersistenceSvc mySubscriptionMessagePersistenceSvc;
	@InjectMocks
	ResourceModifiedSubmitterSvc myUnitUnderTest;
	@Captor
	ArgumentCaptor<ChannelProducerSettings> myArgumentCaptor;

	@BeforeEach
	public void beforeEach(){
		when(myStorageSettings.getSupportedSubscriptionTypes()).thenReturn(Set.of(RESTHOOK));
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
	public void testProcessMessageWithAsynchRetries_willPersistMessage_whenEncounteringIssuesOnChannelSubmission(){
		// given
		IChannelProducer producerChannel = mock(IChannelProducer.class);
		when(mySubscriptionChannelFactory.newMatchingSendingChannel(anyString(), any())).thenReturn(producerChannel);
		when(producerChannel.send(any())).thenThrow(new RuntimeException());

		// when
		myUnitUnderTest.processResourceModified(new ResourceModifiedMessage());

		// then
		verify(mySubscriptionMessagePersistenceSvc, times(1)).persist(any());

	}

	private ChannelProducerSettings getCapturedChannelProducerSettings(){
		verify(mySubscriptionChannelFactory).newMatchingSendingChannel(anyString(), myArgumentCaptor.capture());
		return myArgumentCaptor.getValue();
	}

}
