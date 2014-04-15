package ca.uhn.fhir.model.api;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public abstract class BasePrimitive<T> extends BaseElement implements IPrimitiveDatatype<T> {

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && getValue() == null;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getValueAsString() + "]";
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getValue()).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj.getClass() == getClass())) {
			return false;
		}

		BasePrimitive<?> o = (BasePrimitive<?>)obj;
		
		EqualsBuilder b = new EqualsBuilder();
		b.append(getValue(), o.getValue());
		return b.isEquals();
	}
}
