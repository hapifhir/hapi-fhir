package ca.uhn.fhir.tinder.model;

public class AnyChild extends Child {

	@Override
	public String getReferenceType() {
		return "IDatatype";
	}

	@Override
	public String getAnnotationType() {
		return getReferenceType();
	}

	@Override
	public boolean isSingleChildInstantiable() {
		return false;
	}

	
}
