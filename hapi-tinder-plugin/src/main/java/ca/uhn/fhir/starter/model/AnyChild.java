package ca.uhn.fhir.starter.model;

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
