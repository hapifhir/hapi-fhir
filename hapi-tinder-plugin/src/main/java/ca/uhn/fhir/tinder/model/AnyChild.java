package ca.uhn.fhir.tinder.model;

public class AnyChild extends Child {

	@Override
	public String getTypeSuffix() {
		return "";
	}

	@Override
	public boolean isSingleChildInstantiable() {
		return false;
	}

	
}
