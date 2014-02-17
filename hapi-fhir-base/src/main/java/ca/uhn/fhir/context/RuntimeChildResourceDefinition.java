package ca.uhn.fhir.context;

import java.lang.reflect.Field;

public class RuntimeChildResourceDefinition extends BaseRuntimeChildDefinition {

	private String myResourceName;

	public RuntimeChildResourceDefinition(Field theField, String theElementName, String theResourceName, int theMin, int theMax) {
		super(theField, theElementName,theMin,theMax);
		
		myResourceName = theResourceName;
	}

	public String getResourceName() {
		return myResourceName;
	}

}
