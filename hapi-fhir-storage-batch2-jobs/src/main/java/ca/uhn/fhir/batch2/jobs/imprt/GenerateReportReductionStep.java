/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.ReductionStepFailureException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.util.StopWatch;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.InstantType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class GenerateReportReductionStep
		implements IReductionStepWorker<BulkImportJobParameters, ConsumeFilesOutcomeJson, BulkImportReportJson> {

	private final Map<String, ConsumeFilesOutcomeJson> myOutcomes = new HashMap<>();
	private int myOutcomeCount = 0;

	@Nonnull
	@Override
	public ChunkOutcome consume(
			ChunkExecutionDetails<BulkImportJobParameters, ConsumeFilesOutcomeJson> theChunkDetails) {
		ConsumeFilesOutcomeJson data = theChunkDetails.getData();

		for (Integer nextOutcomeCount :
				theChunkDetails.getData().getOutcomeCount().values()) {
			myOutcomeCount += nextOutcomeCount;
		}

		ConsumeFilesOutcomeJson existing = myOutcomes.get(data.getSourceName());
		if (existing == null) {
			myOutcomes.put(data.getSourceName(), data);
		} else {
			existing.add(data);
		}

		return ChunkOutcome.SUCCESS();
	}

	@Override
	public IReductionStepWorker<BulkImportJobParameters, ConsumeFilesOutcomeJson, BulkImportReportJson> newInstance() {
		return new GenerateReportReductionStep();
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<BulkImportJobParameters, ConsumeFilesOutcomeJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<BulkImportReportJson> theDataSink)
			throws JobExecutionFailedException {
		Date startTime = theStepExecutionDetails.getInstance().getStartTime();
		long elapsedMillis = System.currentTimeMillis() - startTime.getTime();
		long throughputPerSecond = (long) StopWatch.getThroughput(myOutcomeCount, elapsedMillis, TimeUnit.SECONDS);

		StringBuilder report = new StringBuilder();
		report.append("Bulk Import Report\n");
		report.append("------------------------------------------\n");
		report.append("Start Time      : ")
				.append(new InstantType(startTime).getValue())
				.append('\n');
		report.append("Duration        : ")
				.append(StopWatch.formatMillis(elapsedMillis))
				.append('\n');
		report.append("Storage Actions : ")
				.append(myOutcomeCount)
				.append(" (")
				.append(throughputPerSecond)
				.append("/sec)")
				.append('\n');
		report.append("------------------------------------------\n");

		boolean hasErrors = false;
		for (String source : new TreeSet<>(myOutcomes.keySet())) {
			report.append("Source: ").append(source).append("\n");
			ConsumeFilesOutcomeJson outcomes = myOutcomes.get(source);

			if (outcomes.hasOutcomes()) {
				report.append("  Outcomes:\n");
				for (var outcomeCount : new TreeMap<>(outcomes.getOutcomeCount()).entrySet()) {
					report.append("    * ")
							.append(outcomeCount.getKey())
							.append(": ")
							.append(outcomeCount.getValue())
							.append('\n');
				}
			}

			if (outcomes.hasErrors()) {
				report.append("  Errors:\n");
				for (String error : outcomes.getErrors()) {
					report.append("    * ").append(error).append('\n');
				}
				hasErrors = true;
			}
		}

		String reportString = report.toString();

		BulkImportReportJson reportJson = new BulkImportReportJson();
		reportJson.setReportMsg(reportString);

		if (hasErrors) {
			throw new ReductionStepFailureException("Job completed with at least one failure", reportJson);
		}

		theDataSink.accept(reportJson);
		return RunOutcome.SUCCESS;
	}
}
