package ca.uhn.fhir.jpa.searchparam.registry;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.rest.api.Constants;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ReadOnlySearchParamCache {
	private static final Logger ourLog = LoggerFactory.getLogger(ReadOnlySearchParamCache.class);

	protected final Map<String, Map<String, RuntimeSearchParam>> myMap;

	ReadOnlySearchParamCache() {
		myMap = new HashMap<>();
	}

	private ReadOnlySearchParamCache(RuntimeSearchParamCache theRuntimeSearchParamCache) {
		myMap = theRuntimeSearchParamCache.myMap;
	}

	public static ReadOnlySearchParamCache fromFhirContext(FhirContext theFhirContext) {
		ReadOnlySearchParamCache retval = new ReadOnlySearchParamCache();

		Set<String> resourceNames = theFhirContext.getResourceTypes();

		for (String resourceName : resourceNames) {
			RuntimeResourceDefinition nextResDef = theFhirContext.getResourceDefinition(resourceName);
			String nextResourceName = nextResDef.getName();
			HashMap<String, RuntimeSearchParam> nameToParam = new HashMap<>();
			retval.myMap.put(nextResourceName, nameToParam);

			for (RuntimeSearchParam nextSp : nextResDef.getSearchParams()) {
				nameToParam.put(nextSp.getName(), nextSp);
			}
		}
		return retval;
	}

	public static ReadOnlySearchParamCache fromRuntimeSearchParamCache(RuntimeSearchParamCache theRuntimeSearchParamCache) {
		return new ReadOnlySearchParamCache(theRuntimeSearchParamCache);
	}

	protected Map<String, RuntimeSearchParam> getSearchParamMap(String theResourceName) {
		return myMap.computeIfAbsent(theResourceName, k -> new HashMap<>());
	}

	public Collection<String> getValidSearchParameterNamesIncludingMeta(String theResourceName) {
		TreeSet<String> retVal = new TreeSet<>(myMap.get(theResourceName).keySet());
		retVal.add(IAnyResource.SP_RES_ID);
		retVal.add(Constants.PARAM_LASTUPDATED);
		return retVal;

	}

	public int size() {
		return myMap.size();
	}
}
