package ca.uhn.fhir.context;

import java.lang.reflect.Field;

public class RuntimeChildCodedDatatypeDefinition extends RuntimeChildDatatypeDefinition {

	private String myTableName;

	public RuntimeChildCodedDatatypeDefinition(Field theField, String theElementName, String theDatatypeName, int theMin, int theMax, String theTableName) {
		super(theField, theElementName,theDatatypeName, theMin,theMax);
		
		myTableName = theTableName;
	}

	public String getTableName() {
		return myTableName;
	}

}
