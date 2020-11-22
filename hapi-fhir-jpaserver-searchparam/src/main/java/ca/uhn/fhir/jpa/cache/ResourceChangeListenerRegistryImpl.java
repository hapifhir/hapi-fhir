package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryResourceMatcher;
import com.google.common.annotations.VisibleForTesting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResourceChangeListenerRegistryImpl implements IResourceChangeListenerRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceChangeListenerRegistryImpl.class);

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private ResourceChangeListenerCache myResourceChangeListenerCache;
	@Autowired
	private InMemoryResourceMatcher myInMemoryResourceMatcher;

	/**
	 * @param theResourceName           the name of the resource the listener should be notified about
	 * @param theSearchParamMap         the listener will only be notified of changes to resources that match this map
	 * @param theResourceChangeListener the listener to be notified
	 * @param theRemoteRefreshIntervalMs the number of milliseconds between checking the database for changed resources that match the search parameter map
	 * @throws ca.uhn.fhir.parser.DataFormatException      if theResourceName is not valid
	 * @throws IllegalArgumentException if theSearchParamMap cannot be evaluated in-memory
	 * @return RegisteredResourceChangeListener that stores the resource id cache, and the next refresh time
	 */
	@Override
	// FIXME set remote poll interval
	public RegisteredResourceChangeListener registerResourceResourceChangeListener(String theResourceName, SearchParameterMap theSearchParamMap, IResourceChangeListener theResourceChangeListener, long theRemoteRefreshIntervalMs) {
		RuntimeResourceDefinition resourceDef = myFhirContext.getResourceDefinition(theResourceName);
		InMemoryMatchResult inMemoryMatchResult = myInMemoryResourceMatcher.checkIfInMemorySupported(theSearchParamMap, resourceDef);
		if (!inMemoryMatchResult.supported()) {
			throw new IllegalArgumentException("SearchParameterMap " + theSearchParamMap + " cannot be evaluated in-memory: " + inMemoryMatchResult.getUnsupportedReason() + ".  Only search parameter maps that can be evaluated in-memory may be registered.");
		}
		return myResourceChangeListenerCache.add(theResourceName, theResourceChangeListener, theSearchParamMap, theRemoteRefreshIntervalMs);
	}

	@Override
	public void unregisterResourceResourceChangeListener(IResourceChangeListener theResourceChangeListener) {
		myResourceChangeListenerCache.remove(theResourceChangeListener);
	}



	@Override
	@VisibleForTesting
	public void clearListenersForUnitTest() {
		myResourceChangeListenerCache.clearListenersForUnitTest();
	}


}
