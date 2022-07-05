package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.JobDefinition;
import ca.uhn.fhir.batch2.model.StatusEnum;

import java.util.Date;

public interface IJobInstance {
	String getCurrentGatedStepId();

	int getErrorCount();

	String getEstimatedTimeRemaining();

	boolean isWorkChunksPurged();

	StatusEnum getStatus();

	int getJobDefinitionVersion();

	String getInstanceId();

	Date getStartTime();

	Date getEndTime();

	Integer getCombinedRecordsProcessed();

	Double getCombinedRecordsProcessedPerSecond();

	Date getCreateTime();

	Integer getTotalElapsedMillis();

	double getProgress();

	String getErrorMessage();

	JobDefinition<?> getJobDefinition();

	boolean isCancelled();

	String getReport();
}
