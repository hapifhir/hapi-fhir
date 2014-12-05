package org.hl7.fhir.instance.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import ca.uhn.fhir.model.api.BasePrimitive;
import ca.uhn.fhir.parser.DataFormatException;

public abstract class PrimitiveType<T> extends Type {

	private static final long serialVersionUID = 2L;

	private T myCoercedValue;
	private String myStringValue;

	@Override
	public boolean equals(Object theObj) {
		if (theObj == null) {
			return false;
		}
		if (!(theObj.getClass() == getClass())) {
			return false;
		}

		BasePrimitive<?> o = (BasePrimitive<?>) theObj;

		EqualsBuilder b = new EqualsBuilder();
		b.append(getValue(), o.getValue());
		return b.isEquals();
	}

	public T getValue() {
		return myCoercedValue;
	}

	public String asStringValue() throws DataFormatException {
		return myStringValue;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getValue()).toHashCode();
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && getValue() == null;
	}

	public void setValue(T theValue) throws DataFormatException {
		myCoercedValue = theValue;
		updateStringValue();
	}

	protected void updateStringValue() {
		if (myCoercedValue == null) {
			myStringValue = null;
		} else {
			// NB this might be null
			myStringValue = encode(myCoercedValue);
		}
	}

	public void fromStringValue(String theValue) throws DataFormatException {
		if (theValue == null) {
			myCoercedValue = null;
		} else {
			// NB this might be null
			myCoercedValue = parse(theValue);
		}
		myStringValue = theValue;
	}

	/**
	 * Subclasses must override to convert an encoded representation of this datatype into a "coerced" one
	 * 
	 * @param theValue
	 *            Will not be null
	 * @return May return null if the value does not correspond to anything
	 */
	protected abstract T parse(String theValue);

	/**
	 * Subclasses must override to convert a "coerced" value into an encoded one.
	 * 
	 * @param theValue
	 *            Will not be null
	 * @return May return null if the value does not correspond to anything
	 */
	protected abstract String encode(T theValue);

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + asStringValue() + "]";
	}

	public boolean hasValue() {
		return !isEmpty();
	}
	
	public String getValueAsString() {
		return asStringValue();
	}
	
	public void setValueAsString(String theValue) {
		fromStringValue(theValue);
	}
}
