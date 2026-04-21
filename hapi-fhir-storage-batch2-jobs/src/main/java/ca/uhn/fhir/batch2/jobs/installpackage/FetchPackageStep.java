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

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageContentsJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.packages.IHapiPackageCacheManager;
import ca.uhn.fhir.jpa.packages.IPackageInstallerSvc;
import ca.uhn.fhir.jpa.packages.NpmPackageUtils;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import jakarta.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.utilities.npm.NpmPackage;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * This is the first step of an asynchronous package install batch job. It fetches the package to be installed from
 * a source location given in the specification object and passes it to subsequent steps.
 */
public class FetchPackageStep implements IFirstJobStepWorker<PackageInstallationJobParameters, PackageContentsJson> {

	private final IHapiPackageCacheManager myPackageCacheManager;
	private final IPackageInstallerSvc myPackageInstallerSvc;

	public FetchPackageStep(
			IHapiPackageCacheManager thePackageCacheManager, IPackageInstallerSvc thePackageInstallerSvc) {
		this.myPackageCacheManager = thePackageCacheManager;
		this.myPackageInstallerSvc = thePackageInstallerSvc;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PackageInstallationJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<PackageContentsJson> theDataSink)
			throws JobExecutionFailedException {

		try {
			PackageInstallationSpec installationSpec =
					theStepExecutionDetails.getParameters().getInstallationSpec();
			NpmPackage npmPackage = myPackageCacheManager.installPackage(installationSpec);

			if (theStepExecutionDetails.getParameters().isDependencyJob()) {
				// Adjust the dependency package as needed to match the FHIR version of the server
				npmPackage = myPackageInstallerSvc.substituteVersionSpecificPackageIfNeeded(
						npmPackage, installationSpec.getName(), installationSpec.getVersion());
			}

			if (npmPackage == null) {
				String message = formatErrorMessage(installationSpec);
				throw new JobExecutionFailedException(Msg.code(2915) + message);
			}

			// We need to convert the package back to bytes so we can serialize it between steps
			// Note that these are not the identical bytes that were found by the cache,
			// but they should be equivalent
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			npmPackage.save(outputStream);

			PackageInstallOutcomeJson outcome = new PackageInstallOutcomeJson();
			outcome.getMessage().addAll(NpmPackageUtils.getProcessingMessages(npmPackage));

			PackageContentsJson contents = new PackageContentsJson();
			contents.setContents(Base64.getEncoder().encode(outputStream.toByteArray()));
			contents.setReport(outcome);
			theDataSink.accept(contents);
		} catch (Exception e) {
			throw new JobExecutionFailedException(Msg.code(2916) + "Error occurred while retrieving package", e);
		}

		return RunOutcome.SUCCESS;
	}

	@Nonnull
	private static String formatErrorMessage(PackageInstallationSpec theInstallationSpec) {
		String message;
		if (StringUtils.isNotBlank(theInstallationSpec.getPackageUrl())) {
			message = String.format("Unable to retrieve NPM package from URL %s.", theInstallationSpec.getPackageUrl());
		} else if (theInstallationSpec.getPackageContents() != null) {
			message = "Unable to install NPM package from contained bytes.";
		} else if (StringUtils.isNoneBlank(theInstallationSpec.getName(), theInstallationSpec.getVersion())) {
			message = String.format(
					"Unable to retrieve NPM package %s#%s.",
					theInstallationSpec.getName(), theInstallationSpec.getVersion());
		} else {
			message = "Unable to identify NPM package to install.";
		}
		return message;
	}
}
