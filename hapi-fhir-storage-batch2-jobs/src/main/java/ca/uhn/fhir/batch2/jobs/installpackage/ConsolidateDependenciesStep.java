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
import ca.uhn.fhir.batch2.api.RetryChunkLaterException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageContentsJson;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageInstallationJobParameters;
import ca.uhn.fhir.batch2.jobs.installpackage.model.PackageWithDependenciesJson;
import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.model.StatusEnum;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.packages.PackageInstallOutcomeJson;
import ca.uhn.fhir.jpa.packages.PackageInstallationSpec;
import ca.uhn.fhir.util.JsonUtil;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This is the third step of the asynchronous package install batch job. This step waits for all the child jobs
 * started by {@link InitializeDependenciesStep} to complete, then consolidates the output of all the jobs into
 * a single output object that is passed along to subsequent steps. If a child job ends in an error state,
 * that error will be logged, but the job will continue.
 */
public class ConsolidateDependenciesStep
		implements IJobStepWorker<PackageInstallationJobParameters, PackageWithDependenciesJson, PackageContentsJson> {

	private static final Logger ourLog = LoggerFactory.getLogger(ConsolidateDependenciesStep.class);

	private final IJobCoordinator myJobCoordinator;

	public ConsolidateDependenciesStep(IJobCoordinator theJobCoordinator) {
		this.myJobCoordinator = theJobCoordinator;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<PackageInstallationJobParameters, PackageWithDependenciesJson>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<PackageContentsJson> theDataSink)
			throws JobExecutionFailedException {

		PackageWithDependenciesJson packageWithDependenciesJson = theStepExecutionDetails.getData();

		List<JobInstance> jobInstances = packageWithDependenciesJson.getDependencyJobIds().stream()
				.map(myJobCoordinator::getInstance)
				.toList();

		// If any of the child jobs are still in flight, try again later
		if (isAnyDependencyJobStillInProgress(jobInstances)) {
			throw new RetryChunkLaterException(Msg.code(2907));
		}

		List<PackageInstallOutcomeJson> dependencyOutcomes = new ArrayList<>();
		for (JobInstance jobInstance : jobInstances) {
			if (jobInstance.getStatus() == StatusEnum.COMPLETED) {
				if (jobInstance.getReport() != null) {
					dependencyOutcomes.add(
							JsonUtil.deserialize(jobInstance.getReport(), PackageInstallOutcomeJson.class));
				}
			} else {
				PackageInstallationJobParameters parameters =
						JsonUtil.deserialize(jobInstance.getParameters(), PackageInstallationJobParameters.class);
				PackageInstallationSpec installationSpec = parameters.getInstallationSpec();
				String message = String.format(
						"Package installation job for %s#%s terminated in status %s",
						installationSpec.getName(), installationSpec.getVersion(), jobInstance.getStatus());
				ourLog.warn(message);
				theDataSink.recoveredError(message);
			}
		}

		// pass the bytes along unchanged
		PackageContentsJson packageContentsJson = new PackageContentsJson();
		packageContentsJson.setContents(packageWithDependenciesJson.getContents());

		// merge all the reports together
		PackageInstallOutcomeJson inboundReport = packageWithDependenciesJson.getReport();
		PackageInstallOutcomeJson outboundReport = consolidateReports(inboundReport, dependencyOutcomes);

		packageContentsJson.setReport(outboundReport);

		theDataSink.accept(packageContentsJson);

		return RunOutcome.SUCCESS;
	}

	private static boolean isAnyDependencyJobStillInProgress(List<JobInstance> jobInstances) {
		return jobInstances.stream().map(JobInstance::getStatus).anyMatch(s -> !s.isEnded());
	}

	@Nonnull
	private static PackageInstallOutcomeJson consolidateReports(
			PackageInstallOutcomeJson theInboundReport, List<PackageInstallOutcomeJson> theDependencyOutcomes) {
		PackageInstallOutcomeJson outboundReport = new PackageInstallOutcomeJson();

		outboundReport.getMessage().addAll(theInboundReport.getMessage());

		for (PackageInstallOutcomeJson outcomeJson : theDependencyOutcomes) {
			outboundReport.getMessage().addAll(outcomeJson.getMessage());
			for (Map.Entry<String, Integer> entry :
					outcomeJson.getResourcesInstalled().entrySet()) {
				outboundReport.increaseResourcesInstalled(entry.getKey(), entry.getValue());
			}
		}

		return outboundReport;
	}
}
