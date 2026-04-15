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

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageContentsJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
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
		implements IReductionStepWorker<
				PackageInstallationJobParameters, PackageContentsJson, PackageInstallOutcomeJson> {

	IPackageInstallerSvc myPackageInstallerSvc;

	ISearchParamRegistryController mySearchParamRegistryController;

	IValidationSupport myValidationSupport;

	PackageInstallOutcomeJson myPackageOutcome;

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
			@Nonnull IJobDataSink<PackageInstallOutcomeJson> theDataSink)
			throws JobExecutionFailedException {

		PackageInstallationJobParameters parameters = theStepExecutionDetails.getParameters();

		// to prevent wasted effort, we only want to refresh the caches once, at the end of the root job
		if (!parameters.isDependencyJob()) {
			mySearchParamRegistryController.refreshCacheIfNecessary();

			myValidationSupport.invalidateCaches();
		}

		theDataSink.accept(myPackageOutcome);

		return RunOutcome.SUCCESS;
	}

	@Override
	public IReductionStepWorker<PackageInstallationJobParameters, PackageContentsJson, PackageInstallOutcomeJson>
			newInstance() {
		return new ProcessPackageStep(myPackageInstallerSvc, mySearchParamRegistryController, myValidationSupport);
	}

	/**
	 * This is a bit of an edge case. There is only one chunk to consume.
	 * The forking of parallel sub-jobs for dependency processing has already been re-joined.
	 * But we need this final step to be a reduction step in order to be able to return a non-void response
	 * object as part of the final job status.
	 */
	@Nonnull
	@Override
	public ChunkOutcome consume(
			ChunkExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> theChunkDetails) {
		try {
			PackageContentsJson packageContents = theChunkDetails.getData();
			byte[] encodedContents = packageContents.getContents();
			byte[] decodedContents = Base64.getDecoder().decode(encodedContents);
			NpmPackage npmPackage = NpmPackage.fromPackage(new ByteArrayInputStream(decodedContents));

			// we will be appending new data to the report we received from upstream
			PackageInstallOutcomeJson packageOutcome = packageContents.getReport();

			PackageInstallationJobParameters parameters = theChunkDetails.getParameters();
			PackageInstallationSpec installationSpec = parameters.getInstallationSpec();
			if (installationSpec.getInstallMode() == PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY
					|| installationSpec.getInstallMode() == PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL) {
				myPackageInstallerSvc.installPackage(npmPackage, installationSpec, packageOutcome);

				if (installationSpec.getInstallMode() == PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY) {
					packageOutcome
							.getMessage()
							.add(
									"Resources have been successfully installed. This is INSTALL only, so there will be no NPM packages persisted.");
				}
			}

			myPackageOutcome = packageOutcome;
		} catch (IOException e) {
			// error handling is in the scope of a later ticket
		}
		return ChunkOutcome.SUCCESS();
	}
}
