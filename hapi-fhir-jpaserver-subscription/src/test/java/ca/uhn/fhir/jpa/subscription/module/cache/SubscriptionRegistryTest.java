package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.dstu3.model.Subscription;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubscriptionRegistryTest extends BaseSubscriptionRegistryTest {

	private void testSubscriptionAddingWithExtension(Extension... theRetryExtensions) {
		org.hl7.fhir.r4.model.Subscription subscription = createSubscriptionR4();

		// create retry extension
		org.hl7.fhir.r4.model.Subscription.SubscriptionChannelComponent channel = subscription.getChannel();
		for (Extension ex : theRetryExtensions) {
			channel.addExtension(ex);
		}

		boolean isRegistered = mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(
			subscription
		);

		Assertions.assertTrue(isRegistered);

		assertRegistrySize(1, 1);
	}

	@Test
	@Disabled("This test requires R4 fhircontext to be hooked up")
	public void registerSubscriptionUnlessAlreadyRegistered_withRetryExtensionIncludingDLQ_creates1Subscription2Channels() {

		// create retry extension
		Extension retryExtension = new Extension();
		retryExtension.setUrl(HapiExtensions.EX_RETRY_COUNT);
		retryExtension.setValue(new IntegerType(2));
		Extension dlq = new Extension();
		dlq.setUrl(HapiExtensions.EX_DLQ_PREFIX);
		dlq.setValue(new StringType("dlq"));


		testSubscriptionAddingWithExtension(retryExtension, dlq);
	}

	@Test
	public void updateSubscriptionReusesActiveSubscription() {
		Subscription subscription = createSubscription();
		assertRegistrySize(0);
		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);
		assertRegistrySize(1);
		ActiveSubscription origActiveSubscription = mySubscriptionRegistry.get(SUBSCRIPTION_ID);
		assertEquals(ORIG_CRITERIA, origActiveSubscription.getCriteriaString());

		subscription.setCriteria(NEW_CRITERIA);
		assertEquals(ORIG_CRITERIA, origActiveSubscription.getCriteriaString());
		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);
		assertRegistrySize(1);
		ActiveSubscription newActiveSubscription = mySubscriptionRegistry.get(SUBSCRIPTION_ID);
		assertEquals(NEW_CRITERIA, newActiveSubscription.getCriteriaString());
		// The same object
		assertTrue(newActiveSubscription == origActiveSubscription);
	}

	@Test
	public void updateSubscriptionDoesntReusesActiveSubscriptionWhenChannelChanges() {
		Subscription subscription = createSubscription();
		assertRegistrySize(0);
		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);
		assertRegistrySize(1);

		ActiveSubscription origActiveSubscription = mySubscriptionRegistry.get(SUBSCRIPTION_ID);
		assertEquals(ORIG_CRITERIA, origActiveSubscription.getCriteriaString());

		setChannel(subscription, Subscription.SubscriptionChannelType.EMAIL);

		assertEquals(ORIG_CRITERIA, origActiveSubscription.getCriteriaString());
		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);
		assertRegistrySize(1);

		ActiveSubscription newActiveSubscription = mySubscriptionRegistry.get(SUBSCRIPTION_ID);
		// A new object
		assertFalse(newActiveSubscription == origActiveSubscription);
	}

	@Test
	public void updateRemove() {
		Subscription subscription = createSubscription();
		assertRegistrySize(0);
		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);
		assertRegistrySize(1);
		mySubscriptionRegistry.unregisterSubscriptionIfRegistered(subscription.getId());
		assertRegistrySize(0);
	}

}
