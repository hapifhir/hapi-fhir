package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
/**
 * This Spring Component holds all of the IResourceChangeListeners that have been registered with the
 * IResourceChangeListenerRegistry along with the ResourceName and SearchParamMap they were registered with.
 */
public class ResourceChangeListenerCache {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceChangeListenerCache.class);

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	RegisteredResourceListenerFactory myRegisteredResourceListenerFactory;

	private final Queue<RegisteredResourceChangeListener> myListenerEntries = new ConcurrentLinkedQueue<RegisteredResourceChangeListener>();

	public RegisteredResourceChangeListener add(String theResourceName, IResourceChangeListener theResourceChangeListener, SearchParameterMap theMap, long theRemoteRefreshIntervalMs) {
		RegisteredResourceChangeListener retval = myRegisteredResourceListenerFactory.create(theResourceName, theMap, theResourceChangeListener, theRemoteRefreshIntervalMs);
		myListenerEntries.add(retval);
		return retval;
	}

	@VisibleForTesting
	public void clearListenersForUnitTest() {
		myListenerEntries.clear();
	}

	@Nonnull
	public Iterator<RegisteredResourceChangeListener> iterator() {
		return myListenerEntries.iterator();
	}

	/**
	 * Notify a listener with all matching resources if it hasn't been initialized yet, otherwise only notify it if
	 * any resources have changed
	 * @param theListenerEntry
	 * @param theNewResourceVersionMap the measured new resources
	 * @return the list of created, updated and deleted ids
	 */
	public ResourceChangeResult notifyListener(RegisteredResourceChangeListener theListenerEntry, ResourceVersionMap theNewResourceVersionMap) {
		ResourceChangeResult retval;
		IResourceChangeListener resourceChangeListener = theListenerEntry.getResourceChangeListener();
		if (theListenerEntry.isInitialized()) {
			retval = compareLastVersionMapToNewVersionMapAndNotifyListenerOfChanges(resourceChangeListener, theListenerEntry.getResourceVersionCache(), theNewResourceVersionMap);
		} else {
			theListenerEntry.getResourceVersionCache().initialize(theNewResourceVersionMap);
			resourceChangeListener.handleInit(theNewResourceVersionMap.getSourceIds());
			retval = ResourceChangeResult.fromCreated(theNewResourceVersionMap.size());
			theListenerEntry.setInitialized(true);
		}
		return retval;
	}

	private ResourceChangeResult compareLastVersionMapToNewVersionMapAndNotifyListenerOfChanges(IResourceChangeListener theListener, ResourceVersionCache theOldResourceVersionCache, ResourceVersionMap theNewResourceVersionMap) {
		// If the new ResourceVersionMap does not have the old key - delete it
		List<IIdType> deletedIds = new ArrayList<>();
		theOldResourceVersionCache.keySet()
				.forEach(id -> {
					if (!theNewResourceVersionMap.containsKey(id)) {
						deletedIds.add(id);
					}
				});
		deletedIds.forEach(theOldResourceVersionCache::removeResourceId);

		List<IIdType> createdIds = new ArrayList<>();
		List<IIdType> updatedIds = new ArrayList<>();

		for (IIdType id : theNewResourceVersionMap.keySet()) {
			String previousValue = theOldResourceVersionCache.put(id, theNewResourceVersionMap.get(id));
			IIdType newId = id.withVersion(theNewResourceVersionMap.get(id));
			if (previousValue == null) {
				createdIds.add(newId);
			} else if (!theNewResourceVersionMap.get(id).equals(previousValue)) {
				updatedIds.add(newId);
			}
		}

		IResourceChangeEvent resourceChangeEvent = ResourceChangeEvent.fromCreatedUpdatedDeletedResourceIds(createdIds, updatedIds, deletedIds);
		if (!resourceChangeEvent.isEmpty()) {
			theListener.handleChange(resourceChangeEvent);
		}
		return ResourceChangeResult.fromResourceChangeEvent(resourceChangeEvent);
	}

	public void remove(IResourceChangeListener theResourceChangeListener) {
		myListenerEntries.removeIf(l -> l.getResourceChangeListener().equals(theResourceChangeListener));
	}

	public int size() {
		return myListenerEntries.size();
	}

	@VisibleForTesting
	public void clearCachesForUnitTest() {
		myListenerEntries.forEach(RegisteredResourceChangeListener::clear);
	}

	public boolean contains(RegisteredResourceChangeListener theEntry) {
		return myListenerEntries.contains(theEntry);
	}

	@VisibleForTesting
	public int getResourceVersionCacheSizeForUnitTest() {
		int retval = 0;
		for (RegisteredResourceChangeListener entry : myListenerEntries) {
			retval += entry.getResourceVersionCache().size();
		}
		return retval;
	}

	public void requestRefreshIfWatching(IBaseResource theResource) {
		String resourceName = myFhirContext.getResourceType(theResource);
		for (RegisteredResourceChangeListener entry : myListenerEntries) {
			if (resourceName.equals(entry.getResourceName())) {
				entry.requestRefreshIfWatching(theResource);
			}
		}
	}
}
