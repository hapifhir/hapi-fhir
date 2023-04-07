package ca.uhn.fhir.jpa.topic;

public class SubscriptionTopicRegistry {
	private final ActiveSubscriptionTopicCache myActiveSubscriptionTopicCache = new ActiveSubscriptionTopicCache();

	public int size() {
		return myActiveSubscriptionTopicCache.size();
	}
}
