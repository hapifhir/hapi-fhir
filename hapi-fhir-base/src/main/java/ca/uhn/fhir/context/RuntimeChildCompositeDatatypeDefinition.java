package ca.uhn.fhir.context;

import java.lang.reflect.Field;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;

public class RuntimeChildCompositeDatatypeDefinition extends BaseRuntimeChildDatatypeDefinition {

	public RuntimeChildCompositeDatatypeDefinition(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation, Class<? extends IDatatype> theDatatype) {
		super(theField, theElementName, theChildAnnotation,theDescriptionAnnotation, theDatatype);
	}


}
