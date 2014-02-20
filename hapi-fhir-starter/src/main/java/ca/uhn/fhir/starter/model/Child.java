package ca.uhn.fhir.starter.model;


public class Child extends BaseElement {

	private String myReferenceType;
	
	public String getCardMaxForChildAnnotation() {
		if (getCardMax().equals("*")) {
			return "Child.MAX_UNLIMITED";
		}else {
			return getCardMax();
		}
	}

	public String getReferenceType() {
		return myReferenceType;
	}

	public void setReferenceType(String theReferenceType) {
		myReferenceType = theReferenceType;
	}

	public boolean isRepeatable() {
		return "1".equals(getCardMax()) == false;
	}
	
	public String getVariableName() {
		String elementName = getMethodName();
		return "my" + elementName;
	}

	public String getMethodName() {
		String elementName = getElementName();
		if (elementName.endsWith("[x]")) {
			elementName = elementName.substring(0, elementName.length() - 3);
		}
		elementName = elementName.substring(0,1).toUpperCase() + elementName.substring(1);
		return elementName;
	}

	
}
