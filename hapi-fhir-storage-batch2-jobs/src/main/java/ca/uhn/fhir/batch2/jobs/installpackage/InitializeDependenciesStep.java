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

import ca.uhn.fhir.batch2.api.IJobCoordinator;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageContentsJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageWithDependenciesJson;
import ca.uhn.fhir.batch2.model.JobInstanceStartRequest;
import ca.uhn.fhir.jpa.batch.models.Batch2JobStartResponse;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import ca.uhn.fhir.jpa.packages.util.PackageUtils;
import ca.uhn.fhir.util.Batch2JobDefinitionConstants;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.utilities.npm.NpmPackage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class InitializeDependenciesStep
		implements IJobStepWorker<PackageInstallationJobParameters, PackageContentsJson, PackageWithDependenciesJson> {

	private final IJobCoordinator myJobCoordinator;

	public InitializeDependenciesStep(IJobCoordinator myJobCoordinator) {
		this.myJobCoordinator = myJobCoordinator;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<PackageWithDependenciesJson> theDataSink)
			throws JobExecutionFailedException {

		PackageWithDependenciesJson result = new PackageWithDependenciesJson();
		PackageInstallOutcomeJson outcome = new PackageInstallOutcomeJson();

		byte[] encodedContents = theStepExecutionDetails.getData().getContents();
		byte[] decodedContents = Base64.getDecoder().decode(encodedContents);

		// copy the raw package contents into the result for use in future steps
		result.setContents(encodedContents);
		result.setReport(outcome);

		PackageInstallationSpec installationSpec =
				theStepExecutionDetails.getParameters().getInstallationSpec();

		// exit early if the specification asks us to skip processing dependencies
		if (!installationSpec.isFetchDependencies()) {
			result.setDependencyJobIds(new ArrayList<>());
			theDataSink.accept(result);
			return RunOutcome.SUCCESS;
		}

		try {
			NpmPackage npmPackage = NpmPackage.fromPackage(new ByteArrayInputStream(decodedContents));

			List<PackageUtils.DependentPackage> dependencies =
					PackageUtils.extractDependentPackages(npmPackage, installationSpec, outcome);

			List<String> jobIds = launchChildJobs(theStepExecutionDetails, dependencies);

			result.setDependencyJobIds(jobIds);
		} catch (IOException e) {
			// error handling is in the scope of a later ticket
		}

		theDataSink.accept(result);

		return RunOutcome.SUCCESS;
	}

	private List<String> launchChildJobs(
			@Nonnull
					StepExecutionDetails<PackageInstallationJobParameters, PackageContentsJson> theStepExecutionDetails,
			List<PackageUtils.DependentPackage> theDependencies) {
		List<String> jobIds = new ArrayList<>();

		PackageInstallationSpec installationSpec =
				theStepExecutionDetails.getParameters().getInstallationSpec();

		for (PackageUtils.DependentPackage nextDependency : theDependencies) {
			// create a new installation spec, retaining all the control parameters, but targeting the dependency
			// package
			JobInstanceStartRequest startRequest = buildStartRequest(nextDependency, installationSpec);
			Batch2JobStartResponse response =
					myJobCoordinator.startInstance(theStepExecutionDetails.newSystemRequestDetails(), startRequest);
			jobIds.add(response.getInstanceId());
		}

		return jobIds;
	}

	@Nonnull
	private static JobInstanceStartRequest buildStartRequest(
			PackageUtils.DependentPackage theDependency, PackageInstallationSpec theParentInstallationSpec) {
		PackageInstallationSpec dependencySpec = new PackageInstallationSpec(theParentInstallationSpec);
		dependencySpec.setName(theDependency.name());
		dependencySpec.setVersion(theDependency.version());

		PackageInstallationJobParameters parameters = new PackageInstallationJobParameters();
		parameters.setInstallationSpec(dependencySpec);
		parameters.setDependencyJob(true);

		return new JobInstanceStartRequest(Batch2JobDefinitionConstants.INSTALL_PACKAGE, parameters);
	}
}
