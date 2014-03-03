package ca.uhn.fhir.context;

import java.lang.reflect.Field;

import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.IValueSetEnumBinder;

public class RuntimeChildPrimitiveBoundCodeDatatypeDefinition extends RuntimeChildPrimitiveDatatypeDefinition {

	private IValueSetEnumBinder<Enum<?>> myBinder;

	public RuntimeChildPrimitiveBoundCodeDatatypeDefinition(Field theField, String theElementName, int theMin, int theMax, Class<? extends IDatatype> theDatatype, IValueSetEnumBinder<Enum<?>> theBinder) {
		super(theField, theElementName, theMin, theMax, theDatatype);

		myBinder = theBinder;
	}

	public IValueSetEnumBinder<Enum<?>> getInstanceConstructorArguments() {
		return myBinder;
	}

}
