package ca.uhn.fhir.jpa.packages;

import org.apache.commons.lang3.Validate;

public class PackageSearchSpec {
	private int myStart;
	private int mySize = 50;
	private String myResourceUrl;
	private CharSequence myDescription;

	public int getSize() {
		return mySize;
	}

	public void setSize(int theSize) {
		Validate.inclusiveBetween(1, 250, theSize, "Number must be between 1-250");
		mySize = theSize;
	}

	public int getStart() {
		return myStart;
	}

	public void setStart(int theStart) {
		Validate.inclusiveBetween(0, Integer.MAX_VALUE, theStart, "Number must not be negative");
		myStart = theStart;
	}

	public String getResourceUrl() {
		return myResourceUrl;
	}

	public void setResourceUrl(String theResourceUrl) {
		myResourceUrl = theResourceUrl;
	}

	public CharSequence getDescription() {
		return myDescription;
	}

	public void setDescription(CharSequence theDescription) {
		myDescription = theDescription;
	}
}
