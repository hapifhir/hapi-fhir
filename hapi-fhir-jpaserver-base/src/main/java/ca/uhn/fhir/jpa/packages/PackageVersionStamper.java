/*-
 * #%L
 * HAPI FHIR JPA Server
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.MetaUtil;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

/**
 * Handles reading and writing the package source stamp ({@code packageName|packageVersion})
 * on the {@code meta.source} field of installed resources. Also provides version comparison
 * to detect when an incoming package version is older than the version already installed.
 *
 * @since 8.12.0
 */
// Created by Claude Opus 4.6
class PackageVersionStamper {
	private static final Logger ourLog = LoggerFactory.getLogger(PackageVersionStamper.class);
	private static final String OUR_PIPE_CHARACTER = "|";

	private final FhirContext myFhirContext;

	PackageVersionStamper(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	/**
	 * Stamps the resource's {@code meta.source} with {@code packageName|packageVersion}
	 * from the given installation spec.
	 */
	void stampPackageSource(IBaseResource theResource, PackageInstallationSpec theSpec) {
		if (theSpec != null) {
			String metaSourceUrl = theSpec.getName() + OUR_PIPE_CHARACTER + theSpec.getVersion();
			MetaUtil.setSource(myFhirContext, theResource, metaSourceUrl);
		}
	}

	/**
	 * Parses the {@code meta.source} field of a resource to extract the package name and version.
	 *
	 * @return the parsed parts, or empty if the source is not in {@code name|version} format
	 */
	Optional<UrlUtil.CanonicalUrlParts> parsePackageSource(IBaseResource theResource) {
		String source = MetaUtil.getSource(myFhirContext, theResource);
		String sourceUri = MetaUtil.extractSourceUriOrEmpty(source);
		if (sourceUri.isEmpty()) {
			return Optional.empty();
		}
		UrlUtil.CanonicalUrlParts parts = UrlUtil.parseCanonicalUrl(sourceUri);
		if (parts.versionId().isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(parts);
	}

	/**
	 * Returns {@code true} if the incoming package version is older than the version
	 * stamped on the existing resource's {@code meta.source}. Returns {@code false}
	 * if the existing resource has no package source stamp or belongs to a different package.
	 */
	boolean isOlderPackageVersion(PackageInstallationSpec theIncomingSpec, IBaseResource theExistingResource) {
		Optional<UrlUtil.CanonicalUrlParts> existingPackage = parsePackageSource(theExistingResource);
		if (existingPackage.isEmpty()) {
			return false;
		}
		String existingPackageName = existingPackage.get().url();
		String existingPackageVersion = existingPackage.get().versionId().get();

		String incomingName = theIncomingSpec.getName();
		String incomingVersion = theIncomingSpec.getVersion();
		if (incomingVersion == null || !existingPackageName.equals(incomingName)) {
			return false;
		}

		int cmp = PackageVersionComparator.INSTANCE.compare(incomingVersion, existingPackageVersion);
		if (cmp < 0) {
			ourLog.info(
					"Skipping update of {} because existing resource was installed from a newer version "
							+ "of the same package (existing: {}|{}, incoming: {}|{})",
					theExistingResource.getIdElement().toUnqualifiedVersionless(),
					existingPackageName,
					existingPackageVersion,
					incomingName,
					incomingVersion);
			return true;
		}
		return false;
	}
}
