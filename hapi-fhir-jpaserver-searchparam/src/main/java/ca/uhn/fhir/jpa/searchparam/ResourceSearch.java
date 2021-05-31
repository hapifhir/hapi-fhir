package ca.uhn.fhir.jpa.searchparam;

import ca.uhn.fhir.context.RuntimeResourceDefinition;

public class ResourceSearch {
	private final RuntimeResourceDefinition myRuntimeResourceDefinition;
	private final SearchParameterMap mySearchParameterMap;

	public ResourceSearch(RuntimeResourceDefinition theRuntimeResourceDefinition, SearchParameterMap theSearchParameterMap) {
		myRuntimeResourceDefinition = theRuntimeResourceDefinition;
		mySearchParameterMap = theSearchParameterMap;
	}

	public RuntimeResourceDefinition getRuntimeResourceDefinition() {
		return myRuntimeResourceDefinition;
	}

	public SearchParameterMap getSearchParameterMap() {
		return mySearchParameterMap;
	}

	public String getResourceName() {
		return myRuntimeResourceDefinition.getName();
	}
}
