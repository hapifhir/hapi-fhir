package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name="integer")
public class IntegerDt extends BasePrimitive<Integer> {

	private Integer myValue;

	/**
	 * Constructor
	 */
	public IntegerDt() {
		// nothing
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public IntegerDt(@SimpleSetter.Parameter(name = "theInteger") Integer theInteger) {
		setValue(theInteger);
	}
	
	@Override
	public Integer getValue() {
		return myValue;
	}

	@Override
	public void setValue(Integer theValue) {
		myValue = theValue;
	}

	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		if (theValue == null) {
			myValue = null;
		}else {
			myValue = Integer.parseInt(theValue);
		}
	}

	@Override
	public String getValueAsString() {
		if (myValue==null) {
			return null;
		}
		return Integer.toString(myValue);
	}
	
}
