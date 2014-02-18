package ca.uhn.fhir.context;

import java.lang.reflect.Field;

import ca.uhn.fhir.model.api.IDatatype;

public class RuntimeChildCompositeDatatypeDefinition extends BaseRuntimeChildDatatypeDefinition {

	public RuntimeChildCompositeDatatypeDefinition(Field theField, String theElementName, String theDatatypeName, int theMin, int theMax, Class<? extends IDatatype> theDatatype) {
		super(theField, theElementName, theMin,theMax,theDatatype);
	}

}
