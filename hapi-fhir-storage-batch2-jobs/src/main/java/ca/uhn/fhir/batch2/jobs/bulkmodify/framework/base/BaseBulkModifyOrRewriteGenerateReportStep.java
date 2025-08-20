/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
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
package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.ReductionStepFailureException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesChunkOutcomeJson;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common.BulkModifyResourcesResultsJson;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.util.StopWatch;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public abstract class BaseBulkModifyOrRewriteGenerateReportStep<PT extends BaseBulkModifyJobParameters>
		implements IReductionStepWorker<PT, BulkModifyResourcesChunkOutcomeJson, BulkModifyResourcesResultsJson> {

	private final Map<String, Integer> myResourceTypeToChangedCount = new HashMap<>();
	private final Map<String, Integer> myResourceTypeToUnchangedCount = new HashMap<>();
	private final Map<String, Integer> myResourceTypeToFailureCount = new HashMap<>();
	private final Map<String, String> myIdToFailure = new TreeMap<>();

	private int myChangedCount = 0;
	private int myUnchangedCount = 0;
	private int myFailureCount = 0;
	private int myFailureListForReportTruncated = 0;
	private int myChunkRetryCount;
	private int myResourceRetryCount;

	@Nonnull
	@Override
	public ChunkOutcome consume(ChunkExecutionDetails<PT, BulkModifyResourcesChunkOutcomeJson> theChunkDetails) {

		BulkModifyResourcesChunkOutcomeJson data = theChunkDetails.getData();

		incrementMap(data.getChangedIds(), myResourceTypeToChangedCount);
		incrementMap(data.getUnchangedIds(), myResourceTypeToUnchangedCount);
		incrementMap(data.getFailures().keySet(), myResourceTypeToFailureCount);

		myChangedCount += data.getChangedIds().size();
		myUnchangedCount += data.getUnchangedIds().size();
		myFailureCount += data.getFailures().size();
		myChunkRetryCount += data.getChunkRetryCount();
		myResourceRetryCount += data.getResourceRetryCount();

		for (Map.Entry<String, String> failure : data.getFailures().entrySet()) {
			if (myIdToFailure.size() >= 100) {
				myFailureListForReportTruncated++;
				continue;
			}

			myIdToFailure.put(failure.getKey(), failure.getValue());
		}

		return ChunkOutcome.SUCCESS();
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<PT, BulkModifyResourcesChunkOutcomeJson> theStepExecutionDetails,
			@Nonnull IJobDataSink<BulkModifyResourcesResultsJson> theDataSink)
			throws JobExecutionFailedException, ReductionStepFailureException {
		Date startTime = theStepExecutionDetails.getInstance().getStartTime();
		long elapsedMillis = System.currentTimeMillis() - startTime.getTime();
		long changedPerSecond = (long) StopWatch.getThroughput(myChangedCount, elapsedMillis, TimeUnit.SECONDS);
		long unchangedPerSecond = (long) StopWatch.getThroughput(myUnchangedCount, elapsedMillis, TimeUnit.SECONDS);
		long failedPerSecond = (long) StopWatch.getThroughput(myFailureCount, elapsedMillis, TimeUnit.SECONDS);

		StringBuilder report = new StringBuilder();
		report.append(provideJobName()).append(" Report\n");
		report.append("-------------------------------------------------\n");
		report.append("Start Time                : ")
				.append(new InstantType(startTime).getValue())
				.append('\n');
		report.append("Duration                  : ")
				.append(StopWatch.formatMillis(elapsedMillis))
				.append('\n');
		report.append("Total Resources Changed   : ")
				.append(myChangedCount)
				.append(" (")
				.append(changedPerSecond)
				.append("/sec)")
				.append('\n');
		report.append("Total Resources Unchanged : ")
				.append(myUnchangedCount)
				.append(" (")
				.append(unchangedPerSecond)
				.append("/sec)")
				.append('\n');
		report.append("Total Failed Changes      : ")
				.append(myFailureCount)
				.append(" (")
				.append(failedPerSecond)
				.append("/sec)")
				.append('\n');
		report.append("Total Retried Chunks      : ").append(myChunkRetryCount).append('\n');
		report.append("Total Retried Resources   : ")
				.append(myResourceRetryCount)
				.append('\n');
		report.append("-------------------------------------------------\n");

		for (String resourceType : getAllResourceTypes()) {
			report.append("ResourceType[").append(resourceType).append("]\n");
			if (myResourceTypeToChangedCount.containsKey(resourceType)) {
				report.append("    Changed   : ")
						.append(myResourceTypeToChangedCount.get(resourceType))
						.append("\n");
			}
			if (myResourceTypeToUnchangedCount.containsKey(resourceType)) {
				report.append("    Unchanged : ")
						.append(myResourceTypeToUnchangedCount.get(resourceType))
						.append("\n");
			}
			if (myResourceTypeToFailureCount.containsKey(resourceType)) {
				report.append("    Failures  : ")
						.append(myResourceTypeToFailureCount.get(resourceType))
						.append("\n");
			}
		}

		if (!myIdToFailure.isEmpty()) {
			report.append("-------------------------------------------------\n");
			report.append("Failures:\n");
			for (Map.Entry<String, String> failureEntry : myIdToFailure.entrySet()) {
				report.append(failureEntry.getKey())
						.append(": ")
						.append(failureEntry.getValue())
						.append("\n\n");
			}
			if (myFailureListForReportTruncated > 0) {
				report.append("...truncated ")
						.append(myFailureListForReportTruncated)
						.append(" failures...\n");
			}
		}

		report.append("-------------------------------------------------\n");

		String reportString = report.toString();

		BulkModifyResourcesResultsJson reportJson = new BulkModifyResourcesResultsJson();
		reportJson.setReport(reportString);
		reportJson.setResourcesChangedCount(myChangedCount);
		reportJson.setResourcesUnchangedCount(myUnchangedCount);
		reportJson.setResourcesFailedCount(myFailureCount);

		if (!myIdToFailure.isEmpty()) {
			throw new ReductionStepFailureException(
					provideJobName() + " had " + myFailureCount + " failure(s). See report for details.", reportJson);
		}

		theDataSink.accept(reportJson);
		return RunOutcome.SUCCESS;
	}

	@Nonnull
	protected abstract String provideJobName();

	@Nonnull
	private Set<String> getAllResourceTypes() {
		Set<String> resourceTypes = new TreeSet<>();
		resourceTypes.addAll(myResourceTypeToChangedCount.keySet());
		resourceTypes.addAll(myResourceTypeToUnchangedCount.keySet());
		resourceTypes.addAll(myResourceTypeToFailureCount.keySet());
		return resourceTypes;
	}

	private static void incrementMap(
			Collection<String> theResourceIds, Map<String, Integer> theResourceIdToCountMapToPopulate) {
		for (String changedIdValue : theResourceIds) {
			IdType changedId = new IdType(changedIdValue);
			int count = theResourceIdToCountMapToPopulate.getOrDefault(changedId.getResourceType(), 0);
			count++;
			theResourceIdToCountMapToPopulate.put(changedId.getResourceType(), count);
		}
	}
}
