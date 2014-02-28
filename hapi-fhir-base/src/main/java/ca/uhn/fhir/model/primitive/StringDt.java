package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.model.api.BaseElement;
import ca.uhn.fhir.model.api.IPrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "string")
public class StringDt  extends BaseElement implements IPrimitiveDatatype<String> {

	private String myValue;

	/**
	 * Create a new String
	 */
	public StringDt() {
		super();
	}

	/**
	 * Create a new String
	 */
	@SimpleSetter
	public StringDt(@SimpleSetter.Parameter(name="theString") String theValue) {
		myValue=theValue;
	}

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

	/**
	 * Returns the value of this string, or <code>null</code>
	 */
	@Override
	public String toString() {
		return myValue;
	}
	

}
