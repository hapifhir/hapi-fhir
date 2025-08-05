package ca.uhn.fhir.batch2.jobs.bulkmodify.framework.common;

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.ReductionStepFailureException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.jobs.bulkmodify.framework.base.BaseBulkModifyJobParameters;
import ca.uhn.fhir.batch2.model.ChunkOutcome;
import ca.uhn.fhir.util.StopWatch;
import jakarta.annotation.Nonnull;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

public class BulkModifyGenerateReportStep<T extends BaseBulkModifyJobParameters> implements IReductionStepWorker<T, BulkModifyResourcesChunkOutcomeJson, BulkModifyResourcesResultsJson> {

	private final Map<String, Integer> myResourceTypeToChangedCount = new HashMap<>();
	private final Map<String, Integer> myResourceTypeToUnchangedCount = new HashMap<>();
	private final Map<String, Integer> myResourceTypeToFailureCount = new HashMap<>();
	private final Map<String, String> myIdToFailure = new TreeMap<>();

	private int myChangedCount = 0;
	private int myUnchangedCount = 0;
	private int myFailureCount = 0;
	private boolean myFailureListForReportTruncated = false;

	@Nonnull
	@Override
	public ChunkOutcome consume(ChunkExecutionDetails<T, BulkModifyResourcesChunkOutcomeJson> theChunkDetails) {

		BulkModifyResourcesChunkOutcomeJson data = theChunkDetails.getData();

		incrementMap(data.getChangedIds(), myResourceTypeToChangedCount);
		incrementMap(data.getUnchangedIds(), myResourceTypeToUnchangedCount);
		incrementMap(data.getFailures().keySet(), myResourceTypeToFailureCount);

		myChangedCount += data.getChangedIds().size();
		myUnchangedCount += data.getUnchangedIds().size();
		myFailureCount += data.getFailures().size();

		for (Map.Entry<String, String> failure : data.getFailures().entrySet()) {
			if (myIdToFailure.size() >= 100) {
				myFailureListForReportTruncated = true;
				break;
			}

			myIdToFailure.put(failure.getKey(), failure.getValue());
		}

		return ChunkOutcome.SUCCESS();
	}

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<T, BulkModifyResourcesChunkOutcomeJson> theStepExecutionDetails, @NotNull IJobDataSink<BulkModifyResourcesResultsJson> theDataSink) throws JobExecutionFailedException, ReductionStepFailureException {
		Date startTime = theStepExecutionDetails.getInstance().getStartTime();
		long elapsedMillis = System.currentTimeMillis() - startTime.getTime();
		long changedPerSecond = (long) StopWatch.getThroughput(myChangedCount, elapsedMillis, TimeUnit.SECONDS);
		long unchangedPerSecond = (long) StopWatch.getThroughput(myUnchangedCount, elapsedMillis, TimeUnit.SECONDS);
		long failedPerSecond = (long) StopWatch.getThroughput(myFailureCount, elapsedMillis, TimeUnit.SECONDS);

		StringBuilder report = new StringBuilder();
		report.append("Bulk Modification Report\n");
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
			.append(myChangedCount)
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
		report.append("-------------------------------------------------\n");

		boolean hasErrors = false;
		for (String resourceType : getAllResourceTypes()) {
			report.append("ResourceType[").append(resourceType).append("]\n");
			if (myResourceTypeToChangedCount.containsKey(resourceType)) {
				report.append("    Changed   : ").append(myResourceTypeToChangedCount.get(resourceType)).append("\n");
			}
			if (myResourceTypeToUnchangedCount.containsKey(resourceType)) {
				report.append("    Unchanged : ").append(myResourceTypeToUnchangedCount.get(resourceType)).append("\n");
			}
			if (myResourceTypeToFailureCount.containsKey(resourceType)) {
				report.append("    Failures  : ").append(myResourceTypeToFailureCount.get(resourceType)).append("\n");
			}
		}

		if (!myIdToFailure.isEmpty()) {
			report.append("-------------------------------------------------\n");
			report.append("Failures:\n");
			for (Map.Entry<String, String> failureEntry : myIdToFailure.entrySet()) {
				report.append(failureEntry.getKey()).append(": ").append(failureEntry.getValue()).append("\n\n");
			}
		}

		report.append("-------------------------------------------------\n");

		String reportString = report.toString();

		BulkModifyResourcesResultsJson reportJson = new BulkModifyResourcesResultsJson();
		reportJson.setReport(reportString);

		theDataSink.accept(reportJson);

		return RunOutcome.SUCCESS;
	}

	@Nonnull
	private Set<String> getAllResourceTypes() {
		Set<String> resourceTypes = new TreeSet<>();
		resourceTypes.addAll(myResourceTypeToChangedCount.keySet());
		resourceTypes.addAll(myResourceTypeToUnchangedCount.keySet());
		resourceTypes.addAll(myResourceTypeToFailureCount.keySet());
		return resourceTypes;
	}

	private static void incrementMap(Collection<String> theResourceIds, Map<String, Integer> theResourceIdToCountMapToPopulate) {
		for (String changedIdValue : theResourceIds) {
			IdType changedId = new IdType(changedIdValue);
			int count = theResourceIdToCountMapToPopulate.getOrDefault(changedId.getResourceType(), 0);
			count++;
			theResourceIdToCountMapToPopulate.put(changedId.getResourceType(), count);
		}
	}
}
