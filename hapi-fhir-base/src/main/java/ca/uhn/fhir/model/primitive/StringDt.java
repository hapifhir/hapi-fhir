package ca.uhn.fhir.model.primitive;

import org.apache.commons.lang3.StringUtils;

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.model.api.IQueryParameterType;
import ca.uhn.fhir.model.api.annotation.DatatypeDef;
import ca.uhn.fhir.model.api.annotation.SimpleSetter;
import ca.uhn.fhir.parser.DataFormatException;

@DatatypeDef(name = "string")
public class StringDt extends BasePrimitive<String> implements IQueryParameterType {

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

	public String getValueNotNull() {
		return StringUtils.defaultString(myValue);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((myValue == null) ? 0 : myValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StringDt other = (StringDt) obj;
		if (myValue == null) {
			if (other.myValue != null)
				return false;
		} else if (!myValue.equals(other.myValue))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setValueAsQueryToken(String theParameter) {
		setValue(theParameter);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getValueAsQueryToken() {
		return getValue();
	}
	
	/**
	 * Returns <code>true</code> if this datatype has no extensions, and has either a <code>null</code>
	 * value or an empty ("") value.
	 */
	@Override
	public boolean isEmpty() {
		return super.isEmpty() && StringUtils.isBlank(getValue());
	}

}
