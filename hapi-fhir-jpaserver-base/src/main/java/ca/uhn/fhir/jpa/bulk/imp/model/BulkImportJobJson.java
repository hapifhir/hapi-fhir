package ca.uhn.fhir.jpa.bulk.imp.model;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BulkImportJobJson implements IModelJson {

	@JsonProperty("processingMode")
	private JobFileRowProcessingModeEnum myProcessingMode;

	@JsonProperty("fileCount")
	private int myFileCount;

	@JsonProperty("batchSize")
	private int myBatchSize;

	public JobFileRowProcessingModeEnum getProcessingMode() {
		return myProcessingMode;
	}

	public BulkImportJobJson setProcessingMode(JobFileRowProcessingModeEnum theProcessingMode) {
		myProcessingMode = theProcessingMode;
		return this;
	}

	public int getFileCount() {
		return myFileCount;
	}

	public BulkImportJobJson setFileCount(int theFileCount) {
		myFileCount = theFileCount;
		return this;
	}

	public int getBatchSize() {
		return myBatchSize;
	}

	public BulkImportJobJson setBatchSize(int theBatchSize) {
		myBatchSize = theBatchSize;
		return this;
	}
}
