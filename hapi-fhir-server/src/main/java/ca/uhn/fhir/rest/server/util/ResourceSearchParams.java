package ca.uhn.fhir.rest.server.util;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

public class ResourceSearchParams {
	private final String myResourceName;
	private final Map<String, RuntimeSearchParam> myMap;

	public ResourceSearchParams(String theResourceName) {
		myResourceName = theResourceName;
		myMap = new LinkedHashMap<>();
	}

	private ResourceSearchParams(String theResourceName, Map<String, RuntimeSearchParam> theMap) {
		myResourceName = theResourceName;
		myMap = theMap;
	}

	public Collection<RuntimeSearchParam> values() {
		return myMap.values();
	}

	public static ResourceSearchParams empty(String theResourceName) {
		return new ResourceSearchParams(theResourceName, Collections.emptyMap());
	}

	public ResourceSearchParams readOnly() {
		return new ResourceSearchParams(myResourceName, Collections.unmodifiableMap(this.myMap));
	}

	public void remove(String theName) {
		myMap.remove(theName);
	}

	public int size() {
		return myMap.size();
	}

	public RuntimeSearchParam get(String theParamName) {
		return myMap.get(theParamName);
	}

	public RuntimeSearchParam put(String theName, RuntimeSearchParam theSearchParam) {
		return myMap.put(theName, theSearchParam);
	}

	public void addSearchParamIfAbsent(String theParamName, RuntimeSearchParam theRuntimeSearchParam) {
		myMap.putIfAbsent(theParamName, theRuntimeSearchParam);
	}

	public TreeSet<String> getSearchParamNames() {
		return new TreeSet<>(myMap.keySet());
	}

	public boolean containsParamName(String theParamName) {
		return myMap.containsKey(theParamName);
	}

	public void removeInactive() {
		myMap.entrySet().removeIf(entry -> entry.getValue().getStatus() != RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE);
	}

	public Stream<String> getReferenceSearchParamNames() {
		return myMap.entrySet().stream()
			.filter(entry -> entry.getValue().getParamType() == RestSearchParameterTypeEnum.REFERENCE)
			.map(Map.Entry::getKey);
	}

	public ResourceSearchParams makeCopy() {
		return new ResourceSearchParams(myResourceName, new HashMap<>(myMap));
	}
}
