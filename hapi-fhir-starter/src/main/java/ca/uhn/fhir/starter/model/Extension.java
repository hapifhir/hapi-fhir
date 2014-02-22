package ca.uhn.fhir.starter.model;

import java.util.Arrays;
import java.util.List;

import ca.uhn.fhir.model.api.IDatatype;

public class Extension {

	private List<Extension> myChildExtensions;
	private Class<? extends IDatatype> myDatatype;
	private String myName;
	private String myUrl;

	public Extension() {
		super();
	}

	public boolean isHasDatatype() {
		return myDatatype != null;
	}
	
	public String getDatatypeSimpleName() {
		return myDatatype.getSimpleName();
	}
	
	public String getNameType() {
		return getName().substring(0, 1).toUpperCase()+getName().substring(1);
	}
	
	public Extension(String theName, String theUrl, Class<? extends IDatatype> theDatatype) {
		setName(theName);
		setUrl(theUrl);
		setDatatype(theDatatype);
	}

	public Extension(String theName, String theUrl, Extension... theChildExtensions) {
		setName(theName);
		setUrl(theUrl);
		if (theChildExtensions != null) {
			setChildExtensions(Arrays.asList(theChildExtensions));
		}
	}

	public List<Extension> getChildExtensions() {
		return myChildExtensions;
	}

	public Class<? extends IDatatype> getDatatype() {
		return myDatatype;
	}

	public String getName() {
		return myName;
	}

	public String getUrl() {
		return myUrl;
	}

	public void setChildExtensions(List<Extension> theChildExtensions) {
		if (theChildExtensions != null && theChildExtensions.size() > 0 && myDatatype != null) {
			throw new IllegalArgumentException("Extension may not have a datatype AND child extensions");
		}
		myChildExtensions = theChildExtensions;
	}

	public void setDatatype(Class<? extends IDatatype> theDatatype) {
		if (myChildExtensions != null && myChildExtensions.size() > 0 && theDatatype != null) {
			throw new IllegalArgumentException("Extension may not have a datatype AND child extensions");
		}
		myDatatype = theDatatype;
	}

	public void setName(String theName) {
		// TODO: validate that this is a valid name (no punctuation, spaces,
		// etc.)
		myName = theName;
	}

	public void setUrl(String theUrl) {
		myUrl = theUrl;
	}
}
