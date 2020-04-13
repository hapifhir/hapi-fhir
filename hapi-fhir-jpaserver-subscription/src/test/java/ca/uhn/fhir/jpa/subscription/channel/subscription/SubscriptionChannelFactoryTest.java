package ca.uhn.fhir.jpa.subscription.channel.subscription;

import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.GenericMessage;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SubscriptionChannelFactoryTest {

	private SubscriptionChannelFactory mySvc;

	@Mock
	private ChannelInterceptor myInterceptor;
	@Captor
	private ArgumentCaptor<Exception> myExceptionCaptor;

	@Before
	public void before() {
		mySvc = new SubscriptionChannelFactory(new LinkedBlockingChannelFactory());
	}

	/**
	 * Make sure the channel doesn't silently swallow exceptions
	 */
	@Test
	public void testInterceptorsOnChannelWrapperArePropagated() {

		IChannelReceiver channel = mySvc.newDeliveryReceivingChannel("CHANNEL_NAME", null);
		channel.subscribe(new NpeThrowingHandler());
		channel.addInterceptor(myInterceptor);

		Message<?> input = new GenericMessage<>("TEST");

		when(myInterceptor.preSend(any(),any())).thenAnswer(t->t.getArgument(0, Message.class));

		try {
			channel.send(input);
			fail();
		} catch (MessageDeliveryException e) {
			assertTrue(e.getCause() instanceof NullPointerException);
		}

		verify(myInterceptor, times(1)).afterSendCompletion(any(), any(), anyBoolean(), myExceptionCaptor.capture());

		assertTrue(myExceptionCaptor.getValue() instanceof NullPointerException);
	}


	private class NpeThrowingHandler implements MessageHandler {
		@Override
		public void handleMessage(Message<?> message) throws MessagingException {
			throw new NullPointerException("THIS IS THE MESSAGE");
		}
	}
}
