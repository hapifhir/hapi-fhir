package ca.uhn.fhir.batch2.importpull.models;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Batch2BulkImportPullJobParameters implements IModelJson {

	@JsonProperty("jobId")
	private String myJobId;

	@JsonProperty("batchSize")
	private long myBatchSize;

	public String getJobId() {
		return myJobId;
	}

	public void setJobId(String theJobId) {
		myJobId = theJobId;
	}

	public long getBatchSize() {
		return myBatchSize;
	}

	public void setBatchSize(long theBatchSize) {
		myBatchSize = theBatchSize;
	}
}
