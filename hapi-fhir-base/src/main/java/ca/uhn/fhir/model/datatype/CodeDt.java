package ca.uhn.fhir.model.datatype;

import ca.uhn.fhir.model.api.BasePrimitiveDatatype;
import ca.uhn.fhir.model.api.ICodeEnum;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "code")
public class CodeDt extends BasePrimitiveDatatype<String> implements ICodedDatatype {

	private String myValue;

	public String getValue() {
		return myValue;
	}

	public void setValue(String theValue) throws DataFormatException {
		if (theValue == null) {
			myValue = null;
		} else {
			if (theValue.length() == 0) {
				throw new DataFormatException("Value can not be empty");
			}
			if (Character.isWhitespace(theValue.charAt(0)) || Character.isWhitespace(theValue.charAt(theValue.length()-1))){
				throw new DataFormatException("Value must not contain trailing or leading whitespace");
			}
			myValue = theValue;
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
