package ca.uhn.fhir.jpa.subscription.match.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.subscription.channel.subscription.ISubscriptionDeliveryChannelNamer;
import ca.uhn.fhir.jpa.subscription.channel.subscription.SubscriptionChannelRegistry;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import ca.uhn.fhir.subscription.SubscriptionTestDataHelper;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Subscription;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class SubscriptionRegistryTest {
	public static final String CHANNEL_NAME = "subscription-test";
	public static final String SUBSCRIPTION_ID = "123";
	static FhirContext ourFhirContext = FhirContext.forR4Cached();

	@Spy
	SubscriptionCanonicalizer mySubscriptionCanonicalizer = new SubscriptionCanonicalizer(ourFhirContext, new StorageSettings());

	@Spy
	ISubscriptionDeliveryChannelNamer mySubscriptionDeliveryChannelNamer = new TestChannelNamer();

	@Mock
	SubscriptionChannelRegistry mySubscriptionChannelRegistry;

	@Mock
	IInterceptorBroadcaster myInterceptorBroadcaster;

	@InjectMocks
	SubscriptionRegistry mySubscriptionRegistry;

	@Test
	public void registerSubscriptionUnlessAlreadyRegistered_subscriptionWithRetry_createsAsExpected() {
		// init
		String channelName = CHANNEL_NAME;
		int retryCount = 2;

		Extension retryExtension = new Extension();
		retryExtension.setUrl(HapiExtensions.EX_RETRY_COUNT);
		retryExtension.setValue(new IntegerType(retryCount));

		Subscription subscription = createSubscription(retryExtension);

		// test
		boolean registered = mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);

		// verify
		assertThat(registered).isTrue();
		ActiveSubscription activeSubscription = mySubscriptionRegistry.get(SUBSCRIPTION_ID);
		assertThat(activeSubscription.getRetryConfigurationParameters()).isNotNull();
		assertThat(activeSubscription.getChannelName()).isEqualTo(channelName);
		assertThat(activeSubscription.getRetryConfigurationParameters().getRetryCount()).isEqualTo(retryCount);
	}

	@Test
	public void registerSubscriptionUnlessAlreadyRegistered_subscriptionWithoutRetry_createsAsExpected() {
		// init
		String channelName = CHANNEL_NAME;

		Subscription subscription = createSubscription();

		// test
		boolean registered = mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);

		// verify
		assertThat(registered).isTrue();
		ActiveSubscription activeSubscription = mySubscriptionRegistry.get(SUBSCRIPTION_ID);
		assertThat(activeSubscription.getRetryConfigurationParameters()).isNull();
	}

	@Test
	public void registerSubscriptionUnlessAlreadyRegistered_subscriptionWithBadRetry_createsAsExpected() {
		// init
		int retryCount = -1; // invalid retry count -> no retries created

		Extension retryExtension = new Extension();
		retryExtension.setUrl(HapiExtensions.EX_RETRY_COUNT);
		retryExtension.setValue(new IntegerType(retryCount));

		Subscription subscription = createSubscription(retryExtension);

		// test
		boolean registered = mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);

		// verify
		assertThat(registered).isTrue();
		ActiveSubscription activeSubscription = mySubscriptionRegistry.get(SUBSCRIPTION_ID);
		assertThat(activeSubscription.getRetryConfigurationParameters()).isNull();
		assertThat(activeSubscription.getChannelName()).isEqualTo(CHANNEL_NAME);
	}

	@Test
	void R4TopicSubscription() {
		// setup
		Subscription topicSubscription1 = SubscriptionTestDataHelper.buildR4TopicSubscription();
		topicSubscription1.setId("topicSubscription1");

		// execute
		boolean registered = mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(topicSubscription1);

		// verify
		assertThat(registered).isTrue();
		List<ActiveSubscription> subscriptions = mySubscriptionRegistry.getTopicSubscriptionsByTopic(SubscriptionTestDataHelper.TEST_TOPIC);
		assertThat(subscriptions).hasSize(1);

		Subscription topicSubscription2 = SubscriptionTestDataHelper.buildR4TopicSubscription();
		topicSubscription2.setId("topicSubscription2");
		registered = mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(topicSubscription2);
		assertThat(registered).isTrue();
		subscriptions = mySubscriptionRegistry.getTopicSubscriptionsByTopic(SubscriptionTestDataHelper.TEST_TOPIC);
		assertThat(subscriptions).hasSize(2);

		// Repeat registration does not register
		Subscription topicSubscription3 = SubscriptionTestDataHelper.buildR4TopicSubscription();
		topicSubscription3.setId("topicSubscription2");
		registered = mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(topicSubscription3);
		assertThat(registered).isFalse();
		assertThat(subscriptions).hasSize(2);

		// Now register a subscription with a different topic
		Subscription topicSubscription4 = SubscriptionTestDataHelper.buildR4TopicSubscription();
		String topicSubscription4Id = "topicSubscription4";
		topicSubscription4.setId(topicSubscription4Id);
		String testTopic4 = "test-topic-4";
		topicSubscription4.setCriteria(testTopic4);
		registered = mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(topicSubscription4);
		assertThat(registered).isTrue();

		// Still 2 subs with the first topic
		subscriptions = mySubscriptionRegistry.getTopicSubscriptionsByTopic(SubscriptionTestDataHelper.TEST_TOPIC);
		assertThat(subscriptions).hasSize(2);

		// Now also 1 sub with a different topic
		subscriptions = mySubscriptionRegistry.getTopicSubscriptionsByTopic(testTopic4);
		assertThat(subscriptions).hasSize(1);
		assertThat(subscriptions.get(0).getId()).isEqualTo(topicSubscription4Id);
	}


	private Subscription createSubscription(Extension... theExtensions) {
		Subscription subscription = new Subscription();
		subscription.setId(SUBSCRIPTION_ID);
		subscription.setCriteria("Patient");
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		Subscription.SubscriptionChannelComponent channel
			= new Subscription.SubscriptionChannelComponent();
		channel.setType(Subscription.SubscriptionChannelType.RESTHOOK);
		channel.setPayload("application/json");
		channel.setEndpoint("http://unused.test.endpoint/");
		subscription.setChannel(channel);

		if (theExtensions != null) {
			for (Extension ex : theExtensions) {
				channel.addExtension(ex);
			}
		}
		return subscription;
	}

	private class TestChannelNamer implements ISubscriptionDeliveryChannelNamer {
		@Override
		public String nameFromSubscription(CanonicalSubscription theCanonicalSubscription) {
			return CHANNEL_NAME;
		}
	}
}
