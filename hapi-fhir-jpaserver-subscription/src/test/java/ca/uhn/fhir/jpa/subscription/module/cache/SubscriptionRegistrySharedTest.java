package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.subscription.channel.subscription.ISubscriptionDeliveryChannelNamer;
import ca.uhn.fhir.jpa.subscription.model.CanonicalSubscription;
import org.hl7.fhir.dstu3.model.Subscription;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ContextConfiguration(classes = {
	SubscriptionRegistrySharedTest.SpringConfig.class
})
public class SubscriptionRegistrySharedTest extends BaseSubscriptionRegistryTest {

	private static final String OTHER_ID = "OTHER_ID";

	@Configuration
	public static class SpringConfig {

		@Primary
		@Bean
		ISubscriptionDeliveryChannelNamer subscriptionDeliveryChannelNamer() {
			return new SharedNamer();
		}

		private class SharedNamer implements ISubscriptionDeliveryChannelNamer {
			@Override
			public String nameFromSubscription(CanonicalSubscription theCanonicalSubscription) {
				return "shared";
			}
		}
	}

	@Test
	public void testTwoSubscriptionsOneChannel() {
		Subscription subscription = createSubscription();
		assertRegistrySize(0);
		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(subscription);
		assertRegistrySize(1);
		Subscription otherSubscription = createSubscription();
		otherSubscription.setId(OTHER_ID);
		mySubscriptionRegistry.registerSubscriptionUnlessAlreadyRegistered(otherSubscription);
		assertRegistrySize(2, 1);
	}
}
