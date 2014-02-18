package ca.uhn.fhir.context;

import java.lang.reflect.Field;

import ca.uhn.fhir.model.api.IDatatype;

public abstract class BaseRuntimeChildDatatypeDefinition extends BaseRuntimeChildDefinition {

	private Class<? extends IDatatype> myDatatype;

	public BaseRuntimeChildDatatypeDefinition(Field theField, String theElementName, int theMin, int theMax, Class<? extends IDatatype> theDatatype) {
		super(theField, theMin, theMax, theElementName);
		
		myDatatype = theDatatype;
	}

	public Class<? extends IDatatype> getDatatype() {
		return myDatatype;
	}

}
