package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Used to register which fields are written to freetext index for each parameter type so at query time the
 * information can be used to build sorting clauses.
 */
public class ExtendedFulltextSearchParamRegistry extends LinkedHashMap<String, RestSearchParameterTypeEnum> {

	private final FreetextSortPropertyFilterHelper mySortPropertyFilterHelper;

	public ExtendedFulltextSearchParamRegistry(FreetextSortPropertyFilterHelper theFreetextSortPropertyFilterHelper) {
		mySortPropertyFilterHelper = theFreetextSortPropertyFilterHelper;
	}


	public List<String> getFieldPaths(String theParamName) {
		RestSearchParameterTypeEnum paramType = null;

		List<String> parameterFieldPathList = new ArrayList<>();
		for (String mapKey : this.keySet()) {
			if (secondKeyPartEquals(mapKey, theParamName)) {
				paramType = this.get(mapKey);
				parameterFieldPathList.add(mapKey);
			}
		}

		return mySortPropertyFilterHelper.filterProperties(paramType, parameterFieldPathList);
	}


	private boolean secondKeyPartEquals(String theComposite, String theArgument) {
		String[] parts = theComposite.split("\\.");
		return parts.length > 1 && parts[1].equals(theArgument);
	}


}
