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
// Created by claude-opus-4-5-20250101
package ca.uhn.fhir.jpa.packages.batch;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.jpa.packages.IPackageInstallerSvc;
import ca.uhn.fhir.jpa.packages.ImplementationGuideInstallationException;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * First step in the package installation batch job.
 * This step performs the actual package installation by delegating to
 * the existing {@link IPackageInstallerSvc}.
 *
 * @since 8.2.0
 */
public class InstallPackageStep
		implements IFirstJobStepWorker<PackageInstallJobParameters, PackageInstallStepOutcomeJson> {

	private static final Logger ourLog = LoggerFactory.getLogger(InstallPackageStep.class);

	@Autowired
	private IPackageInstallerSvc myPackageInstallerSvc;

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PackageInstallJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<PackageInstallStepOutcomeJson> theDataSink)
			throws JobExecutionFailedException {

		PackageInstallJobParameters params = theStepExecutionDetails.getParameters();

		ourLog.info("Starting async package installation for {}#{}", params.getName(), params.getVersion());

		try {
			// Convert job parameters back to installation spec
			PackageInstallationSpec spec = params.toSpec();

			// Perform the installation using the existing service
			PackageInstallOutcomeJson outcome = myPackageInstallerSvc.install(spec);

			// Convert outcome to step output
			PackageInstallStepOutcomeJson stepOutcome = new PackageInstallStepOutcomeJson(outcome);
			stepOutcome.setPackageName(params.getName());
			stepOutcome.setPackageVersion(params.getVersion());

			ourLog.info(
					"Async package installation completed for {}#{}. Resources installed: {}",
					params.getName(),
					params.getVersion(),
					stepOutcome.getResourcesInstalledCount());

			// Send the outcome to the next step
			theDataSink.accept(stepOutcome);

			return RunOutcome.SUCCESS;

		} catch (ImplementationGuideInstallationException e) {
			ourLog.error(
					"Package installation failed for {}#{}: {}", params.getName(), params.getVersion(), e.getMessage());
			throw new JobExecutionFailedException(e.getMessage(), e);
		} catch (Exception e) {
			ourLog.error(
					"Unexpected error during package installation for {}#{}: {}",
					params.getName(),
					params.getVersion(),
					e.getMessage(),
					e);
			throw new JobExecutionFailedException("Package installation failed: " + e.getMessage(), e);
		}
	}

	/**
	 * For testing purposes only.
	 */
	public void setPackageInstallerSvc(IPackageInstallerSvc thePackageInstallerSvc) {
		myPackageInstallerSvc = thePackageInstallerSvc;
	}
}
