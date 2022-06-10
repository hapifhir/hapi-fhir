package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

//fixme jm: extend map
public class ExtendedLuceneSearchParamRegistry {

	// insertion order must be kept
	private final Map<String, RestSearchParameterTypeEnum> parameterTypeMap = new LinkedHashMap<>();

	public RestSearchParameterTypeEnum put(String key, RestSearchParameterTypeEnum value) {
		return parameterTypeMap.put(key, value);
	}

	public Map.Entry<RestSearchParameterTypeEnum, List<String>> getTypeAndFieldPaths(String theKey) {
		RestSearchParameterTypeEnum paramType = null;
		List<String> fieldPathList = new ArrayList<>();
		for (String mapKey : parameterTypeMap.keySet()) {
			if (secondPartEquals(mapKey, theKey)) {
				paramType = parameterTypeMap.get(theKey);
				fieldPathList.add(mapKey);
			}
		}
		return new java.util.AbstractMap.SimpleEntry(paramType, fieldPathList);
	}


	private boolean secondPartEquals(String theComposite, String theArgument) {
		String[] parts = theComposite.split("\\.");
		return parts.length > 1 && parts[1].equals(theArgument);
	}
}
