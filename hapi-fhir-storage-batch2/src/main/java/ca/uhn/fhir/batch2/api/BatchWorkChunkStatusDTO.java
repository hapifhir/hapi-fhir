package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.batch2.model.WorkChunkStatusEnum;

import java.util.Date;

public class BatchWorkChunkStatusDTO {
	public final String stepId;
	public final WorkChunkStatusEnum status;
	public final Date start;
	public final Date stop;
	public final Double avg;
	public final Long totalChunks;

	public BatchWorkChunkStatusDTO(
			String theStepId,
			WorkChunkStatusEnum theStatus,
			Date theStart,
			Date theStop,
			Double theAvg,
			Long theTotalChunks) {
		stepId = theStepId;
		status = theStatus;
		start = theStart;
		stop = theStop;
		avg = theAvg;
		totalChunks = theTotalChunks;
	}
}
