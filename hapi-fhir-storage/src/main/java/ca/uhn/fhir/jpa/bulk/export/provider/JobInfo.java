package ca.uhn.fhir.jpa.bulk.export.provider;

import ca.uhn.fhir.jpa.bulk.export.model.BulkExportJobStatusEnum;

public class JobInfo {
	/**
	 * Legacy - but this jobId is not the job id
	 * but the actual id of the record storing metadata of the job
	 */
	private String myJobMetadataId;
	private BulkExportJobStatusEnum myStatus;

	public String getJobMetadataId() {
		return myJobMetadataId;
	}

	public JobInfo setJobMetadataId(String theJobId) {
		myJobMetadataId = theJobId;
		return this;
	}

	public BulkExportJobStatusEnum getStatus() {
		return myStatus;
	}

	public JobInfo setStatus(BulkExportJobStatusEnum theStatus) {
		myStatus = theStatus;
		return this;
	}
}
