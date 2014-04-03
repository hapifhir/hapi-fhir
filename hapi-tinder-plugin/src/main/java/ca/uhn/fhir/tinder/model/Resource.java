package ca.uhn.fhir.tinder.model;

public class Resource extends BaseRootType {

	@Override
	public void setElementName(String theName) {
		super.setElementName(theName);
		String name = theName;
		if ("List".equals(name)) {
			name="ListResource";
		}
		setDeclaringClassNameComplete(name);
	}

}
