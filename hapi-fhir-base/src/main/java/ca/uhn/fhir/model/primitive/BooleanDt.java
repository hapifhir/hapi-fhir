package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "boolean")
public class BooleanDt extends BasePrimitive<Boolean> {

	private Boolean myValue;

	/**
	 * Constructor
	 */
	public BooleanDt() {
		super();
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public BooleanDt(@SimpleSetter.Parameter(name="theBoolean") Boolean theBoolean) {
		setValue(theBoolean);
	}
	
	
	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		if ("true".equals(theValue)) {
			myValue = Boolean.TRUE;
		} else if ("false".equals(theValue)) {
			myValue = Boolean.FALSE;
		} else {
			throw new DataFormatException("Invalid boolean string: '" + theValue + "'");
		}
	}

	@Override
	public String getValueAsString() {
		if (myValue == null) {
			return null;
		} else if (Boolean.TRUE.equals(myValue)) {
			return "true";
		} else {
			return "false";
		}

	}

	@Override
	public void setValue(Boolean theValue) {
		myValue = theValue;
	}

	@Override
	public Boolean getValue() {
		return myValue;
	}
	
	
	

}
