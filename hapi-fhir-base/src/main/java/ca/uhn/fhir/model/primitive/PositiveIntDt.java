package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.util.CoverageIgnore;

@DatatypeDef(name = "positiveInt", profileOf=IntegerDt.class)
@CoverageIgnore
public class PositiveIntDt extends IntegerDt {

	/**
	 * Constructor
	 */
	public PositiveIntDt() {
		// nothing
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public PositiveIntDt(@SimpleSetter.Parameter(name = "theInteger") int theInteger) {
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
	public PositiveIntDt(String theIntegerAsString) {
		setValueAsString(theIntegerAsString);
	}

}
