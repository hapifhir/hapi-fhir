package ca.uhn.fhir.broker.impl;

import ca.uhn.fhir.broker.api.ChannelConsumerSettings;
import ca.uhn.fhir.broker.api.ChannelProducerSettings;
import ca.uhn.fhir.broker.api.IChannelConsumer;
import ca.uhn.fhir.broker.api.IChannelNamer;
import ca.uhn.fhir.broker.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.impl.LinkedBlockingChannelFactory;
import ca.uhn.fhir.jpa.subscription.channel.impl.RetryPolicyProvider;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LinkedBlockingBrokerClientTest {
	private static final String TEST_CHANNEL_NAME = "LinkedBlockingBrokerClientTest-TestChannel";
	private IChannelNamer myChannelNamer = (theNameComponent, theChannelSettings) -> theNameComponent;;
	private RetryPolicyProvider myRetryPolicyProvider = new RetryPolicyProvider();
	private final LinkedBlockingChannelFactory myLinkedBlockingChannelFactory = new LinkedBlockingChannelFactory(myChannelNamer, myRetryPolicyProvider);
	private final LinkedBlockingBrokerClient myBrokerClient = new LinkedBlockingBrokerClient(myLinkedBlockingChannelFactory);

	@Test
	public void testSendReceiveSync() throws Exception {
		try (IChannelProducer<MyTestMessageType, Boolean> producer = myBrokerClient.getOrCreateProducer(TEST_CHANNEL_NAME, MyTestMessageType.class, new ChannelProducerSettings())) {
			MyTestMessageType message = new MyTestMessageType("Honda", "Civic");
			try (IChannelConsumer<MyTestMessageType> consumer = myBrokerClient.getOrCreateConsumer(TEST_CHANNEL_NAME, MyTestMessageType.class, new ChannelConsumerSettings())) {
				producer.send(message);
				message = (MyTestMessageType) consumer.receive();
				assertEquals("Honda", message.make);
				assertEquals("Civic", message.model);
			}
		}
	}

	private static class MyTestMessageType {
		String make;
		String model;

		public MyTestMessageType(String theModel, String theMake) {
			model = theModel;
			make = theMake;
		}
	}
}
