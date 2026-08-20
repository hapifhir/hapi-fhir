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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Tracks which dependency packages have been installed during a single package installation
 * operation. Detects redundant dependencies — exact duplicates and older versions of
 * already-installed packages — to prevent unnecessary processing and version downgrades.
 *
 * @since 8.12.0
 */
// Created by Claude Opus 4.6
class PackageDependencyRegistry {
	private static final Logger ourLog = LoggerFactory.getLogger(PackageDependencyRegistry.class);
	private final Map<String, String> myInstalledVersionByPackageName = new HashMap<>();

	void addDependency(String thePackageName, String theVersion) {
		myInstalledVersionByPackageName.merge(
				thePackageName, Objects.requireNonNullElse(theVersion, "current"), (existing, incoming) -> {
					int cmp = PackageVersionComparator.INSTANCE.compare(incoming, existing);
					return cmp > 0 ? incoming : existing;
				});
	}

	boolean isRedundant(String thePackageName, String theVersion, PackageInstallationSpec theInstallationSpec) {
		String installedVersion = myInstalledVersionByPackageName.get(thePackageName);
		if (installedVersion == null) {
			return false;
		}
		String version = Objects.requireNonNullElse(theVersion, "current");
		if (installedVersion.equals(version)) {
			ourLog.debug(
					"Package {}#{} has already been installed. Skipping redundant processing.",
					thePackageName,
					version);
			return true;
		}
		boolean isSingleVersionMode =
				theInstallationSpec.getVersionPolicy() == PackageInstallationSpec.VersionPolicyEnum.SINGLE_VERSION;
		if (isSingleVersionMode) {
			int cmp = PackageVersionComparator.INSTANCE.compare(version, installedVersion);
			if (cmp < 0) {
				ourLog.info(
						"Skipping installation of {}#{} because a newer version ({}) has already been installed.",
						thePackageName,
						version,
						installedVersion);
				return true;
			}
		}
		return false;
	}
}
