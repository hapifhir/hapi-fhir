package ca.uhn.fhir.model.npm;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(
	creatorVisibility = JsonAutoDetect.Visibility.NONE,
	fieldVisibility = JsonAutoDetect.Visibility.NONE,
	getterVisibility = JsonAutoDetect.Visibility.NONE,
	isGetterVisibility = JsonAutoDetect.Visibility.NONE,
	setterVisibility = JsonAutoDetect.Visibility.NONE)
public class NpmPackageMetadataLiteJson {
	/**
	 * The npm package name
	 */
	@JsonProperty("name")
	private String myName;
	/**
	 * The npm package version
	 */
	@JsonProperty("version")
	private String myVersion;

	public String getName() {
		return myName;
	}

	public void setName(String theName) {
		myName = theName;
	}

	public String getVersion() {
		return myVersion;
	}

	public void setVersion(String theVersion) {
		myVersion = theVersion;
	}
}
