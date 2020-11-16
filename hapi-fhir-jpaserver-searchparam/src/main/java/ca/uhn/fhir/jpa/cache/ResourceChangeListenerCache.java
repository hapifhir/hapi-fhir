package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.model.primitive.IdDt;
import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.collections4.SetValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class ResourceChangeListenerCache {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceChangeListenerCache.class);

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	SearchParamMatcher mySearchParamMatcher;

	private final SetValuedMap<String, ResourceChangeListenerWithSearchParamMap> myListenersByResourceType = new HashSetValuedHashMap<>();

	public void add(String theResourceType, IResourceChangeListener theResourceChangeListener, SearchParameterMap theMap) {
		getListenerEntries(theResourceType).add(new ResourceChangeListenerWithSearchParamMap(theResourceChangeListener, theMap));
	}

	@VisibleForTesting
	public void clearListenersForUnitTest() {
		myListenersByResourceType.clear();
	}

	public Set<String> resourceNames() {
		return myListenersByResourceType.keySet();
	}

	@Nonnull
	public Set<ResourceChangeListenerWithSearchParamMap> getListenerEntries(String theResourceType) {
		return myListenersByResourceType.get(theResourceType);
	}

	public boolean hasListenerFor(IBaseResource theResource) {
		String resourceName = myFhirContext.getResourceType(theResource);
		return myListenersByResourceType.get(resourceName).stream().anyMatch(entry -> matches(entry.getSearchParameterMap(), theResource));
	}

	private boolean matches(SearchParameterMap theSearchParameterMap, IBaseResource theResource) {
		InMemoryMatchResult result = mySearchParamMatcher.match(theSearchParameterMap, theResource);
		if (!result.isInMemory()) {
			// This should never happen since we detect this at
			ourLog.warn("Search Parameter Map {} cannot be processed in-memory", theSearchParameterMap);
		}
		return result.matched();
	}

	public ResourceChangeResult notifyListener(ResourceChangeListenerWithSearchParamMap theListenerEntry, ResourceVersionCache theOldResourceVersionCache, ResourceVersionMap theNewResourceVersionMap) {
		ResourceChangeResult retval;
		IResourceChangeListener resourceChangeListener = theListenerEntry.getResourceChangeListener();
		if (theListenerEntry.isInitialized()) {
			retval = compareLastVersionMapToNewVersionMapAndNotifyListenerOfChanges(resourceChangeListener, theOldResourceVersionCache, theNewResourceVersionMap);
		} else {
			theOldResourceVersionCache.initialize(theNewResourceVersionMap);
			resourceChangeListener.handleInit(theNewResourceVersionMap.getSourceIds());
			retval = ResourceChangeResult.fromAdded(theNewResourceVersionMap.size());
			theListenerEntry.setInitialized(true);
		}
		return retval;
	}

	public ResourceChangeResult compareLastVersionMapToNewVersionMapAndNotifyListenerOfChanges(IResourceChangeListener theListener, ResourceVersionCache theOldResourceVersionCache, ResourceVersionMap theNewResourceVersionMap) {
		AtomicLong removed = new AtomicLong();
		// If the NEW ResourceVersionMap does NOT have OLD key - delete it
		List<IdDt> toDelete = new ArrayList<>();
		for (String key : theOldResourceVersionCache.keySet()) {
			Map<IdDt, String> oldVersionCache = theOldResourceVersionCache.getMap(key);
			oldVersionCache.keySet()
				.forEach(id -> {
					if (!theNewResourceVersionMap.containsKey(id)) {
						toDelete.add(id);
					}
				});
		}
		toDelete.forEach(id -> {
			theOldResourceVersionCache.remove(id);
			theListener.handleDelete(id);
			removed.incrementAndGet();
		});

		long added = 0;
		long updated = 0;
		for (IdDt id : theNewResourceVersionMap.keySet()) {
			String previousValue = theOldResourceVersionCache.addOrUpdate(id, theNewResourceVersionMap.get(id));
			IdDt newId = id.withVersion(theNewResourceVersionMap.get(id));
			if (previousValue == null) {
				theListener.handleCreate(newId);
				++added;
			} else if (!theNewResourceVersionMap.get(id).equals(previousValue)) {
				theListener.handleUpdate(newId);
				++updated;
			}
		}

		return ResourceChangeResult.fromAddedUpdatedRemoved(added, updated, removed.get());
	}

	public void remove(IResourceChangeListener theResourceChangeListener) {
		myListenersByResourceType.entries().removeIf(entry -> entry.getValue().getResourceChangeListener().equals(theResourceChangeListener));
	}

	public int size() {
		return myListenersByResourceType.size();
	}
}
