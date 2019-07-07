package ca.uhn.fhir.rest.server;

import ca.uhn.fhir.rest.server.method.OperationMethodBinding;
import ca.uhn.fhir.rest.server.method.SearchMethodBinding;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;

public class Bindings {
	private final IdentityHashMap<SearchMethodBinding, String> myNamedSearchMethodBindingToName;
	private final HashMap<String, List<SearchMethodBinding>> mySearchNameToBindings;
	private final HashMap<String, List<OperationMethodBinding>> myOperationNameToBindings;
	private final IdentityHashMap<OperationMethodBinding, String> myOperationBindingToName;

	public Bindings(IdentityHashMap<SearchMethodBinding, String> theNamedSearchMethodBindingToName, HashMap<String, List<SearchMethodBinding>> theSearchNameToBindings, HashMap<String, List<OperationMethodBinding>> theOperationNameToBindings, IdentityHashMap<OperationMethodBinding, String> theOperationBindingToName) {
		myNamedSearchMethodBindingToName = theNamedSearchMethodBindingToName;
		mySearchNameToBindings = theSearchNameToBindings;
		myOperationNameToBindings = theOperationNameToBindings;
		myOperationBindingToName = theOperationBindingToName;
	}

	public IdentityHashMap<SearchMethodBinding, String> getNamedSearchMethodBindingToName() {
		return myNamedSearchMethodBindingToName;
	}

	public HashMap<String, List<SearchMethodBinding>> getSearchNameToBindings() {
		return mySearchNameToBindings;
	}

	public HashMap<String, List<OperationMethodBinding>> getOperationNameToBindings() {
		return myOperationNameToBindings;
	}

	public IdentityHashMap<OperationMethodBinding, String> getOperationBindingToName() {
		return myOperationBindingToName;
	}
}
