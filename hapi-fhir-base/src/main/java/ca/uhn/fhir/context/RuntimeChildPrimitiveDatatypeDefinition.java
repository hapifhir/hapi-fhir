package ca.uhn.fhir.context;

import java.lang.reflect.Field;

import ca.uhn.fhir.model.api.IDatatype;

public class RuntimeChildPrimitiveDatatypeDefinition extends BaseRuntimeChildDatatypeDefinition {

	public RuntimeChildPrimitiveDatatypeDefinition(Field theField, String theElementName, int theMin, int theMax, Class<? extends IDatatype> theDatatype) {
		super(theField, theElementName, theMin,theMax, theDatatype);
	}

}
