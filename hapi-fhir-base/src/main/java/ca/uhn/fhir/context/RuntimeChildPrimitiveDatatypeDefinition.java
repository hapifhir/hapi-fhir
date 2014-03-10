package ca.uhn.fhir.context;

import java.lang.reflect.Field;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;

public class RuntimeChildPrimitiveDatatypeDefinition extends BaseRuntimeChildDatatypeDefinition {

	public RuntimeChildPrimitiveDatatypeDefinition(Field theField, String theElementName, Description theDescriptionAnnotation, Child theChildAnnotation,  Class<? extends IDatatype> theDatatype) {
		super(theField, theElementName, theChildAnnotation, theDescriptionAnnotation, theDatatype);
	}

}
