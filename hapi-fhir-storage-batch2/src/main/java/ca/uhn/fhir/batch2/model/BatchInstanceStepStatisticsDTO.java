package ca.uhn.fhir.batch2.model;

import java.util.Map;

public class BatchInstanceStepStatisticsDTO {

	private final Map<String, StepStatistics> myStepIdToStatistics;

	public BatchInstanceStepStatisticsDTO(Map<String, StepStatistics> theStepIdToStatistics) {
		myStepIdToStatistics = theStepIdToStatistics;
	}

	public StepStatistics get(String theStepId) {
		return myStepIdToStatistics.get(theStepId);
	}

	public record StepStatistics(int chunkCount, long millisElapsed) {}
}
