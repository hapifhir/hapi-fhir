package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import com.google.common.annotations.VisibleForTesting;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class VersionChangeListenerMap {
	private final Map<String, List<VersionChangeListenerEntry>> myListenersByResourcetype = new HashMap<>();

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
	public List<VersionChangeListenerEntry> getListenerEntries(String theResourceType) {
		return myListenersByResourcetype.computeIfAbsent(theResourceType, listener -> new ArrayList<>());
	}
}
