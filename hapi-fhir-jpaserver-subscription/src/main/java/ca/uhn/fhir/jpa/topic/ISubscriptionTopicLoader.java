package ca.uhn.fhir.jpa.topic;

import ca.uhn.fhir.jpa.cache.IResourceChangeListener;
import ca.uhn.fhir.rest.server.util.IResourceRepositoryCache;

/**
 * Tag interface for Subscription Topic Loaders
 */
public interface ISubscriptionTopicLoader extends IResourceChangeListener, IResourceRepositoryCache {}
