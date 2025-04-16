package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirVersionEnum;
import jakarta.annotation.Nullable;

import java.util.Objects;
import java.util.StringJoiner;

// LUKETODO:  javaodoc
// LUKETODO:  copyright headers
public class FindPackageAssetsRequest {

	private final FhirVersionEnum myFhirVersion;
	private final String myCanonicalUrl;
	private final String myPackageId;

	@Nullable
	private final String myVersion;

	public static FindPackageAssetsRequest withVersion(
			FhirVersionEnum theFhirVersion, String theCanonicalUrl, String thePackageId, String theVersion) {
		return new FindPackageAssetsRequest(theFhirVersion, theCanonicalUrl, thePackageId, theVersion);
	}

	public static FindPackageAssetsRequest noVersion(
			FhirVersionEnum theFhirVersion, String theCanonicalUrl, String thePackageId) {
		return new FindPackageAssetsRequest(theFhirVersion, theCanonicalUrl, thePackageId, null);
	}

	private FindPackageAssetsRequest(
			FhirVersionEnum myFhirVersion, String myCanonicalUrl, String myPackageId, @Nullable String myVersion) {
		this.myFhirVersion = myFhirVersion;
		this.myCanonicalUrl = myCanonicalUrl;
		this.myPackageId = myPackageId;
		this.myVersion = myVersion;
	}

	public FhirVersionEnum getFhirVersion() {
		return myFhirVersion;
	}

	public String getCanonicalUrl() {
		return myCanonicalUrl;
	}

	public String getPackageId() {
		return myPackageId;
	}

	@Nullable
	public String getVersion() {
		return myVersion;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		FindPackageAssetsRequest that = (FindPackageAssetsRequest) object;
		return myFhirVersion == that.myFhirVersion
				&& Objects.equals(myCanonicalUrl, that.myCanonicalUrl)
				&& Objects.equals(myPackageId, that.myPackageId)
				&& Objects.equals(myVersion, that.myVersion);
	}

	@Override
	public int hashCode() {
		return Objects.hash(myFhirVersion, myCanonicalUrl, myPackageId, myVersion);
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", FindPackageAssetsRequest.class.getSimpleName() + "[", "]")
				.add("myFhirVersion=" + myFhirVersion)
				.add("myCanonicalUrl='" + myCanonicalUrl + "'")
				.add("myPackageId='" + myPackageId + "'")
				.add("myVersion='" + myVersion + "'")
				.toString();
	}
}
