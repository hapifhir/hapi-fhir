package ca.uhn.fhir.tinder.model;

public class Resource extends BaseRootType {

	@Override
	public void setElementName(String theName) {
		super.setElementName(theName);
		String name = correctName(theName);
		setDeclaringClassNameComplete(name);
	}

	public static String correctName(String theName) {
		String name = theName;
		if ("List".equals(name)) {
			name="ListResource";
		}
		if (name.endsWith(".List")) {
			name = name + "Resource";
		}
		return name;
	}

}
