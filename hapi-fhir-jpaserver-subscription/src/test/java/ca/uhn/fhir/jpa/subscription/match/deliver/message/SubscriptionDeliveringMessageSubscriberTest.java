package ca.uhn.fhir.jpa.subscription.match.deliver.message;

import org.junit.Test;

import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

public class SubscriptionDeliveringMessageSubscriberTest {
	@Test
	public void queueNameTest() throws URISyntaxException {
		assertEquals("my-queue:foo", SubscriptionDeliveringMessageSubscriber.extractQueueNameFromEndpoint("jms:queue:my-queue:foo"));
	}

}
