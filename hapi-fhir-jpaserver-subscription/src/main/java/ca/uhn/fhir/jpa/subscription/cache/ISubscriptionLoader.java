package ca.uhn.fhir.jpa.subscription.cache;

public interface ISubscriptionLoader {
	void initSubscriptions();

	int doInitSubscriptions();
}
