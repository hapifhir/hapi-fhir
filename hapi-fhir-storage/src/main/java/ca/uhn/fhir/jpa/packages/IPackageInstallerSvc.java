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

import org.hl7.fhir.utilities.npm.IPackageCacheManager;
import org.hl7.fhir.utilities.npm.NpmPackage;

public interface IPackageInstallerSvc {

	/**
	 * Loads and installs an IG from a file on disk or the Simplifier repo using
	 * the {@link IPackageCacheManager}.
	 * <p>
	 * Installs the IG by persisting instances of the following types of resources:
	 * <p>
	 * - NamingSystem, CodeSystem, ValueSet, StructureDefinition (with snapshots),
	 * ConceptMap, SearchParameter, Subscription
	 * <p>
	 * Creates the resources if non-existent, updates them otherwise.
	 *
	 * @param theSpec The details about what should be installed
	 * @return a JSON object describing the changes that were made
	 */
	PackageInstallOutcomeJson install(PackageInstallationSpec theSpec);

	/**
	 * Removes a package from the {@link IPackageCacheManager}.
	 *
	 * @param theSpec The details about what should be removed
	 * @return a JSON object describing the changes that were made
	 */
	PackageDeleteOutcomeJson uninstall(PackageInstallationSpec theSpec);

	/**
	 * Installs a single npm package. Exposed publicly to support asynchronous operation.
	 * @param npmPackage          the package to install
	 * @param theInstallationSpec the specification providing control flags
	 * @param theOutcome          accumulates outcome messages
	 */
	void installPackage(
			NpmPackage npmPackage, PackageInstallationSpec theInstallationSpec, PackageInstallOutcomeJson theOutcome);

	/**
	 * Starts an asynchronous batch job to install a package asynchronously as a background process
	 * @param theInstallationSpec the specification defining the package to install
	 * @return the instance id of the job, needed for polling for updates
	 */
	String installAsynchronously(PackageInstallationSpec theInstallationSpec);

	/**
	 * Retrieves the current status of an asynchronous package installer job.
	 * @param theJobId the id of the job
	 * @return a JSON object containing the status report
	 */
	PackageInstallationStatusJson checkInstallationStatus(String theJobId);

	/**
	 * Checks if a dependency package's FHIR version is compatible with the server's FHIR version.
	 * If incompatible, attempts to load a version-specific variant by appending a FHIR version
	 * suffix (e.g., ".r4" for R4/R4B servers, ".r5" for R5, ".r3" for DSTU3).
	 * <p>
	 * This handles cross-version packages like {@code hl7.fhir.uv.extensions} which declare
	 * FHIR version 5.0.0 but have R4-specific counterparts like {@code hl7.fhir.uv.extensions.r4}.
	 *
	 * @param theDependency the loaded dependency package
	 * @param theId the package ID
	 * @param theVersion the package version
	 * @return the original package if compatible, or the version-specific variant if found
	 */
	NpmPackage substituteVersionSpecificPackageIfNeeded(NpmPackage theDependency, String theId, String theVersion);
}
