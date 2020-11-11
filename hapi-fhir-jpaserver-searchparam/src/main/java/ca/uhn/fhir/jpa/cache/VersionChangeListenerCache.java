package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.searchparam.matcher.InMemoryMatchResult;
import ca.uhn.fhir.jpa.searchparam.matcher.SearchParamMatcher;
import ca.uhn.fhir.model.primitive.IdDt;
import com.google.common.annotations.VisibleForTesting;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// FIXME KHS change to bean
@Component
public class VersionChangeListenerCache {
	private static final Logger ourLog = LoggerFactory.getLogger(VersionChangeListenerCache.class);

	@Autowired
	FhirContext myFhirContext;
	@Autowired
	SearchParamMatcher mySearchParamMatcher;

	private final Map<String, Set<VersionChangeListenerEntry>> myListenersByResourcetype = new HashMap<>();

	public void add(String theResourceType, IVersionChangeListener theVersionChangeListener, SearchParameterMap theMap) {
		getListenerEntries(theResourceType).add(new VersionChangeListenerEntry(theVersionChangeListener, theMap));
	}

	@VisibleForTesting
	public void clearListenersForUnitTest() {
		myListenersByResourcetype.clear();
	}

	public Set<String> resourceNames() {
		return myListenersByResourcetype.keySet();
	}

	@Nonnull
	public Set<VersionChangeListenerEntry> getListenerEntries(String theResourceType) {
		return myListenersByResourcetype.computeIfAbsent(theResourceType, listener -> new HashSet<>());
	}

	public boolean hasListenerFor(IBaseResource theResource) {
		String resourceName = myFhirContext.getResourceType(theResource);
		return myListenersByResourcetype.get(resourceName).stream().anyMatch(entry -> matches(entry.getSearchParameterMap(), theResource));
	}

	private boolean matches(SearchParameterMap theSearchParameterMap, IBaseResource theResource) {
		InMemoryMatchResult result = mySearchParamMatcher.match(theSearchParameterMap, theResource);
		if (!result.isInMemory()) {
			// FIXME KHS detect this at registration time
			ourLog.warn("Search Parameter Map {} cannot be processed in-memory", theSearchParameterMap);
		}
		return result.matched();
	}

	// FIXME KHS ensure we reset cache
	public long notifyListener(VersionChangeListenerEntry theListenerEntry, ResourceVersionCache theOldResourceVersionCache, ResourceVersionMap theNewResourceVersionMap) {
		long retval = 0;
		IVersionChangeListener versionChangeListener = theListenerEntry.getVersionChangeListener();
		if (theListenerEntry.isInitialized()) {
			retval = compareLastVersionMapToNewVersionMapAndNotifyListenerOfChanges(versionChangeListener, theOldResourceVersionCache, theNewResourceVersionMap);
		} else {
			theOldResourceVersionCache.initialize(theNewResourceVersionMap);
			versionChangeListener.handleInit(theNewResourceVersionMap.getSourceIds());
			retval = theNewResourceVersionMap.size();
			theListenerEntry.setInitialized(true);
		}
		return retval;
	}

	public long compareLastVersionMapToNewVersionMapAndNotifyListenerOfChanges(IVersionChangeListener theListener, ResourceVersionCache theOldResourceVersionCache, ResourceVersionMap theNewResourceVersionMap) {
		long count = 0;
		for (IdDt id : theNewResourceVersionMap.keySet()) {
			String previousValue = theOldResourceVersionCache.addOrUpdate(id, theNewResourceVersionMap.get(id));
			IdDt newId = id.withVersion(theNewResourceVersionMap.get(id));
			if (previousValue == null) {
				theListener.handleCreate(newId);
				++count;
			} else if (!theNewResourceVersionMap.get(id).equals(previousValue)) {
				theListener.handleUpdate(newId);
				++count;
			}
		}

		// Now check for deletes
		for (IdDt id : theOldResourceVersionCache.keySet()) {
			if (!theNewResourceVersionMap.containsKey(id)) {
				theListener.handleDelete(id);
				++count;
			}
		}
		return count;
	}
}
