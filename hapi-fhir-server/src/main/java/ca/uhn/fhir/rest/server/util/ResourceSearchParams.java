/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.rest.server.util;

import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static ca.uhn.fhir.rest.server.util.ISearchParamRegistry.isAllowedForContext;

public class ResourceSearchParams {
	private final String myResourceName;
	private final Map<String, RuntimeSearchParam> myMap;
	private final Map<ISearchParamRegistry.SearchParamLookupContextEnum, ResourceSearchParams> myContextToParams =
			new HashMap<>();

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

	/**
	 * Returns a filtered view of this {@link ResourceSearchParams} instance if
	 * any parameters are not valid for the given {@literal theContext}.
	 */
	public ResourceSearchParams toFilteredForContext(ISearchParamRegistry.SearchParamLookupContextEnum theContext) {
		if (theContext == null) {
			return this;
		}
		synchronized (this) {
			ResourceSearchParams retVal = myContextToParams.get(theContext);
			if (retVal == null) {
				Map<String, RuntimeSearchParam> filteredMap = new HashMap<>(myMap.size());
				for (var nextEntry : myMap.entrySet()) {
					String key = nextEntry.getKey();
					RuntimeSearchParam nextParam = nextEntry.getValue();
					if (isAllowedForContext(nextParam, theContext)) {
						filteredMap.put(key, nextParam);
					}
				}
				retVal = new ResourceSearchParams(myResourceName, filteredMap);
				myContextToParams.put(theContext, retVal);
			}
			return retVal;
		}
	}

	public static ResourceSearchParams empty(String theResourceName) {
		return new ResourceSearchParams(theResourceName, Collections.emptyMap());
	}

	public ResourceSearchParams readOnly() {
		return new ResourceSearchParams(myResourceName, Collections.unmodifiableMap(this.myMap));
	}

	public void remove(String theName) {
		myContextToParams.clear();
		myMap.remove(theName);
	}

	public int size() {
		return myMap.size();
	}

	public RuntimeSearchParam get(String theParamName) {
		return myMap.get(theParamName);
	}

	public RuntimeSearchParam put(String theName, RuntimeSearchParam theSearchParam) {
		myContextToParams.clear();
		return myMap.put(theName, theSearchParam);
	}

	public void addSearchParamIfAbsent(String theParamName, RuntimeSearchParam theRuntimeSearchParam) {
		myContextToParams.clear();
		myMap.putIfAbsent(theParamName, theRuntimeSearchParam);
	}

	public Set<String> getSearchParamNames() {
		return myMap.keySet();
	}

	public boolean containsParamName(String theParamName) {
		return myMap.containsKey(theParamName);
	}

	public void removeInactive() {
		myMap.entrySet()
				.removeIf(entry ->
						entry.getValue().getStatus() != RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE);
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
