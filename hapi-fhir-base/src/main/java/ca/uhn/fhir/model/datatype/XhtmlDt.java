package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "xhtml")
public class XhtmlDt implements IPrimitiveDatatype<String> {

	private String myValue;

	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		myValue=theValue;
	}

	@Override
	public String getValueAsString() {
		return myValue;
	}

	@Override
	public String getValue() {
		return myValue;
	}

	@Override
	public void setValue(String theValue) throws DataFormatException {
		myValue=theValue;
	}

}
