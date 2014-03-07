package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "code")
public class CodeDt extends BasePrimitive<String> implements ICodedDatatype {

	private String myValue;

	public String getValue() {
		return myValue;
	}

	public void setValue(String theValue) throws DataFormatException {
		if (theValue == null) {
			myValue = null;
		} else {
			String newValue = theValue.trim();
			myValue = newValue;
		}
	}

	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		setValue(theValue);
	}

	@Override
	public String getValueAsString() {
		return getValue();
	}

}
