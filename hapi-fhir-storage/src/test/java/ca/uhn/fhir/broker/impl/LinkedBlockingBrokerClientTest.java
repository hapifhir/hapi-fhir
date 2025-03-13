package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.ChannelConsumerSettings;
import ca.uhn.fhir.broker.api.ChannelProducerSettings;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IChannelNamer;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.broker.api.Message;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.impl.RetryPolicyProvider;
import ca.uhn.test.concurrency.IPointcutLatch;
import ca.uhn.test.concurrency.PointcutLatch;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LinkedBlockingBrokerClientTest {
	private static final String TEST_CHANNEL_NAME = "LinkedBlockingBrokerClientTest-TestChannel";
	private IChannelNamer myChannelNamer = (theNameComponent, theChannelSettings) -> theNameComponent;
	;
	private RetryPolicyProvider myRetryPolicyProvider = new RetryPolicyProvider();
	private final LinkedBlockingChannelFactory myLinkedBlockingChannelFactory = new LinkedBlockingChannelFactory(myChannelNamer, myRetryPolicyProvider);
	private final LinkedBlockingBrokerClient myBrokerClient = new LinkedBlockingBrokerClient(myLinkedBlockingChannelFactory);

	@Test
	public void testSendReceiveSync() throws Exception {
		try (IChannelProducer<MyTestMessageType, Boolean> producer = myBrokerClient.getOrCreateProducer(TEST_CHANNEL_NAME, MyTestMessageType.class, new ChannelProducerSettings())) {
			MyTestMessageType message = new MyTestMessageType("Honda", "Civic");
			MyMessageListener listener = new MyMessageListener();
			try (IChannelConsumer<MyTestMessageType> consumer = myBrokerClient.getOrCreateConsumer(TEST_CHANNEL_NAME, MyTestMessageType.class, listener, new ChannelConsumerSettings())) {
				producer.send(message);
				assertEquals("Honda", message.make);
				assertEquals("Civic", message.model);
			}
		}
	}

	private static class MyTestMessageType {
		String make;
		String model;

		public MyTestMessageType(String theMake, String theModel) {
			make = theMake;
			model = theModel;
		}
	}

	private static class MyMessageListener implements IMessageListener<MyTestMessageType>, IPointcutLatch {
		private final PointcutLatch myLatch = new PointcutLatch("MyMessageListener");

		@Override
		public void received(IChannelConsumer<MyTestMessageType> theChannelConsumer, Message<MyTestMessageType> theMessage) {
			myLatch.call(theMessage);
		}

		@Override
		public void clear() {
			myLatch.clear();
		}

		@Override
		public void setExpectedCount(int count) {
			myLatch.setExpectedCount(count);
		}

		@Override
		public List<HookParams> awaitExpected() throws InterruptedException {
			return myLatch.awaitExpected();
		}
	}
}
