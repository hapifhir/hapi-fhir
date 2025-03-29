package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.jpa.cache.IResourceChangeListener;

/**
 * Tag interface for Subscription Resource Change Listeners
 */
public interface ISubscriptionTopicLoader extends IResourceChangeListener {
	/**
	 * Update the cache to ensure it contains all the subscription topics currently in the database
	 */
	void syncDatabaseToCache();
}
