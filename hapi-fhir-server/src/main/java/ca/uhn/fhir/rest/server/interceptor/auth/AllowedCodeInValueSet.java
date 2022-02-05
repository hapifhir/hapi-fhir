package ca.uhn.fhir.rest.server.interceptor.auth;

import javax.annotation.Nonnull;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

class AllowedCodeInValueSet {
	private final String myResourceName;
	private final String mySearchParameterName;
	private final String myValueSetUrl;
	private final boolean myNegate;

	public AllowedCodeInValueSet(@Nonnull String theResourceName, @Nonnull String theSearchParameterName, @Nonnull String theValueSetUrl, boolean theNegate) {
		assert isNotBlank(theResourceName);
		assert isNotBlank(theSearchParameterName);
		assert isNotBlank(theValueSetUrl);

		myResourceName = theResourceName;
		mySearchParameterName = theSearchParameterName;
		myValueSetUrl = theValueSetUrl;
		myNegate = theNegate;
	}

	public String getResourceName() {
		return myResourceName;
	}

	public String getSearchParameterName() {
		return mySearchParameterName;
	}

	public String getValueSetUrl() {
		return myValueSetUrl;
	}

	public boolean isNegate() {
		return myNegate;
	}
}
