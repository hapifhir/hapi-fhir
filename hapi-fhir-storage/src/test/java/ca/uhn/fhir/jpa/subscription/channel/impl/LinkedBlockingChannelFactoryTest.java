package ca.uhn.fhir.jpa.subscription.channel.impl;

import ca.uhn.fhir.jpa.subscription.channel.api.ChannelConsumerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.ChannelProducerSettings;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelProducer;
import ca.uhn.fhir.jpa.subscription.channel.api.IChannelReceiver;
import ca.uhn.test.concurrency.PointcutLatch;
import jakarta.annotation.Nonnull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LinkedBlockingChannelFactoryTest {
	private static final Logger ourLog = LoggerFactory.getLogger(LinkedBlockingChannelFactoryTest.class);

	private static final String TEST_CHANNEL_NAME = "test-channel-name";
	private static final String TEST_PAYLOAD = "payload";
	LinkedBlockingChannelFactory myChannelFactory = new LinkedBlockingChannelFactory((name, settings) -> name);
	private List<String> myReceivedPayloads;
	private PointcutLatch[] myHandlerCanProceedLatch = {
		new PointcutLatch("first delivery"),
		new PointcutLatch("second delivery")
	};
	@BeforeEach
	public void before() {
		myReceivedPayloads = new ArrayList<>();
	}

	@Test
	void testDeliverOneAtATime() {
		// setup
		AtomicInteger index = new AtomicInteger();
		LinkedBlockingChannel producer = (LinkedBlockingChannel) buildChannels(() -> startProcessingMessage(index.getAndIncrement()));

		// execute
		prepareToHandleMessage(0);
		producer.send(new TestMessage(TEST_PAYLOAD));
		producer.send(new TestMessage(TEST_PAYLOAD));
		producer.send(new TestMessage(TEST_PAYLOAD));

		validateThreeMessagesDelivered(producer);
	}

	private void validateThreeMessagesDelivered(LinkedBlockingChannel producer) {

		// The first send was dequeued but our handler won't deliver it until we unblock it
		await().until(() -> producer.getQueueSizeForUnitTest() == 2);
		// no messages received yet
		assertThat(myReceivedPayloads).hasSize(0);

		// Unblock the first latch so message handling is allowed to proceed
		finishProcessingMessage(0);

		// our queue size should decrement
		await().until(() -> producer.getQueueSizeForUnitTest() == 1);

		// and we should now have received 1 message
		assertThat(myReceivedPayloads).hasSize(1);
		assertEquals(TEST_PAYLOAD, myReceivedPayloads.get(0));

		// Unblock the second latch so message handling is allowed to proceed
		finishProcessingMessage(1);

		// our queue size decrements again
		await().until(() -> producer.getQueueSizeForUnitTest() == 0);

		// and we should now have received 2 messages
		assertThat(myReceivedPayloads).hasSize(2);
		assertEquals(TEST_PAYLOAD, myReceivedPayloads.get(1));
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	@Test
	void testDeliveryResumesAfterFailedMessages() throws InterruptedException {
		// setup
		CountDownLatch successfulProcessedLatch = new CountDownLatch(5);
		LinkedBlockingChannel producer = (LinkedBlockingChannel) buildChannels(failTwiceThenProceed(successfulProcessedLatch));

		// execute
		producer.send(new TestMessage(TEST_PAYLOAD));
		producer.send(new TestMessage(TEST_PAYLOAD));
		producer.send(new TestMessage(TEST_PAYLOAD));
		producer.send(new TestMessage(TEST_PAYLOAD));
		producer.send(new TestMessage(TEST_PAYLOAD));

		// verify
		successfulProcessedLatch.await(20, TimeUnit.SECONDS);
	}

	@Nonnull
	private Runnable failTwiceThenProceed(CountDownLatch theSuccessfulProcessedLatch) {
		AtomicInteger failCounter = new AtomicInteger(0);

		return () -> {
			int value = failCounter.getAndIncrement();

			if (value < 2) {
				// This exception will be thrown the first two times this method is run
				throw new RuntimeException("Expected Exception " + value);
			} else {
				ourLog.info("Successfully processed message");
				theSuccessfulProcessedLatch.countDown();
			}
		};
	}

	private void prepareToHandleMessage(int theIndex) {
		myHandlerCanProceedLatch[theIndex].setExpectedCount(1);
	}

	private void startProcessingMessage(int theIndex) {
		try {
			myHandlerCanProceedLatch[theIndex].awaitExpected();
		} catch (InterruptedException e) {
			ourLog.warn("interrupted", e);
		}
	}

	private void finishProcessingMessage(int theIndex) {
		if (theIndex + 1 < myHandlerCanProceedLatch.length) {
			prepareToHandleMessage(theIndex + 1);
		}
		myHandlerCanProceedLatch[theIndex].call("");
	}

	private IChannelProducer buildChannels(Runnable theCallback) {
		ChannelProducerSettings channelSettings = new ChannelProducerSettings();
		channelSettings.setConcurrentConsumers(1);
		IChannelProducer producer = myChannelFactory.getOrCreateProducer(TEST_CHANNEL_NAME, TestMessage.class, channelSettings);
		IChannelReceiver reciever = myChannelFactory.getOrCreateReceiver(TEST_CHANNEL_NAME, TestMessage.class, new ChannelConsumerSettings());
		reciever.subscribe(msg -> {
			theCallback.run();
			myReceivedPayloads.add((String) msg.getPayload());
		});
		return producer;
	}


	static class TestMessage implements Message<String> {
		private final String payload;

		TestMessage(String thePayload) {
			payload = thePayload;
		}

		@Override
		public String getPayload() {
			return payload;
		}

		@Override
		public MessageHeaders getHeaders() {
			return null;
		}
	}
}
