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
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.npm.IPackageCacheManager;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface IHapiPackageCacheManager extends IPackageCacheManager {

	NpmPackage installPackage(PackageInstallationSpec theInstallationSpec) throws IOException;

	/**
	 * Returns the first instance of a resource by fhir version and canonical url
	 * @param theFhirVersion the fhir version
	 * @param theCanonicalUrl the canonical url
	 * @return the first instance of a resource matching these conditions, or null if none found.
	 * @deprecated Use loadPackageAssetsByUrl.
	 */
	@Deprecated(forRemoval = true, since = "8.5")
	IBaseResource loadPackageAssetByUrl(FhirVersionEnum theFhirVersion, String theCanonicalUrl);

	/**
	 * Returns all possible resources by the provided url and fhir version.
	 */
	List<IBaseResource> loadPackageAssetsByUrl(
			FhirVersionEnum theFhirVersionEnum, String theCanonicalUrl, PageRequest thePageRequest);

	List<NpmPackageAssetInfoJson> findPackageAssetInfoByUrl(FhirVersionEnum theFhirVersion, String theCanonicalUrl);

	/**
	 * Returns the first instance of a resource by fhir version and canonical url
	 * @param theFindPackageAssetRequest The request parameters
	 * @return the first instance of a resource matching these conditions, or null if none found.
	 * @deprecated Use findsPackageAssets with a proper page size/page number.
	 */
	@Deprecated(forRemoval = true, since = "8.5")
	IBaseResource findPackageAsset(FindPackageAssetRequest theFindPackageAssetRequest);

	/**
	 * Returns all package assets matching the request object.
	 * @param theRequest the request object
	 * @return a list of package assets
	 */
	List<IBaseResource> findPackageAssets(FindPackageAssetRequest theRequest);

	NpmPackageMetadataJson loadPackageMetadata(String thePackageId) throws ResourceNotFoundException;

	PackageContents loadPackageContents(String thePackageId, String theVersion);

	NpmPackageSearchResultJson search(PackageSearchSpec thePackageSearchSpec);

	PackageDeleteOutcomeJson uninstallPackage(String thePackageId, String theVersion);

	List<IBaseResource> loadPackageAssetsByType(FhirVersionEnum theFhirVersion, String theResourceType);

	class PackageContents {

		private byte[] myBytes;
		private String myPackageId;
		private String myVersion;
		private Date myLastModified;

		/**
		 * Constructor
		 */
		public PackageContents() {
			super();
		}

		public byte[] getBytes() {
			return myBytes;
		}

		public PackageContents setBytes(byte[] theBytes) {
			myBytes = theBytes;
			return this;
		}

		public String getPackageId() {
			return myPackageId;
		}

		public PackageContents setPackageId(String thePackageId) {
			myPackageId = thePackageId;
			return this;
		}

		public String getVersion() {
			return myVersion;
		}

		public PackageContents setVersion(String theVersion) {
			myVersion = theVersion;
			return this;
		}

		public Date getLastModified() {
			return myLastModified;
		}

		public PackageContents setLastModified(Date theLastModified) {
			myLastModified = theLastModified;
			return this;
		}
	}
}
