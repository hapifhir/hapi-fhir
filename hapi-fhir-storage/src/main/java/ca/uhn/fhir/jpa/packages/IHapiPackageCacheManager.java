/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.utilities.npm.IPackageCacheManager;
import org.hl7.fhir.utilities.npm.NpmPackage;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface IHapiPackageCacheManager extends IPackageCacheManager {

	NpmPackage installPackage(PackageInstallationSpec theInstallationSpec) throws IOException;

	IBaseResource loadPackageAssetByUrl(FhirVersionEnum theFhirVersion, String theCanonicalUrl);

	/**
	 * Load the package specified by the id and version specified by the parameters.
	 * <p/>
	 * Depending on the implementation logic, this may be from the package cache, or resolved from another service.
	 *
	 * @param thePackageId         The package id
	 * @param thePackageVersion    The package version. If this is null, the implementation logic will attempt to
	 *                                resolve an appropriate version based on the package id.
	 * @param theShouldUpdateCache Indicates whether the cache should be updated
	 * @return the NPM Package
	 * @throws FHIRException if the package cannot be located
	 * @throws IOException if the package cannot be processed
	 */
	NpmPackage loadPackage(String thePackageId, String thePackageVersion, boolean theShouldUpdateCache)
			throws FHIRException, IOException;

	/**
	 * Returns all possible resources by the provided url and fhir version.
	 */
	List<IBaseResource> loadPackageAssetsByUrl(
			FhirVersionEnum theFhirVersionEnum, String theCanonicalUrl, PageRequest thePageRequest);

	List<NpmPackageAssetInfoJson> findPackageAssetInfoByUrl(FhirVersionEnum theFhirVersion, String theCanonicalUrl);

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
