package ca.uhn.fhir.model.api;

import org.apache.commons.lang3.StringUtils;

public class BundleCategory implements IElement {

	private String myLabel;
	private String myScheme;
	private String myTerm;

	public String getLabel() {
		return myLabel;
	}

	public String getScheme() {
		return myScheme;
	}

	public String getTerm() {
		return myTerm;
	}

	public void setLabel(String theLabel) {
		myLabel = theLabel;
	}

	public void setScheme(String theScheme) {
		myScheme = theScheme;
	}

	public void setTerm(String theTerm) {
		myTerm = theTerm;
	}

	@Override
	public boolean isEmpty() {
		return StringUtils.isBlank(myLabel) && StringUtils.isBlank(myScheme) && StringUtils.isBlank(myTerm);
	}

}
