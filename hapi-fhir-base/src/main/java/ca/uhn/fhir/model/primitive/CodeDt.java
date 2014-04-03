package ca.uhn.fhir.model.primitive;

import static org.apache.commons.lang3.StringUtils.defaultString;
import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "code")
public class CodeDt extends BasePrimitive<String> implements ICodedDatatype, Comparable<CodeDt> {

	private String myValue;

	/**
	 * Constructor
	 */
	public CodeDt() {
		super();
	}

	/**
	 * Constructor which accepts a string code
	 */
	@SimpleSetter()
	public CodeDt(@SimpleSetter.Parameter(name = "theCode") String theCode) {
		setValue(theCode);
	}

	@Override
	public String getValue() {
		return myValue;
	}

	@Override
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

	@Override
	public int compareTo(CodeDt theCode) {
		if (theCode == null) {
			return 1;
		}
		return defaultString(getValue()).compareTo(defaultString(theCode.getValue()));
	}

}
