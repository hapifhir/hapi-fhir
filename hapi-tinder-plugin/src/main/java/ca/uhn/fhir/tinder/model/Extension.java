package ca.uhn.fhir.tinder.model;

import ca.uhn.fhir.i18n.Msg;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Extension extends Child {

	private List<Extension> myChildExtensions;

	private String myUrl;

	public Extension() {
		super();
	}

	public List<Extension> getChildExtensionsWithChildren() {
		ArrayList<Extension> retVal = new ArrayList<Extension>();
		for (Extension next : getChildExtensions()) {
			if (next.getChildExtensions().size() > 0) {
				retVal.add(next);
			}
		}
		return retVal;
	}
	
	public Extension(String theName, String theUrl, Extension... theChildExtensions) {
		setName(theName);
		setUrl(theUrl);
		setCardMin("0");
		setCardMax("*");
		if (theChildExtensions != null) {
			setChildExtensions(Arrays.asList(theChildExtensions));
		}
	}

	public Extension(String theName, String theUrl, String theDatatype) {
		setName(theName);
		setUrl(theUrl);
		setTypeFromString(theDatatype);
		setCardMin("0");
		setCardMax("*");
	}

	public List<Extension> getChildExtensions() {
		if (myChildExtensions == null) {
			myChildExtensions = new ArrayList<Extension>();
		}
		return myChildExtensions;
	}

	public String getNameType() {
		return getName().substring(0, 1).toUpperCase() + getName().substring(1);
	}

	public String getUrl() {
		return myUrl;
	}

	public boolean isHasChildExtensions() {
		return getChildExtensions().size() > 0;
	}

	public void setChildExtensions(List<Extension> theChildExtensions) {
		if (theChildExtensions != null && theChildExtensions.size() > 0 && getType().size() > 0) {
			throw new IllegalArgumentException(Msg.code(186) + "Extension may not have a datatype AND child extensions");
		}
		myChildExtensions = theChildExtensions;
	}

	@Override
	public void setElementNameAndDeriveParentElementName(String theName) {
		super.setElementNameAndDeriveParentElementName(theName);
		if (getName() == null) {
			super.setName(theName);
		}
	}

	@Override
	public void setName(String theName) {
		super.setName(theName);
		if (getElementName() == null) {
			super.setElementNameAndDeriveParentElementName(theName);
		}
	}

	@Override
	public void setTypeFromString(String theType) {
		if (myChildExtensions != null && myChildExtensions.size() > 0 && StringUtils.isNotBlank(theType)) {
			throw new IllegalArgumentException(Msg.code(187) + "Extension may not have a datatype AND child extensions");
		}
		super.setTypeFromString(theType);
	}

	public void setUrl(String theUrl) {
		myUrl = theUrl;
	}

	@Override
	public String getSingleType() {
		if (isHasChildExtensions()) {
			return getNameType();
		}
		return super.getSingleType();
	}
}
