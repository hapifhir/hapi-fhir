package org.hl7.fhir.dstu3.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hl7.fhir.instance.model.api.IBaseHasExtensions;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import ca.uhn.fhir.model.api.IElement;

public abstract class PrimitiveType<T> extends Type implements IPrimitiveType<T>, IBaseHasExtensions, IElement {

	private static final long serialVersionUID = 3L;

	private T myCoercedValue;
	private String myStringValue;

	public T getValue() {
		return myCoercedValue;
	}

	public String asStringValue() {
		return myStringValue;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getValue()).toHashCode();
	}

	public PrimitiveType<T> setValue(T theValue) {
		myCoercedValue = theValue;
		updateStringValue();
		return this;
	}

	protected void updateStringValue() {
		if (myCoercedValue == null) {
			myStringValue = null;
		} else {
			// NB this might be null
			myStringValue = encode(myCoercedValue);
		}
	}

	@Override
	public boolean isEmpty() {
		return super.isEmpty() && StringUtils.isBlank(getValueAsString());
	}

	public void fromStringValue(String theValue) {
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

	public boolean isPrimitive() {
		return true;
	}
	
	public String primitiveValue() {
		return asStringValue();
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + asStringValue() + "]";
	}

	public boolean hasValue() {
  	  return !StringUtils.isBlank(getValueAsString());
	}

	public String getValueAsString() {
		return asStringValue();
	}

	public void setValueAsString(String theValue) {
		fromStringValue(theValue);
	}

	protected Type typedCopy() {
		return copy();
	}

	public abstract Type copy();

	@Override
	public boolean equalsDeep(Base obj) {
		if (!super.equalsDeep(obj))
			return false;
		if (obj == null) {
			return false;
		}
		if (!(obj.getClass() == getClass())) {
			return false;
		}

		PrimitiveType<?> o = (PrimitiveType<?>) obj;

		EqualsBuilder b = new EqualsBuilder();
		b.append(getValue(), o.getValue());
		return b.isEquals();
	}

	@Override
	public boolean equalsShallow(Base obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj.getClass() == getClass())) {
			return false;
		}

		PrimitiveType<?> o = (PrimitiveType<?>) obj;

		EqualsBuilder b = new EqualsBuilder();
		b.append(getValue(), o.getValue());
		return b.isEquals();
	}

}
