package ca.uhn.fhir.jpa.subscription.channel.subscription;

import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.GenericMessage;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SubscriptionChannelFactoryTest {

	private SubscriptionChannelFactory mySvc;

	@Mock
	private ChannelInterceptor myInterceptor;
	@Mock
	private IChannelNamer myChannelNamer;
	@Captor
	private ArgumentCaptor<Exception> myExceptionCaptor;

	@BeforeEach
	public void before() {
		when(myChannelNamer.getChannelName(any(), any())).thenReturn("CHANNEL_NAME");
		mySvc = new SubscriptionChannelFactory(new LinkedBlockingChannelFactory(myChannelNamer));
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
