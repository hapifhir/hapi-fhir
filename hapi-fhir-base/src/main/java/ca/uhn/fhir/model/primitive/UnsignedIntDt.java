package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.CoverageIgnore;

@DatatypeDef(name = "unsignedInt", profileOf=IntegerDt.class)
@CoverageIgnore
public class UnsignedIntDt extends IntegerDt {

	/**
	 * Constructor
	 */
	public UnsignedIntDt() {
		// nothing
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public UnsignedIntDt(@SimpleSetter.Parameter(name = "theInteger") int theInteger) {
		setValue(theInteger);
	}

	/**
	 * Constructor
	 * 
	 * @param theIntegerAsString
	 *            A string representation of an integer
	 * @throws DataFormatException
	 *             If the string is not a valid integer representation
	 */
	public UnsignedIntDt(String theIntegerAsString) {
		setValueAsString(theIntegerAsString);
	}

}
