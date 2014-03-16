package ca.uhn.fhir.model.primitive;

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "id")
public class IdDt extends BasePrimitive<String> {

	private String myValue;

	/**
	 * Create a new ID.
	 *  
	 * <p>
	 * <b>Description</b>:
	 * A whole number in the range 0 to 2^64-1 (optionally represented in hex), a uuid, an oid, or any other combination of lowercase letters, numerals, "-" and ".", with a length limit of 36 characters.
	 * </p>
	 * <p>
     * regex: [a-z0-9\-\.]{1,36}
     * </p>
	 */
	public IdDt() {
		super();
	}
	
	/**
	 * Create a new ID using a long
	 */
	public IdDt(long theId) {
		setValue(Long.toString(theId));
	}

	/**
	 * Create a new ID
	 *  
	 * <p>
	 * <b>Description</b>:
	 * A whole number in the range 0 to 2^64-1 (optionally represented in hex), a uuid, an oid, or any other combination of lowercase letters, numerals, "-" and ".", with a length limit of 36 characters.
	 * </p>
	 * <p>
     * regex: [a-z0-9\-\.]{1,36}
     * </p>
	 */
	@SimpleSetter
	public IdDt(@SimpleSetter.Parameter(name="theId") String theValue) {
		setValue(theValue);
	}

	@Override
	public String getValue() {
		return myValue;
	}

	@Override
	public String getValueAsString() {
		return myValue;
	}

	/**
	 * Set the value
	 *   
	 * <p>
	 * <b>Description</b>:
	 * A whole number in the range 0 to 2^64-1 (optionally represented in hex), a uuid, an oid, or any other combination of lowercase letters, numerals, "-" and ".", with a length limit of 36 characters.
	 * </p>
	 * <p>
     * regex: [a-z0-9\-\.]{1,36}
     * </p>
	 */
	@Override
	public void setValue(String theValue) throws DataFormatException {
		// TODO: add validation
		myValue = theValue;
	}

	/**
	 * Set the value
	 *   
	 * <p>
	 * <b>Description</b>:
	 * A whole number in the range 0 to 2^64-1 (optionally represented in hex), a uuid, an oid, or any other combination of lowercase letters, numerals, "-" and ".", with a length limit of 36 characters.
	 * </p>
	 * <p>
     * regex: [a-z0-9\-\.]{1,36}
     * </p>
	 */
	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		setValue(theValue);
	}
	
	@Override
	public String toString() {
		return myValue;
	}

}
