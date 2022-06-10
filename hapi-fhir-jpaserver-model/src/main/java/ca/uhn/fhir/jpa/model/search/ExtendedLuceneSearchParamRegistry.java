package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_VALUE;
import static ca.uhn.fhir.jpa.model.search.HibernateSearchIndexWriter.QTY_VALUE_NORM;

//fixme jm: extend map (needs to be a map. No no?)
public class ExtendedLuceneSearchParamRegistry {

	// insertion order must be kept
	private final Map<String, RestSearchParameterTypeEnum> parameterTypeMap = new LinkedHashMap<>();

	public RestSearchParameterTypeEnum put(String key, RestSearchParameterTypeEnum value) {
		return parameterTypeMap.put(key, value);
	}

	public Map.Entry<RestSearchParameterTypeEnum, List<String>> getTypeAndFieldPaths(String theParamName) {
		RestSearchParameterTypeEnum paramType = null;
		List<String> fieldPathList = new ArrayList<>();
		for (String mapKey : parameterTypeMap.keySet()) {
			if (secondPartEquals(mapKey, theParamName)) {
				paramType = parameterTypeMap.get(mapKey);
				fieldPathList.add(mapKey);
			}
		}

		return filterEntries(paramType, fieldPathList);
	}


	/**
	 * Filter entries based in theParamType in the following way:
	 *
	 *  for parameter type Quantity:
	 *  	if a normalized value exists return only that path as it is enough for sorting.
	 *  	Otherwise return the regular value entry. This sorting could be meaningless, in case system and units defer
	 *
	 * @param theParamType the parameter type of the list of entries
	 * @param theFieldPathList each index global path for the parameter
	 * @return the filtered entries
	 */
	private Map.Entry<RestSearchParameterTypeEnum, List<String>> filterEntries(RestSearchParameterTypeEnum theParamType, List<String> theFieldPathList) {
		if (theParamType == RestSearchParameterTypeEnum.QUANTITY) {
				return new java.util.AbstractMap.SimpleEntry<>(theParamType, getFilteredQuantityPathList(theFieldPathList));
		}

		// defalt: no filtering
		return new java.util.AbstractMap.SimpleEntry<>(theParamType, theFieldPathList);
	}


//	fixme jm: use polymorphism
	/**
	 * If any entry ends with QTY_VALUE_NORM return it. Otherwise, return the QTY_VALUE entry
	 */
	private List<String> getFilteredQuantityPathList(List<String> theFieldPathList) {
		String theNotNormalizedPath = null;

		for (String fieldPath : theFieldPathList) {
			if (fieldPath.endsWith(QTY_VALUE_NORM)) {
				return Collections.singletonList(fieldPath);
			}

			if (fieldPath.endsWith(QTY_VALUE)) {
				theNotNormalizedPath = fieldPath;
			}
		}

		return theNotNormalizedPath == null ? Collections.emptyList() : Collections.singletonList(theNotNormalizedPath);
	}


	private boolean secondPartEquals(String theComposite, String theArgument) {
		String[] parts = theComposite.split("\\.");
		return parts.length > 1 && parts[1].equals(theArgument);
	}


}
