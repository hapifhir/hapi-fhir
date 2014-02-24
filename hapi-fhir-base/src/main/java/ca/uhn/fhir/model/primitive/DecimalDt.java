package ca.uhn.fhir.model.primitive;

import java.math.BigDecimal;

import ca.uhn.fhir.model.api.BasePrimitiveDatatype;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "decimal")
public class DecimalDt extends BasePrimitiveDatatype<BigDecimal> {

	private BigDecimal myValue;

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

	public void setValueAsInteger(int theValue) {
		myValue = new BigDecimal(theValue);
	}

	public int getValueAsInteger() {
		return myValue.intValue();
	}

}
