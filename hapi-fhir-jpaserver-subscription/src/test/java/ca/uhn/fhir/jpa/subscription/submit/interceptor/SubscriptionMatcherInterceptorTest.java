package ca.uhn.fhir.jpa.subscription.submit.interceptor;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hl7.fhir.dstu2.model.Subscription.SubscriptionChannelType.RESTHOOK;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelFactory;
import java.util.Set;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SubscriptionMatcherInterceptorTest {

    @Mock StorageSettings myStorageSettings;
    @Mock SubscriptionChannelFactory mySubscriptionChannelFactory;
    @InjectMocks SubscriptionMatcherInterceptor myUnitUnderTest;
    @Captor ArgumentCaptor<ChannelProducerSettings> myArgumentCaptor;

    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    public void
            testMethodStartIfNeeded_withQualifySubscriptionMatchingChannelNameProperty_mayQualifyChannelName(
                    boolean theIsQualifySubMatchingChannelName) {
        // given
        boolean expectedResult = theIsQualifySubMatchingChannelName;
        when(myStorageSettings.isQualifySubscriptionMatchingChannelName())
                .thenReturn(theIsQualifySubMatchingChannelName);
        when(myStorageSettings.getSupportedSubscriptionTypes()).thenReturn(Set.of(RESTHOOK));

        // when
        myUnitUnderTest.startIfNeeded();

        // then
        ChannelProducerSettings capturedChannelProducerSettings =
                getCapturedChannelProducerSettings();
        assertThat(capturedChannelProducerSettings.isQualifyChannelName(), is(expectedResult));
    }

    private ChannelProducerSettings getCapturedChannelProducerSettings() {
        verify(mySubscriptionChannelFactory)
                .newMatchingSendingChannel(anyString(), myArgumentCaptor.capture());
        return myArgumentCaptor.getValue();
    }
}
