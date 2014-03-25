package ca.uhn.fhir.tinder.model;

public class Composite extends BaseRootType {

	@Override
	public void setElementName(String theName) {
		super.setElementName(theName);
		setDeclaringClassNameComplete(theName+"Dt");
	}
}
