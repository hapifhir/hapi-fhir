package ca.uhn.fhir.context;

import java.lang.reflect.Field;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IValueSetEnumBinder;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;

public class RuntimeChildCompositeBoundDatatypeDefinition extends RuntimeChildCompositeDatatypeDefinition {

	private IValueSetEnumBinder<Enum<?>> myBinder;

	public RuntimeChildCompositeBoundDatatypeDefinition(Field theField, String theElementName, Child theChildAnnotation, Description theDescriptionAnnotation, Class<? extends IDatatype> theDatatype, IValueSetEnumBinder<Enum<?>> theBinder) {
		super(theField, theElementName, theChildAnnotation, theDescriptionAnnotation, theDatatype);
		myBinder = theBinder;
		if (theBinder==null) {
			throw new IllegalArgumentException("Binder must not be null");
		}
	}

	@Override
	public IValueSetEnumBinder<Enum<?>> getInstanceConstructorArguments() {
		return myBinder;
	}

}
