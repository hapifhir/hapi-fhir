package ca.uhn.fhir.model.api;

import org.apache.commons.lang3.StringUtils;


public class ResourceReference implements IElement {

	private String myDisplay;
	private String myReference;

	public String getDisplay() {
		return myDisplay;
	}

	public String getReference() {
		return myReference;
	}

	public void setDisplay(String theDisplay) {
		myDisplay = theDisplay;
	}

	public void setReference(String theReference) {
		myReference = theReference;
	}

	public boolean hasContent() {
		return StringUtils.isNotBlank(myDisplay) || StringUtils.isNotBlank(myReference);
	}

}
