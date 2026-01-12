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

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Final reduction step in the package installation batch job.
 * This step aggregates installation outcomes from all packages and generates
 * a combined report. The report is automatically stored in the job instance
 * by the Batch2 framework when the output is sent to the data sink.
 *
 * @since 8.2.0
 */
public class GeneratePackageInstallReportStep
		implements IReductionStepWorker<
				PackageInstallJobParameters, PackageInstallStepOutcomeJson, PackageInstallReportJson> {

	private static final Logger ourLog = LoggerFactory.getLogger(GeneratePackageInstallReportStep.class);

	private final List<PackageInstallStepOutcomeJson> myOutcomes = new ArrayList<>();

	@Nonnull
	@Override
	public ChunkOutcome consume(
			ChunkExecutionDetails<PackageInstallJobParameters, PackageInstallStepOutcomeJson> theChunkDetails) {
		PackageInstallStepOutcomeJson outcome = theChunkDetails.getData();
		myOutcomes.add(outcome);
		ourLog.debug(
				"Consumed installation outcome for {}#{}: {} resources",
				outcome.getPackageName(),
				outcome.getPackageVersion(),
				outcome.getResourcesInstalledCount());
		return ChunkOutcome.SUCCESS();
	}

	@Override
	public IReductionStepWorker<PackageInstallJobParameters, PackageInstallStepOutcomeJson, PackageInstallReportJson>
			newInstance() {
		return new GeneratePackageInstallReportStep();
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull
					StepExecutionDetails<PackageInstallJobParameters, PackageInstallStepOutcomeJson>
							theStepExecutionDetails,
			@Nonnull IJobDataSink<PackageInstallReportJson> theDataSink)
			throws JobExecutionFailedException {

		ourLog.info("Generating installation report for {} package outcome(s)", myOutcomes.size());

		// Aggregate all outcomes into a single report
		PackageInstallReportJson report = aggregateOutcomes();

		ourLog.info("Package installation job completed. {}", report.getReportMessage());

		// Send the report to the data sink - this will be stored in the job instance
		theDataSink.accept(report);

		return new RunOutcome(report.getTotalResourcesInstalled());
	}

	private PackageInstallReportJson aggregateOutcomes() {
		List<String> allMessages = new ArrayList<>();
		Map<String, Integer> aggregatedResources = new HashMap<>();
		int totalResources = 0;
		StringBuilder packageList = new StringBuilder();

		for (PackageInstallStepOutcomeJson outcome : myOutcomes) {
			allMessages.addAll(outcome.getMessages());

			for (Map.Entry<String, Integer> entry :
					outcome.getResourcesInstalled().entrySet()) {
				aggregatedResources.merge(entry.getKey(), entry.getValue(), Integer::sum);
			}

			totalResources += outcome.getResourcesInstalledCount();

			if (packageList.length() > 0) {
				packageList.append(", ");
			}
			packageList.append(outcome.getPackageName());
			if (outcome.getPackageVersion() != null) {
				packageList.append("#").append(outcome.getPackageVersion());
			}
		}

		PackageInstallReportJson report = new PackageInstallReportJson();
		report.setMessages(allMessages);
		report.setResourcesInstalled(aggregatedResources);
		report.setTotalResourcesInstalled(totalResources);

		// For single package, set the package name/version
		if (myOutcomes.size() == 1) {
			report.setPackageName(myOutcomes.get(0).getPackageName());
			report.setPackageVersion(myOutcomes.get(0).getPackageVersion());
		}

		// Generate report message
		StringBuilder msg = new StringBuilder();
		msg.append("Package installation completed. ");
		msg.append("Packages: ").append(packageList).append(". ");
		msg.append("Total resources installed: ").append(totalResources).append(".");

		if (!aggregatedResources.isEmpty()) {
			msg.append(" Breakdown: ");
			boolean first = true;
			for (Map.Entry<String, Integer> entry : aggregatedResources.entrySet()) {
				if (!first) {
					msg.append(", ");
				}
				msg.append(entry.getKey()).append("=").append(entry.getValue());
				first = false;
			}
			msg.append(".");
		}

		report.setReportMessage(msg.toString());
		return report;
	}
}
