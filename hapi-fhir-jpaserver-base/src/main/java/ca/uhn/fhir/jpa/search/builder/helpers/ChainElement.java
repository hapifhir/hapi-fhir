package ca.uhn.fhir.jpa.search.builder.helpers;

import java.util.Objects;

public class ChainElement {
	private final String myResourceType;
	private final String mySearchParameterName;
	private final String myPath;

	public ChainElement(String theResourceType, String theSearchParameterName, String thePath) {
		this.myResourceType = theResourceType;
		this.mySearchParameterName = theSearchParameterName;
		this.myPath = thePath;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public String getPath() { return myPath; }

	public String getSearchParameterName() { return mySearchParameterName; }

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ChainElement that = (ChainElement) o;
		return myResourceType.equals(that.myResourceType) && mySearchParameterName.equals(that.mySearchParameterName) && myPath.equals(that.myPath);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myResourceType, mySearchParameterName, myPath);
	}
}
