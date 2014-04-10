package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "integer")
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
	public IntegerDt(@SimpleSetter.Parameter(name = "theInteger") int theInteger) {
		setValue(theInteger);
	}

	/**
	 * Constructor
	 * 
	 * @param theIntegerAsString A string representation of an integer
	 * @throws DataFormatException If the string is not a valid integer representation
	 */
	public IntegerDt(String theIntegerAsString) {
		setValueAsString(theIntegerAsString);
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
		} else {
			try {
				myValue = Integer.parseInt(theValue);
			} catch (NumberFormatException e) {
				throw new DataFormatException(e);
			}
		}
	}

	@Override
	public String getValueAsString() {
		if (myValue == null) {
			return null;
		}
		return Integer.toString(myValue);
	}

}
