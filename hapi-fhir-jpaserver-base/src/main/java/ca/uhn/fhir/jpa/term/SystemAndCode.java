package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.util.UrlUtil;

public record SystemAndCode(UrlUtil.CanonicalUrlParts system, String code) {

	public SystemAndCode(String theSystem, String theVersion, String theCode) {
		this(UrlUtil.parseCanonicalUrl(theSystem, theVersion), theCode);
	}

	public SystemAndCode(String theSystem, String theCode) {
		this(UrlUtil.parseCanonicalUrl(theSystem), theCode);
	}
}
