package ca.uhn.fhir.model.api;


public abstract class BasePrimitive<T> extends BaseElement implements IPrimitiveDatatype<T> {

	@Override
	public boolean isEmpty() {
		return super.isBaseEmpty() && getValue() == null;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getValueAsString() + "]";
	}

}
