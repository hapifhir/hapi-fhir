package ca.uhn.fhir.context;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import ca.uhn.fhir.model.api.IResource;

public class RuntimeChildResourceDefinition extends BaseRuntimeChildDefinition {

	private String myResourceName;
	private Set<String> myValidChildNames;
	private List<Class<? extends IResource>> myChildTypes;

	public RuntimeChildResourceDefinition(Field theField, String theElementName, int theMin, int theMax, List<Class<? extends IResource>> theChildTypes) {
		super(theField, theMin,theMax, theElementName);
		
		myChildTypes = theChildTypes;
	}

	public String getResourceName() {
		return myResourceName;
	}

	@Override
	public Set<String> getValidChildNames() {
		return myValidChildNames;
	}

	@Override
	public BaseRuntimeElementDefinition<?> getChildByName(String theName) {
		if (myElementName.equals(theName)) {
		}
		return null;
	}


}
