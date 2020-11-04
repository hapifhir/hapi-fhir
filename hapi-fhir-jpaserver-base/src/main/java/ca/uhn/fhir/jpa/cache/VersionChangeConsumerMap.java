package ca.uhn.fhir.jpa.cache;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import com.google.common.annotations.VisibleForTesting;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class VersionChangeConsumerMap {
	private final Map<String, Map<IVersionChangeListener, SearchParameterMap>> myConsumersByResourcetype = new HashMap<>();

	public void add(String theResourceType, IVersionChangeListener theVersionChangeConsumer, SearchParameterMap theMap) {
		myConsumersByResourcetype.computeIfAbsent(theResourceType, consumer -> new HashMap<>());
		myConsumersByResourcetype.get(theResourceType).put(theVersionChangeConsumer, theMap);
	}

	@VisibleForTesting
	public void clearConsumersForUnitTest() {
		myConsumersByResourcetype.clear();
	}

	public Set<String> keySet() {
		return myConsumersByResourcetype.keySet();
	}

	public Map<IVersionChangeListener, SearchParameterMap> getConsumerMap(String theResourceType) {
		return myConsumersByResourcetype.get(theResourceType);
	}

	public boolean hasConsumersFor(String theResourceName) {
		return myConsumersByResourcetype.containsKey(theResourceName) && !myConsumersByResourcetype.get(theResourceName).isEmpty();
	}
}
