package ca.uhn.fhir.tinder.model;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;

public class SearchParameter {

	private String myDescription;
	private String myName;
	private String myPath;
	private String myType;

	public String getDescription() {
		return StringUtils.defaultString(myDescription);
	}

	public String getConstantName() {
		return "SP_" + myName.toUpperCase().replace("_[X]", "_X").replace("-[X]", "_X").replace('-', '_').replace("!", "");
	}

	public String getName() {
		return myName;
	}

	public String getNameCapitalized() {
		return WordUtils.capitalize(myName).replace("_[x]", "").replace("-[x]", "").replace("_[X]", "").replace("-[X]", "").replace('-', '_').replace("!", "");
	}

	public String getPath() {
		return StringUtils.defaultString(myPath);
	}

	public String getType() {
		return StringUtils.defaultString(myType);
	}

	public void setDescription(String theDescription) {
		myDescription = theDescription;
	}

	public void setName(String theName) {
		myName = theName;
	}

	public void setPath(String thePath) {
		myPath = thePath;
	}

	public void setType(String theType) {
		myType = theType;
	}

}
