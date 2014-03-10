package ca.uhn.fhir.model.primitive;

import static org.apache.commons.lang3.StringUtils.*;
import ca.uhn.fhir.model.api.IValueSetEnumBinder;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.dstu.composite.CodingDt;

@DatatypeDef(name = "Coding")
public class BoundCodingDt<T extends Enum<?>> extends CodingDt {

	private IValueSetEnumBinder<T> myBinder;

	public BoundCodingDt(IValueSetEnumBinder<T> theBinder) {
		myBinder = theBinder;
	}
	
	public BoundCodingDt(IValueSetEnumBinder<T> theBinder, T theValue) {
		myBinder = theBinder;
		setValueAsEnum(theValue);
	}

	public void setValueAsEnum(T theValue) {
		setCode(new BoundCodeDt<T>(myBinder, theValue));
		setSystem(myBinder.toSystemString(theValue));
	}
	
	public T getValueAsEnum() {
		return myBinder.fromCodeString(defaultString(getCode().getValue()), defaultString(getSystem().getValueAsString()));
	}

}
