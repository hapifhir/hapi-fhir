package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.subscription.module.BaseSubscriptionDstu3Test;
import ca.uhn.fhir.jpa.subscription.module.channel.SubscriptionChannelRegistry;
import org.hl7.fhir.dstu3.model.Subscription;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public abstract class BaseSubscriptionRegistryTest extends BaseSubscriptionDstu3Test {
	public static final String SUBSCRIPTION_ID = "1";
	public static final String ORIG_CRITERIA = "Patient?";
	public static final String NEW_CRITERIA = "Observation?";

	@Autowired
	SubscriptionRegistry mySubscriptionRegistry;
	@Autowired
	SubscriptionChannelRegistry mySubscriptionChannelRegistry;

	@After
	public void clearRegistryAfter() {
		mySubscriptionRegistry.unregisterAllSubscriptions();
		assertRegistrySize(0);
	}

	protected Subscription createSubscription() {
		Subscription subscription = new Subscription();
		subscription.setId(SUBSCRIPTION_ID);
		subscription.setCriteria(ORIG_CRITERIA);
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		setChannel(subscription, Subscription.SubscriptionChannelType.RESTHOOK);
		return subscription;
	}

	protected void setChannel(Subscription theSubscription, Subscription.SubscriptionChannelType theResthook) {
		Subscription.SubscriptionChannelComponent channel = new Subscription.SubscriptionChannelComponent();
		channel.setType(theResthook);
		channel.setPayload("application/json");
		channel.setEndpoint("http://unused.test.endpoint/");
		theSubscription.setChannel(channel);
	}

	protected void assertRegistrySize(int theSize) {
		assertRegistrySize(theSize, theSize);
	}

	protected void assertRegistrySize(int theSubscriptionRegistrySize, int theSubscriptionChannelRegistrySize) {
		assertEquals(theSubscriptionRegistrySize, mySubscriptionRegistry.size());
		assertEquals(theSubscriptionChannelRegistrySize, mySubscriptionChannelRegistry.size());

	}
}
