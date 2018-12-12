package ca.uhn.fhir.jpa.subscription.module.cache;

public interface ISubscriptionLoader {
	void initSubscriptions();

	int doInitSubscriptions();
}
