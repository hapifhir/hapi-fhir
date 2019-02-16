package ca.uhn.fhir.jpa.subscription.module.cache;


import ca.uhn.fhir.jpa.subscription.module.BaseSubscriptionDstu3Test;
import org.hl7.fhir.dstu3.model.Subscription;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class SubscriptionRegistryTest extends BaseSubscriptionDstu3Test {
	public static final String SUBSCRIPTION_ID = "1";
	public static final String ORIG_CRITERIA = "Patient?";
	public static final String NEW_CRITERIA = "Observation?";
	@Autowired
	SubscriptionRegistry mySubscriptionRegistry;

	@Before
	public void clearRegistryBefore() {
		mySubscriptionRegistry.unregisterAllSubscriptions();
	}

	@After
	public void clearRegistryAfter() {
		mySubscriptionRegistry.unregisterAllSubscriptions();
	}

	@Test
	public void updateSubscriptionReusesActiveSubscription() {
		Subscription subscription = createSubscription();
		assertEquals(0, mySubscriptionRegistry.size());
		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);
		assertEquals(1, mySubscriptionRegistry.size());
		ActiveSubscription origActiveSubscription = mySubscriptionRegistry.get(SUBSCRIPTION_ID);
		assertEquals(ORIG_CRITERIA, origActiveSubscription.getCriteriaString());

		subscription.setCriteria(NEW_CRITERIA);
		assertEquals(ORIG_CRITERIA, origActiveSubscription.getCriteriaString());
		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);
		assertEquals(1, mySubscriptionRegistry.size());
		ActiveSubscription newActiveSubscription = mySubscriptionRegistry.get(SUBSCRIPTION_ID);
		assertEquals(NEW_CRITERIA, newActiveSubscription.getCriteriaString());
		// The same object
		assertTrue(newActiveSubscription == origActiveSubscription);
	}

	@Test
	public void updateSubscriptionDoesntReusesActiveSubscriptionWhenChannelChanges() {
		Subscription subscription = createSubscription();
		assertEquals(0, mySubscriptionRegistry.size());
		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);
		assertEquals(1, mySubscriptionRegistry.size());
		ActiveSubscription origActiveSubscription = mySubscriptionRegistry.get(SUBSCRIPTION_ID);
		assertEquals(ORIG_CRITERIA, origActiveSubscription.getCriteriaString());

		setChannel(subscription, Subscription.SubscriptionChannelType.EMAIL);

		assertEquals(ORIG_CRITERIA, origActiveSubscription.getCriteriaString());
		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);
		assertEquals(1, mySubscriptionRegistry.size());
		ActiveSubscription newActiveSubscription = mySubscriptionRegistry.get(SUBSCRIPTION_ID);
		// A new object
		assertFalse(newActiveSubscription == origActiveSubscription);
	}

	private Subscription createSubscription() {
		Subscription subscription = new Subscription();
		subscription.setId(SUBSCRIPTION_ID);
		subscription.setCriteria(ORIG_CRITERIA);
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		setChannel(subscription, Subscription.SubscriptionChannelType.RESTHOOK);
		return subscription;
	}

	private void setChannel(Subscription theSubscription, Subscription.SubscriptionChannelType theResthook) {
		Subscription.SubscriptionChannelComponent channel = new Subscription.SubscriptionChannelComponent();
		channel.setType(theResthook);
		channel.setPayload("application/json");
		channel.setEndpoint("http://unused.test.endpoint/");
		theSubscription.setChannel(channel);
	}
}
