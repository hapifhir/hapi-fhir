package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.model.api.BasePrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "string")
public class StringDt extends BasePrimitiveDatatype<String> {

	private String myValue;

	@Override
	public String getValue() {
		return myValue;
	}

	@Override
	public String getValueAsString() {
		return myValue;
	}

	@Override
	public void setValue(String theValue) throws DataFormatException {
		myValue = theValue;
	}

	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		myValue = theValue;
	}

}
