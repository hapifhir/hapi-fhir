package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.primitive.IdDt;

public class VersionChangeListenerEntry {
	private final IVersionChangeListener myVersionChangeListener;
	private final SearchParameterMap mySearchParameterMap;
	private boolean myInitialized = false;

	public VersionChangeListenerEntry(IVersionChangeListener theVersionChangeListener, SearchParameterMap theSearchParameterMap) {
		myVersionChangeListener = theVersionChangeListener;
		mySearchParameterMap = theSearchParameterMap;
	}

	public IVersionChangeListener getVersionChangeListener() {
		return myVersionChangeListener;
	}

	public SearchParameterMap getSearchParameterMap() {
		return mySearchParameterMap;
	}

	public long notifyListener(ResourceVersionCache theResourceVersionCache, ResourceVersionMap theNewResourceVersionMap) {
		long retval = 0;
		if (myInitialized) {
			retval = compareLastVersionMapToNewVersionMapAndNotifyListenerOfChanges(theResourceVersionCache, theNewResourceVersionMap, myVersionChangeListener);
		} else {
			theResourceVersionCache.initialize(theNewResourceVersionMap);
			myVersionChangeListener.handleInit(theNewResourceVersionMap.getSourceIds());
			retval = theNewResourceVersionMap.size();
			myInitialized = true;
		}
		return retval;
	}

	public long compareLastVersionMapToNewVersionMapAndNotifyListenerOfChanges(ResourceVersionCache theOldResourceVersionCache, ResourceVersionMap theNewResourceVersionMap, IVersionChangeListener theListener) {
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
