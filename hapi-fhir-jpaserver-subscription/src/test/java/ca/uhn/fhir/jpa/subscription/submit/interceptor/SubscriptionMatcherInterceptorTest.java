package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import org.hl7.fhir.dstu2.model.Subscription;
import org.hl7.fhir.dstu3.model.Bundle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hl7.fhir.dstu2.model.Subscription.SubscriptionChannelType.RESTHOOK;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class SubscriptionMatcherInterceptorTest {

	@Mock
	DaoConfig myDaoConfig;
	@Mock
	SubscriptionChannelFactory mySubscriptionChannelFactory;
	@InjectMocks
	SubscriptionMatcherInterceptor myUnitUnderTest;
	@Captor
	ArgumentCaptor<ChannelProducerSettings> myArgumentCaptor;
	@Test
	public void testMethodStartIfNeeded_withQualifySubscriptionMatchingChannelNamePropertyIsFalse_willNotQualifyChannelName(){
		// given
		when(myDaoConfig.isQualifySubscriptionMatchingChannelName()).thenReturn(false);
		when(myDaoConfig.getSupportedSubscriptionTypes()).thenReturn(Set.of(RESTHOOK));

		// when
		myUnitUnderTest.startIfNeeded();

		// then
		ChannelProducerSettings capturedChannelProducerSettings = getCapturedChannelProducerSettings();
		assertThat(capturedChannelProducerSettings.isQualifyChannelName(), is(false));

	}

	private ChannelProducerSettings getCapturedChannelProducerSettings(){
		verify(mySubscriptionChannelFactory).newMatchingSendingChannel(anyString(), myArgumentCaptor.capture());
		return myArgumentCaptor.getValue();
	}


}
