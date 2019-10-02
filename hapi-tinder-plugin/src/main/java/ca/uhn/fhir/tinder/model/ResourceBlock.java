package ca.uhn.fhir.tinder.model;

import java.util.List;

public class ResourceBlock extends Child {

	public ResourceBlock() {
		super();
	}
	
	private String myForcedClassName;

	@Override
	public List<BaseElement> getChildren() {
		return super.getChildren();
	}

	@Override
	public void setElementName(String theName) {
		super.setElementName(theName);
		String name = theName;
		if ("object".equals(name)) {
			setForcedClassName("ObjectElement");
		}
	}
	
	public String getClassName() {
		if (myForcedClassName != null) {
			return myForcedClassName;
		}
		
//		return getElementName().substring(0, 1).toUpperCase() + getElementName().substring(1);
		String name = getName();
		return convertFhirPathNameToClassName(name);
	}

	public static String convertFhirPathNameToClassName(String name) {
		StringBuilder b = new StringBuilder();
		boolean first=true;
		for (String next : name.split("\\.")) {
			if (first) {
				first=false;
				continue;
			}
			b.append(next.substring(0, 1).toUpperCase() + next.substring(1));
		}
		
		return b.toString();
	}


	@Override
	public String getDeclaringClassNameCompleteForChildren() {
		return getClassName();
	}

	@Override
	public String getSingleType() {
		return getClassName();
	}

	@Override
	public boolean isBlock() {
		return true;
	}

	public void setForcedClassName(String theClassName) {
		myForcedClassName =theClassName;
	}

}
