package ca.uhn.fhir.context;

import java.lang.reflect.Field;

public class RuntimeChildDatatypeDefinition extends BaseRuntimeChildDefinition {

	private String myDatatypeName;

	public RuntimeChildDatatypeDefinition(Field theField, String theElementName, String theDatatypeName, int theMin, int theMax) {
		super(theField, theElementName,theMin,theMax);
		
		myDatatypeName = theDatatypeName;
	}

	public String getDatatypeName() {
		return myDatatypeName;
	}

}
