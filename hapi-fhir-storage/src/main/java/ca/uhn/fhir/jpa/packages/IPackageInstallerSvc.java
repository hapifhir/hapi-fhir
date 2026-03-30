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

import org.hl7.fhir.utilities.npm.NpmPackage;

public interface IPackageInstallerSvc {

	PackageInstallOutcomeJson install(PackageInstallationSpec theSpec);

	PackageDeleteOutcomeJson uninstall(PackageInstallationSpec theSpec);

	/**
	 * Installs a single npm package. Exposed publicly to support asynchronous operation.
	 * @param npmPackage          the package to install
	 * @param theInstallationSpec the specification providing control flags
	 * @param theOutcome          accumulates outcome messages
	 */
	void installPackage(
			NpmPackage npmPackage, PackageInstallationSpec theInstallationSpec, PackageInstallOutcomeJson theOutcome);
}
