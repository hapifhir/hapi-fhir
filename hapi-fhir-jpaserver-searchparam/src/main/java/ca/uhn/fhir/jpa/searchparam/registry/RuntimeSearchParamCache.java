package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.RuntimeSearchParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class RuntimeSearchParamCache extends ReadOnlySearchParamCache {
	private static final Logger ourLog = LoggerFactory.getLogger(RuntimeSearchParamCache.class);

	public static RuntimeSearchParamCache copyActiveSearchParamsFrom(RuntimeSearchParamCache theSearchParams) {
		RuntimeSearchParamCache retval = new RuntimeSearchParamCache();
		for (Map.Entry<String, Map<String, RuntimeSearchParam>> nextEntry : theSearchParams.myMap.entrySet()) {
			for (RuntimeSearchParam nextSp : nextEntry.getValue().values()) {
				String nextName = nextSp.getName();

				if (nextSp.getStatus() == RuntimeSearchParam.RuntimeSearchParamStatusEnum.ACTIVE) {
					retval.add(nextEntry.getKey(), nextName, nextSp);
				} else {
					// FIXME KHS how could this ever happen?
					retval.remove(nextEntry.getKey(), nextName);
					throw new IllegalStateException("HOW DID WE GET HERE?");
				}
				// FIXME KHS this log message never made sense...
				ourLog.debug("Replacing existing/built in search param {}:{} with new one", nextEntry.getKey(), nextName);
			}
		}
		return retval;
	}

	public void add(String theResourceName, String theName, RuntimeSearchParam theSearchParam) {
		getSearchParamMap(theResourceName).put(theName, theSearchParam);
	}

	public void remove(String theKey, String theName) {
		if (!myMap.containsKey(theKey)) {
			return;
		}
		myMap.get(theKey).remove(theName);
	}

	public void putAll(ReadOnlySearchParamCache theReadOnlySearchParamCache) {
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

	public Stream<RuntimeSearchParam> getSearchParamStream() {
		return myMap.values().stream().flatMap(entry -> entry.values().stream());
	}
}
