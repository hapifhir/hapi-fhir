package ca.uhn.fhir.starter.model;

public class ResourceBlock extends Child {

	public String getClassName() {
		return getElementName().substring(0,1).toUpperCase() + getElementName().substring(1);
	}
	
}
