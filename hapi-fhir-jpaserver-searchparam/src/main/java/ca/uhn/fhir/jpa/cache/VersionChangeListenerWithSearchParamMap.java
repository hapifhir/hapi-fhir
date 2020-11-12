package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;

public class VersionChangeListenerWithSearchParamMap {
	private final IVersionChangeListener myVersionChangeListener;
	private final SearchParameterMap mySearchParameterMap;
	private boolean myInitialized = false;

	public VersionChangeListenerWithSearchParamMap(IVersionChangeListener theVersionChangeListener, SearchParameterMap theSearchParameterMap) {
		myVersionChangeListener = theVersionChangeListener;
		mySearchParameterMap = theSearchParameterMap;
	}

	public IVersionChangeListener getVersionChangeListener() {
		return myVersionChangeListener;
	}

	public SearchParameterMap getSearchParameterMap() {
		return mySearchParameterMap;
	}

	public boolean isInitialized() {
		return myInitialized;
	}

	public VersionChangeListenerWithSearchParamMap setInitialized(boolean theInitialized) {
		myInitialized = theInitialized;
		return this;
	}
}
