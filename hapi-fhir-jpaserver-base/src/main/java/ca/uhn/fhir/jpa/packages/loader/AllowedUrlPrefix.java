package ca.uhn.fhir.jpa.packages.loader;

import static ca.uhn.fhir.jpa.model.util.PackageUrlConstants.WILDCARD;

public class AllowedUrlPrefix {

	public static AllowedUrlPrefix all() {
		return new AllowedUrlPrefix(WILDCARD, false, false);
	}

	private final String myUrl;
	private final boolean myIsSecure;
	private final boolean myIsPrivateNetwork;

	public AllowedUrlPrefix(String theUrl, boolean theIsSecure, boolean theIsPrivateNetwork) {
		myUrl = theUrl;
		myIsSecure = theIsSecure;
		myIsPrivateNetwork = theIsPrivateNetwork;
	}

	public String getUrl() {
		return myUrl;
	}

	public boolean isSecure() {
		return myIsSecure;
	}

	public boolean isPrivateNetwork() {
		return myIsPrivateNetwork;
	}
}
