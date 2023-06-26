package ca.uhn.fhir.jpa.subscription.channel.subscription;

import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedJsonMessage;
import ca.uhn.fhir.jpa.subscription.model.ResourceModifiedMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.MessageDeliveryException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BroadcastingSubscribableChannelWrapperTest {

	@Mock
	private IChannelReceiver myReceiver;

	@Test
	public void testFailIfNoSubscribers() {
		BroadcastingSubscribableChannelWrapper svc = new BroadcastingSubscribableChannelWrapper(myReceiver);

		try {
			svc.send(new ResourceModifiedJsonMessage(new ResourceModifiedMessage()));
		} catch (MessageDeliveryException e) {
			assertThat(e.getMessage(), containsString("Channel has zero subscribers"));
		}
	}


	@Test
	public void testWrappedChannelDestroyed() throws Exception {
		BroadcastingSubscribableChannelWrapper svc = new BroadcastingSubscribableChannelWrapper(myReceiver);

		svc.destroy();

		verify(myReceiver, times(1)).destroy();
	}

}
