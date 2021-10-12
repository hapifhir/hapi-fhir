package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import ca.uhn.fhir.util.HapiExtensions;
import org.hl7.fhir.dstu3.model.Extension;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Subscription;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SubscriptionRegistryTest extends BaseSubscriptionRegistryTest {

	private void testSubscriptionAddingWithExtension(Extension theRetryExtension) {
		Subscription subscription = createSubscription();

		// create retry extension
		subscription.addExtension(theRetryExtension);

		boolean isRegistered = mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(
			subscription
		);

		Assertions.assertTrue(isRegistered);

		int channels = 1; // the subscription at least

		for (Extension e : theRetryExtension.getExtension()) {
			if (e.getUrl().equalsIgnoreCase(HapiExtensions.SUB_EXTENSION_DEAD_LETTER_QUEUE)) {
				channels++;
				break;
			}
		}

		assertRegistrySize(1, // 1 subscription
			channels); // channels listening (channel + dlq, if available)
	}

	@Test
	public void registerSubscriptionUnlessAlreadyRegistered_withRetryExtensionIncludingDLQ_creates1Subscription2Channels() {
		// create retry extension
		Extension retryExtension = new Extension();
		retryExtension.setUrl(HapiExtensions.SUB_EXTENSION_RETRY_COUNT);
		IntegerType retryCount = new IntegerType();
		retryCount.setValue(5);
		retryExtension.setValue(retryCount);
		Extension dlqExtension = new Extension();
		dlqExtension.setUrl(HapiExtensions.SUB_EXTENSION_DEAD_LETTER_QUEUE);
		StringType dlqName = new StringType("subscription-dlq");
		dlqExtension.setValue(dlqName);

		Extension extension = new Extension();
		extension.setUrl(HapiExtensions.EXT_RETRY_POLICY);
		extension.addExtension(retryExtension);
		extension.addExtension(dlqExtension);

		testSubscriptionAddingWithExtension(extension);
	}

	@Test
	public void registerSubscriptionUnlessAlreadyRegistered_withRetryExtensionButNoDLQ_createsAsExpected() {
		// create retry extension
		Extension retryExtension = new Extension();
		retryExtension.setUrl(HapiExtensions.SUB_EXTENSION_RETRY_COUNT);
		IntegerType retryCount = new IntegerType();
		retryCount.setValue(5);
		retryExtension.setValue(retryCount);

		Extension extension = new Extension();
		extension.setUrl(HapiExtensions.EXT_RETRY_POLICY);
		extension.addExtension(retryExtension);

		testSubscriptionAddingWithExtension(extension);
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
