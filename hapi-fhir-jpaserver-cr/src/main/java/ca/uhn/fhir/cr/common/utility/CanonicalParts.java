package ca.uhn.fhir.cr.common.utility;

public final class CanonicalParts {
	private final String url;
	private final String idPart;
	private final String resourceType;
	private final String version;
	private final String fragment;

	CanonicalParts(String url, String idPart, String resourceType, String version, String fragment) {
		this.url = url;
		this.idPart = idPart;
		this.resourceType = resourceType;
		this.version = version;
		this.fragment = fragment;
	}

	public String url() {
		return this.url;
	}

	public String idPart() {
		return this.idPart;
	}

	public String resourceType() {
		return this.resourceType;
	}

	public String version() {
		return this.version;
	}

	public String fragment() {
		return this.fragment;
	}
}
