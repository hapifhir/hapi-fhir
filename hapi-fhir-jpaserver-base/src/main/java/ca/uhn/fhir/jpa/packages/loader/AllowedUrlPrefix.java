package ca.uhn.fhir.jpa.packages.loader;

import java.util.Objects;

import static ca.uhn.fhir.jpa.model.util.PackageUrlConstants.WILDCARD;

public class AllowedUrlPrefix {

	public static AllowedUrlPrefix all() {
		return new AllowedUrlPrefix(WILDCARD, false);
	}

	private final String myUrl;
	private final boolean myIsPrivateNetwork;

	public AllowedUrlPrefix(String theUrl, boolean theIsPrivateNetwork) {
		myUrl = theUrl;
		myIsPrivateNetwork = theIsPrivateNetwork;
	}

	public String getUrl() {
		return myUrl;
	}

	public boolean isPrivateNetwork() {
		return myIsPrivateNetwork;
	}

	@Override
	public String toString() {
		return "url:" + myUrl + "|" + "private:" + myIsPrivateNetwork;
	}

	@Override
	public boolean equals(Object theOther) {
		if (theOther == null) {
			return false;
		}
		if (theOther instanceof AllowedUrlPrefix that) {
			return Objects.equals(getUrl(), that.getUrl()) && this.isPrivateNetwork() == that.isPrivateNetwork();
		}

		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(myUrl, myIsPrivateNetwork);
	}
}
