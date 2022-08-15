package ca.uhn.fhir.jpa.subscription.module.cache;

import ca.uhn.fhir.jpa.subscription.module.BaseSubscriptionDstu3Test;
import org.hl7.fhir.dstu3.model.Subscription;
import org.junit.jupiter.api.AfterEach;

public abstract class BaseSubscriptionRegistryTest extends BaseSubscriptionDstu3Test {
	public static final String SUBSCRIPTION_ID = "1";
	public static final String ORIG_CRITERIA = "Patient?";
	public static final String NEW_CRITERIA = "Observation?";

	@AfterEach
	public void clearRegistryAfter() {
		super.clearRegistry();
	}

	protected Subscription createSubscription() {
		Subscription subscription = new Subscription();
		subscription.setId(SUBSCRIPTION_ID);
		subscription.setCriteria(ORIG_CRITERIA);
		subscription.setStatus(Subscription.SubscriptionStatus.ACTIVE);
		setChannel(subscription, Subscription.SubscriptionChannelType.RESTHOOK);
		return subscription;
	}

	protected org.hl7.fhir.r4.model.Subscription createSubscriptionR4() {
		org.hl7.fhir.r4.model.Subscription subscription = new org.hl7.fhir.r4.model.Subscription();
		subscription.setId(SUBSCRIPTION_ID);
		subscription.setCriteria(ORIG_CRITERIA);
		subscription.setStatus(org.hl7.fhir.r4.model.Subscription.SubscriptionStatus.ACTIVE);
		org.hl7.fhir.r4.model.Subscription.SubscriptionChannelComponent channel
			= new org.hl7.fhir.r4.model.Subscription.SubscriptionChannelComponent();
		channel.setType(org.hl7.fhir.r4.model.Subscription.SubscriptionChannelType.RESTHOOK);
		channel.setPayload("application/json");
		channel.setEndpoint("http://unused.test.endpoint/");
		subscription.setChannel(channel);
		return subscription;
	}

	protected void setChannel(Subscription theSubscription, Subscription.SubscriptionChannelType theResthook) {
		Subscription.SubscriptionChannelComponent channel = new Subscription.SubscriptionChannelComponent();
		channel.setType(theResthook);
		channel.setPayload("application/json");
		channel.setEndpoint("http://unused.test.endpoint/");
		theSubscription.setChannel(channel);
	}
}
