package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.ChannelConsumerSettings;
import ca.uhn.fhir.broker.api.ChannelProducerSettings;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IChannelNamer;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.broker.api.IMessageListener;
import ca.uhn.fhir.broker.api.ISendResult;
import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.impl.RetryPolicyProvider;
import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.messaging.IMessage;
import ca.uhn.test.concurrency.IPointcutLatch;
import ca.uhn.test.concurrency.PointcutLatch;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LegacyBrokerClientTest {
	private static final String TEST_CHANNEL_NAME = "LinkedBlockingBrokerClientTest-TestChannel";
	private static final String TEST_KEY = "LinkedBlockingBrokerClientTest-TestKey";
	private IChannelNamer myChannelNamer = (theNameComponent, theChannelSettings) -> theNameComponent;
	;
	private RetryPolicyProvider myRetryPolicyProvider = new RetryPolicyProvider();
	private final LinkedBlockingChannelFactory myLinkedBlockingChannelFactory = new LinkedBlockingChannelFactory(myChannelNamer, myRetryPolicyProvider);
	private final LegacyBrokerClient myBrokerClient = new LegacyBrokerClient(myChannelNamer);

	@BeforeEach
	public void before() {
		myBrokerClient.setLegacyChannelFactory(myLinkedBlockingChannelFactory);
	}

	// FIXME KHS this changed?
	@Test
	public void testSendReceive() throws Exception {
		IChannelProducer<MyTestMessageValue> producer = myBrokerClient.getOrCreateProducer(TEST_CHANNEL_NAME, MyTestMessage.class, new ChannelProducerSettings());
		MyMessageListener listener = new MyMessageListener();
		try (IChannelConsumer<MyTestMessageValue> consumer = myBrokerClient.getOrCreateConsumer(TEST_CHANNEL_NAME, MyTestMessage.class, listener, new ChannelConsumerSettings())) {
			listener.setExpectedCount(1);
			MyTestMessage message = buildMessage("Honda", "Civic");
			sendMessage(producer, message);
			List<HookParams> result = listener.awaitExpected();
			assertThat(result).hasSize(1);
			MyTestMessage receivedMessage = result.get(0).get(MyTestMessage.class);
			MyTestMessageValue receivedValue = receivedMessage.getPayload();
			assertEquals("Honda", receivedValue.make);
			assertEquals("Civic", receivedValue.model);
		}
	}

	private static void sendMessage(IChannelProducer<MyTestMessageValue> producer, MyTestMessage message) {
		ISendResult result = producer.send(message);
		assertTrue(result.isSuccessful());
	}

	@Test
	public void testSendReceiveTenMessages() throws Exception {
		IChannelProducer<MyTestMessageValue> producer = myBrokerClient.getOrCreateProducer(TEST_CHANNEL_NAME, MyTestMessage.class, new ChannelProducerSettings());
		MyMessageListener listener = new MyMessageListener();
		try (IChannelConsumer<MyTestMessageValue> consumer = myBrokerClient.getOrCreateConsumer(TEST_CHANNEL_NAME, MyTestMessage.class, listener, new ChannelConsumerSettings())) {
			listener.setExpectedCount(10);
			for (int i = 0; i < 10; i++) {
				MyTestMessage message = buildMessage("Honda", "Civic" + i);
				sendMessage(producer, message);
			}

			List<HookParams> result = listener.awaitExpected();
			assertThat(result).hasSize(10);
			MyTestMessage receivedMessage = result.get(5).get(MyTestMessage.class);
			MyTestMessageValue receivedValue = receivedMessage.getPayload();
			assertEquals("Honda", receivedValue.make);
			assertEquals("Civic5", receivedValue.model);
		}
	}

	private static @NotNull MyTestMessage buildMessage(String theMake, String theModel) {
		MyTestMessageValue value = new MyTestMessageValue(theMake, theModel);
		MyTestMessage message = new MyTestMessage(value);
		return message;
	}

	private static class MyTestMessage extends TestMessage<MyTestMessageValue> {
		public MyTestMessage(MyTestMessageValue thePayload) {
			super(thePayload);
		}
	}

	private static class MyTestMessageValue implements IModelJson {
		@JsonProperty
		String make;

		@JsonProperty
		String model;

		public MyTestMessageValue(String theMake, String theModel) {
			make = theMake;
			model = theModel;
		}

		public String getMake() {
			return make;
		}

		public void setMake(String theMake) {
			make = theMake;
		}

		public String getModel() {
			return model;
		}

		public void setModel(String theModel) {
			model = theModel;
		}
	}

	private static class MyMessageListener implements IMessageListener<MyTestMessageValue>, IPointcutLatch {
		private final PointcutLatch myLatch = new PointcutLatch("MyMessageListener");

		@Override
		public void handleMessage(IMessage<MyTestMessageValue> theMessage) {
			myLatch.call(theMessage);
		}

		@Override
		public Class<MyTestMessageValue> getPayloadType() {
			return MyTestMessageValue.class;
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
