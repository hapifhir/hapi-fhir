/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
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
package ca.uhn.fhir.batch2.jobs.installpackage;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.installpackage.model.InstallationOutcomeJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageContentsJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.packages.IPackageInstallerSvc;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import ca.uhn.fhir.jpa.searchparam.registry.ISearchParamRegistryController;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.utilities.npm.NpmPackage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class ProcessPackageStep
		implements IJobStepWorker<PackageInstallationJobParameters, PackageContentsJson, InstallationOutcomeJson> {

	IPackageInstallerSvc myPackageInstallerSvc;

	ISearchParamRegistryController mySearchParamRegistryController;

	IValidationSupport myValidationSupport;

	public ProcessPackageStep(
			IPackageInstallerSvc thePackageInstallerSvc,
			ISearchParamRegistryController theSearchParamRegistryController,
			IValidationSupport theValidationSupport) {
		this.myPackageInstallerSvc = thePackageInstallerSvc;
		this.mySearchParamRegistryController = theSearchParamRegistryController;
		this.myValidationSupport = theValidationSupport;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<InstallationOutcomeJson> theDataSink)
			throws JobExecutionFailedException {
		try {
			PackageContentsJson packageContents = theStepExecutionDetails.getData();
			byte[] encodedContents = packageContents.getContents();
			byte[] decodedContents = Base64.getDecoder().decode(encodedContents);
			NpmPackage npmPackage = NpmPackage.fromPackage(new ByteArrayInputStream(decodedContents));

			// we will be appending new data to the report we received from upstream
			PackageInstallOutcomeJson packageOutcome = packageContents.getReport();

			PackageInstallationJobParameters parameters = theStepExecutionDetails.getParameters();
			PackageInstallationSpec installationSpec =
					parameters.getInstallationSpec();

			myPackageInstallerSvc.installPackage(npmPackage, installationSpec, packageOutcome);

			if (installationSpec.getInstallMode() == PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY) {
				packageOutcome
						.getMessage()
						.add(
								"Resources have been successfully installed. This is INSTALL only, so there will be no NPM packages persisted.");
			}

			// to prevent wasted effort, we only want to refresh the caches once, at the end of the root job
			if (!parameters.isDependencyJob()) {
				mySearchParamRegistryController.refreshCacheIfNecessary();

				myValidationSupport.invalidateCaches();
			}

			InstallationOutcomeJson outcome = new InstallationOutcomeJson();
			outcome.getOutcomes().add(packageOutcome);

			theDataSink.accept(outcome);
		} catch (IOException e) {
			// error handling is in the scope of a later ticket
		}
		return RunOutcome.SUCCESS;
	}
}
