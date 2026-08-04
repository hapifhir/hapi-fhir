package ca.uhn.fhir.jpa.packages.loader;

import java.util.List;

public class PackageLoaderSettings {

	/**
	 * A list of url (prefixes) that are allowed
	 * for packageloader.
	 */
	private List<String> myAllowList;

	public List<String> getAllowList() {
		return myAllowList;
	}

	public void setAllowList(List<String> theAllowList) {
		myAllowList = theAllowList;
	}
}
