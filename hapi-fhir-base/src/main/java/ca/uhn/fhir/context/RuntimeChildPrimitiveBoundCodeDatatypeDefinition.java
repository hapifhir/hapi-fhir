package ca.uhn.fhir.context;

import java.lang.reflect.Field;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IValueSetEnumBinder;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;

public class RuntimeChildPrimitiveBoundCodeDatatypeDefinition extends RuntimeChildPrimitiveDatatypeDefinition {

	private IValueSetEnumBinder<Enum<?>> myBinder;

	public RuntimeChildPrimitiveBoundCodeDatatypeDefinition(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation,  Class<? extends IDatatype> theDatatype, IValueSetEnumBinder<Enum<?>> theBinder) {
		super(theField, theElementName, theDescriptionAnnotation, theChildAnnotation, theDatatype);

		myBinder = theBinder;
	}

	@Override
	public IValueSetEnumBinder<Enum<?>> getInstanceConstructorArguments() {
		return myBinder;
	}

}
