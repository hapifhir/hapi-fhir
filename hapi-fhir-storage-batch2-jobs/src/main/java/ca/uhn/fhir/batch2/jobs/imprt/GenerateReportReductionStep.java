package ca.uhn.fhir.batch2.jobs.imprt;

/*
public class ReplaceReferenceUpdateTaskReducerStep<PT extends ReplaceReferencesJobParameters>
		implements IReductionStepWorker<PT, ReplaceReferencePatchOutcomeJson, ReplaceReferenceResultsJson> {

 */

import ca.uhn.fhir.batch2.api.ChunkExecutionDetails;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IReductionStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
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

public class GenerateReportReductionStep implements IReductionStepWorker<BulkImportJobParameters, ConsumeFilesOutcomeJson, BulkImportReportJson> {

	private Map<String, ConsumeFilesOutcomeJson> myOutcomes = new HashMap<>();
	private int myOutcomeCount = 0;

	@Nonnull
	@Override
	public ChunkOutcome consume(ChunkExecutionDetails<BulkImportJobParameters, ConsumeFilesOutcomeJson> theChunkDetails) {
		ConsumeFilesOutcomeJson data = theChunkDetails.getData();

		ConsumeFilesOutcomeJson existing = myOutcomes.get(data.getSourceName());
		if (existing == null) {
			myOutcomes.put(data.getSourceName(), data);
		} else {
			existing.add(data);
		}

		for (ConsumeFilesOutcomeJson outcome : myOutcomes.values()) {
			for (Integer nextOutcomeCount : outcome.getOutcomeCount().values()) {
				myOutcomeCount += nextOutcomeCount;
			}
		}

		return ChunkOutcome.SUCCESS();
	}

	@Override
	public IReductionStepWorker<BulkImportJobParameters, ConsumeFilesOutcomeJson, BulkImportReportJson> newInstance() {
		return new GenerateReportReductionStep();
	}

	@Nonnull
	@Override
	public RunOutcome run(@Nonnull StepExecutionDetails<BulkImportJobParameters, ConsumeFilesOutcomeJson> theStepExecutionDetails, @Nonnull IJobDataSink<BulkImportReportJson> theDataSink) throws JobExecutionFailedException {
		Date startTime = theStepExecutionDetails.getInstance().getStartTime();
		long elapsedMillis = System.currentTimeMillis() - startTime.getTime();
		long throughputPerSecond = (long) StopWatch.getThroughput(myOutcomeCount, elapsedMillis, TimeUnit.SECONDS);

		StringBuilder report = new StringBuilder();
		report.append("Bulk Import Report\n");
		report.append("------------------------------------------\n");
		report.append("Start Time      : ").append(new InstantType(startTime).getValue()).append('\n');
		report.append("Duration        : ").append(StopWatch.formatMillis(elapsedMillis)).append('\n');
		report.append("Storage Actions : ").append(myOutcomeCount).append(" (").append(throughputPerSecond).append("/sec)").append('\n');
		report.append("------------------------------------------\n");

		for (String source : new TreeSet<>(myOutcomes.keySet())) {
			report.append("Source: ").append(source).append("\n");
			ConsumeFilesOutcomeJson outcomes = myOutcomes.get(source);

			if (outcomes.hasOutcomes()) {
				report.append("  Outcomes:\n");
				for (var outcomeCount : new TreeMap<>(outcomes.getOutcomeCount()).entrySet()) {
					report.append("    * ").append(outcomeCount.getKey()).append(": ").append(outcomeCount.getValue()).append('\n');
				}
			}

			if (outcomes.hasErrors()) {
				report.append("  Errors:\n");
				for (String error : outcomes.getErrors()) {
					report.append("    * ").append(error).append('\n');
				}
			}
		}

		String reportString = report.toString();

		BulkImportReportJson reportJson = new BulkImportReportJson();
		reportJson.setReportMsg(reportString);
		theDataSink.accept(reportJson);

		return RunOutcome.SUCCESS;
	}
}
