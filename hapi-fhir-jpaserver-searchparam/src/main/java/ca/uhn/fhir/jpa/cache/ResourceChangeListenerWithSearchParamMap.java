package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;

public class ResourceChangeListenerWithSearchParamMap {
	private final String myResourceName;
	private final IResourceChangeListener myResourceChangeListener;
	private final SearchParameterMap mySearchParameterMap;
	private boolean myInitialized = false;

	public ResourceChangeListenerWithSearchParamMap(String theResourceName, IResourceChangeListener theResourceChangeListener, SearchParameterMap theSearchParameterMap) {
		myResourceName = theResourceName;
		myResourceChangeListener = theResourceChangeListener;
		mySearchParameterMap = theSearchParameterMap;
	}

	public IResourceChangeListener getResourceChangeListener() {
		return myResourceChangeListener;
	}

	public SearchParameterMap getSearchParameterMap() {
		return mySearchParameterMap;
	}

	public boolean isInitialized() {
		return myInitialized;
	}

	public ResourceChangeListenerWithSearchParamMap setInitialized(boolean theInitialized) {
		myInitialized = theInitialized;
		return this;
	}

	public String getResourceName() {
		return myResourceName;
	}
}
