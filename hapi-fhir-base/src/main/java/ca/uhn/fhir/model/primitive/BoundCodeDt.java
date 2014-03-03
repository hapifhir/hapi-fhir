package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.model.api.IValueSetEnumBinder;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;

@DatatypeDef(name = "code")
public class BoundCodeDt<T extends Enum<?>> extends CodeDt {

	private IValueSetEnumBinder<T> myBinder;

	public BoundCodeDt(IValueSetEnumBinder<T> theBinder) {
		myBinder = theBinder;
	}
	
	public BoundCodeDt(IValueSetEnumBinder<T> theBinder, T theValue) {
		myBinder = theBinder;
		setValueAsEnum(theValue);
	}

	public void setValueAsEnum(T theValue) {
		setValue(myBinder.toCodeString(theValue));
	}
	
	public T getValueAsEnum() {
		return myBinder.fromCodeString(getValue());
	}
}
