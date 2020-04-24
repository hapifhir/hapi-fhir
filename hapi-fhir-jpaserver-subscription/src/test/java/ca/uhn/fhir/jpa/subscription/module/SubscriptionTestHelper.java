package ca.uhn.fhir.jpa.subscription.module;

import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.Subscription;

import java.util.concurrent.atomic.AtomicLong;

public class SubscriptionTestHelper {

	protected static final AtomicLong idCounter = new AtomicLong();


	public Subscription makeActiveSubscription(String theCriteria, String thePayload, String theEndpoint) {
		return makeSubscriptionWithStatus(theCriteria, thePayload, theEndpoint, Subscription.SubscriptionStatus.ACTIVE);
	}

	public Subscription makeSubscriptionWithStatus(String theCriteria, String thePayload, String theEndpoint, Subscription.SubscriptionStatus status) {
		Subscription subscription = new Subscription();
		subscription.setReason("Monitor new neonatal function (note, age will be determined by the monitor)");
		subscription.setStatus(status);
		subscription.setCriteria(theCriteria);
		IdType id = new IdType("Subscription", nextId());
		subscription.setId(id);

		Subscription.SubscriptionChannelComponent channel = new Subscription.SubscriptionChannelComponent();
		channel.setType(Subscription.SubscriptionChannelType.RESTHOOK);
		channel.setPayload(thePayload);
		channel.setEndpoint(theEndpoint);
		subscription.setChannel(channel);
		return subscription;
	}

	public long nextId() {
		return idCounter.incrementAndGet();
	}
}
