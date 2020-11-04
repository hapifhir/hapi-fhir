package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import com.google.common.annotations.VisibleForTesting;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VersionChangeListenerMap {
	private final Map<String, Map<IVersionChangeListener, SearchParameterMap>> myListenersByResourcetype = new HashMap<>();

	public void add(String theResourceType, IVersionChangeListener theVersionChangeListener, SearchParameterMap theMap) {
		myListenersByResourcetype.computeIfAbsent(theResourceType, listener -> new HashMap<>());
		myListenersByResourcetype.get(theResourceType).put(theVersionChangeListener, theMap);
	}

	@VisibleForTesting
	public void clearListenersForUnitTest() {
		myListenersByResourcetype.clear();
	}

	public Set<String> keySet() {
		return myListenersByResourcetype.keySet();
	}

	public Map<IVersionChangeListener, SearchParameterMap> getListenerMap(String theResourceType) {
		return myListenersByResourcetype.get(theResourceType);
	}

	public boolean hasListenersFor(String theResourceName) {
		return myListenersByResourcetype.containsKey(theResourceName) && !myListenersByResourcetype.get(theResourceName).isEmpty();
	}
}
