package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.subscription.match.registry.ActiveSubscription;
import org.hl7.fhir.dstu3.model.Subscription;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SubscriptionRegistryTest extends BaseSubscriptionRegistryTest {

	@Test
	public void updateSubscriptionReusesActiveSubscription() {
		Subscription subscription = createSubscription();
		assertRegistrySize(0);
		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);
		assertRegistrySize(1);
		ActiveSubscription origActiveSubscription = mySubscriptionRegistry.get(SUBSCRIPTION_ID);
		assertThat(origActiveSubscription.getCriteria().getCriteria()).isEqualTo(ORIG_CRITERIA);

		subscription.setCriteria(NEW_CRITERIA);
		assertThat(origActiveSubscription.getCriteria().getCriteria()).isEqualTo(ORIG_CRITERIA);
		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);
		assertRegistrySize(1);
		ActiveSubscription newActiveSubscription = mySubscriptionRegistry.get(SUBSCRIPTION_ID);
		assertThat(newActiveSubscription.getCriteria().getCriteria()).isEqualTo(NEW_CRITERIA);
		// The same object
		assertThat(newActiveSubscription == origActiveSubscription).isTrue();
	}

	@Test
	public void updateSubscriptionDoesntReusesActiveSubscriptionWhenChannelChanges() {
		Subscription subscription = createSubscription();
		assertRegistrySize(0);
		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);
		assertRegistrySize(1);

		ActiveSubscription origActiveSubscription = mySubscriptionRegistry.get(SUBSCRIPTION_ID);
		assertThat(origActiveSubscription.getCriteria().getCriteria()).isEqualTo(ORIG_CRITERIA);

		setChannel(subscription, Subscription.SubscriptionChannelType.EMAIL);

		assertThat(origActiveSubscription.getCriteria().getCriteria()).isEqualTo(ORIG_CRITERIA);
		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);
		assertRegistrySize(1);

		ActiveSubscription newActiveSubscription = mySubscriptionRegistry.get(SUBSCRIPTION_ID);
		// A new object
		assertThat(newActiveSubscription == origActiveSubscription).isFalse();
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
