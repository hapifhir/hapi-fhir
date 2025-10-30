/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.context.FhirVersionEnum;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.PageRequest;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

/**
 * Request parameters for {@link IHapiPackageCacheManager#findPackageAsset(FindPackageAssetRequest)}
 */
public class FindPackageAssetRequest {

	private static final int DEFAULT_PAGE_NUMBER = 0;
	private static final int DEFAULT_PAGE_SIZE = 50;

	private final FhirVersionEnum myFhirVersion;
	private final String myCanonicalUrl;
	private final String myPackageId;
	private final PageRequest myPageRequest;

	@Nullable
	private final String myVersion;

	public static FindPackageAssetRequest withVersion(
			FhirVersionEnum theFhirVersion, String theCanonicalUrl, String thePackageId, String theVersion) {
		return new FindPackageAssetRequest(theFhirVersion, theCanonicalUrl, thePackageId, theVersion, null);
	}

	public static FindPackageAssetRequest withVersion(
			FhirVersionEnum theFhirVersion,
			String theCanonicalUrl,
			String thePackageId,
			String theVersion,
			int thePageNumber,
			int thePageSize) {
		return new FindPackageAssetRequest(
				theFhirVersion, theCanonicalUrl, thePackageId, theVersion, PageRequest.of(thePageNumber, thePageSize));
	}

	public static FindPackageAssetRequest noVersion(
			FhirVersionEnum theFhirVersion, String theCanonicalUrl, String thePackageId) {
		return new FindPackageAssetRequest(theFhirVersion, theCanonicalUrl, thePackageId, null, null);
	}

	public static FindPackageAssetRequest noVersion(
			FhirVersionEnum theFhirVersion,
			String theCanonicalUrl,
			String thePackageId,
			int thePageNumber,
			int thePageSize) {
		return new FindPackageAssetRequest(
				theFhirVersion, theCanonicalUrl, thePackageId, null, PageRequest.of(thePageNumber, thePageSize));
	}

	private FindPackageAssetRequest(
			FhirVersionEnum theFhirVersion,
			String theCanonicalUrl,
			String thePackageId,
			@Nullable String theVersion,
			@Nullable PageRequest thePageRequest) {
		this.myFhirVersion = theFhirVersion;
		this.myCanonicalUrl = theCanonicalUrl;
		this.myPackageId = thePackageId;
		this.myVersion = theVersion;
		this.myPageRequest =
				Optional.ofNullable(thePageRequest).orElse(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE));
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

	public PageRequest getPageRequest() {
		return myPageRequest;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null || getClass() != object.getClass()) {
			return false;
		}
		FindPackageAssetRequest that = (FindPackageAssetRequest) object;
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
		return new StringJoiner(", ", FindPackageAssetRequest.class.getSimpleName() + "[", "]")
				.add("myFhirVersion=" + myFhirVersion)
				.add("myCanonicalUrl='" + myCanonicalUrl + "'")
				.add("myPackageId='" + myPackageId + "'")
				.add("myPageRequest=" + myPageRequest)
				.add("myVersion='" + myVersion + "'")
				.toString();
	}
}
