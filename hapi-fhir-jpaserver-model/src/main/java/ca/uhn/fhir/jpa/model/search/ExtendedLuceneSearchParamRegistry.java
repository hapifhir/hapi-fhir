package ca.uhn.fhir.jpa.model.search;

import ca.uhn.fhir.rest.api.RestSearchParameterTypeEnum;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ExtendedLuceneSearchParamRegistry extends LinkedHashMap<String, RestSearchParameterTypeEnum> {

	private final FreetextSortPropertyFilterHelper mySortPropertyFilterHelper;


	public ExtendedLuceneSearchParamRegistry() {
		mySortPropertyFilterHelper = new FreetextSortPropertyFilterHelper();
	}


	public List<String> getTypeAndFieldPaths(String theParamName) {
		RestSearchParameterTypeEnum paramType = null;
		List<String> fieldPathList = new ArrayList<>();
		for (String mapKey : this.keySet()) {
			if (secondKeyPartEquals(mapKey, theParamName)) {
				paramType = this.get(mapKey);
				fieldPathList.add(mapKey);
			}
		}

		return mySortPropertyFilterHelper.filterProperties(paramType, fieldPathList);
	}


	private boolean secondKeyPartEquals(String theComposite, String theArgument) {
		String[] parts = theComposite.split("\\.");
		return parts.length > 1 && parts[1].equals(theArgument);
	}


}
