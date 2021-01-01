package ca.uhn.fhir.jpa.searchparam.registry;

/*-
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2021 Smile CDR, Inc.
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

import ca.uhn.fhir.context.RuntimeSearchParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RuntimeSearchParamCache extends ReadOnlySearchParamCache {
	private static final Logger ourLog = LoggerFactory.getLogger(RuntimeSearchParamCache.class);

	protected RuntimeSearchParamCache() {
	}

	public static RuntimeSearchParamCache fromReadOnlySearchParmCache(ReadOnlySearchParamCache theBuiltInSearchParams) {
		RuntimeSearchParamCache retval = new RuntimeSearchParamCache();
		retval.putAll(theBuiltInSearchParams);
		return retval;
	}

	public void add(String theResourceName, String theName, RuntimeSearchParam theSearchParam) {
		getSearchParamMap(theResourceName).put(theName, theSearchParam);
	}

	public void remove(String theResourceName, String theName) {
		if (!myMap.containsKey(theResourceName)) {
			return;
		}
		myMap.get(theResourceName).remove(theName);
	}

	private void putAll(ReadOnlySearchParamCache theReadOnlySearchParamCache) {
		Set<Map.Entry<String, Map<String, RuntimeSearchParam>>> builtInSps = theReadOnlySearchParamCache.myMap.entrySet();
		for (Map.Entry<String, Map<String, RuntimeSearchParam>> nextBuiltInEntry : builtInSps) {
			for (RuntimeSearchParam nextParam : nextBuiltInEntry.getValue().values()) {
				String nextResourceName = nextBuiltInEntry.getKey();
				getSearchParamMap(nextResourceName).put(nextParam.getName(), nextParam);
			}

			ourLog.trace("Have {} built-in SPs for: {}", nextBuiltInEntry.getValue().size(), nextBuiltInEntry.getKey());
		}
	}

	public RuntimeSearchParam get(String theResourceName, String theParamName) {
		RuntimeSearchParam retVal = null;
		Map<String, RuntimeSearchParam> params = myMap.get(theResourceName);
		if (params != null) {
			retVal = params.get(theParamName);
		}
		return retVal;
	}

	public Set<String> getResourceNameKeys() {
		return myMap.keySet();
	}

	@Override
	protected Map<String, RuntimeSearchParam> getSearchParamMap(String theResourceName) {
		return myMap.computeIfAbsent(theResourceName, k -> new HashMap<>());
	}
}
