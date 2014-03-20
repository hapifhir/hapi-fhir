package ca.uhn.fhir.model.primitive;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "decimal")
public class DecimalDt extends BasePrimitive<BigDecimal> {

	private BigDecimal myValue;

	/**
	 * Constructor
	 */
	public DecimalDt() {
		super();
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public DecimalDt(@SimpleSetter.Parameter(name = "theValue") BigDecimal theValue) {
		setValue(theValue);
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public DecimalDt(@SimpleSetter.Parameter(name = "theValue") double theValue) {
		setValue(new BigDecimal(theValue));
	}

	/**
	 * Rounds the value to the given prevision
	 * 
	 * @see MathContext#getPrecision()
	 */
	public void round(int thePrecision) {
		if (getValue()!=null) {
			BigDecimal newValue = getValue().round(new MathContext(thePrecision));
			setValue(newValue);
		}
	}

	/**
	 * Rounds the value to the given prevision
	 * 
	 * @see MathContext#getPrecision()
	 * @see MathContext#getRoundingMode()
	 */
	public void round(int thePrecision, RoundingMode theRoundingMode) {
		if (getValue()!=null) {
			BigDecimal newValue = getValue().round(new MathContext(thePrecision, theRoundingMode));
			setValue(newValue);
		}
	}

	/**
	 * Constructor
	 */
	@SimpleSetter
	public DecimalDt(@SimpleSetter.Parameter(name = "theValue") long theValue) {
		setValue(new BigDecimal(theValue));
	}

	@Override
	public void setValueAsString(String theValue) throws DataFormatException {
		if (theValue == null) {
			myValue = null;
		} else {
			myValue = new BigDecimal(theValue);
		}
	}

	@Override
	public String getValueAsString() {
		if (myValue == null) {
			return null;
		}
		return myValue.toPlainString();
	}

	@Override
	public BigDecimal getValue() {
		return myValue;
	}

	@Override
	public void setValue(BigDecimal theValue) throws DataFormatException {
		myValue = theValue;
	}

	/**
	 * Sets a new value using an integer
	 */
	public void setValueAsInteger(int theValue) {
		myValue = new BigDecimal(theValue);
	}

	/**
	 * Gets the value as an integer, using {@link BigDecimal#intValue()}
	 */
	public int getValueAsInteger() {
		return myValue.intValue();
	}

}
