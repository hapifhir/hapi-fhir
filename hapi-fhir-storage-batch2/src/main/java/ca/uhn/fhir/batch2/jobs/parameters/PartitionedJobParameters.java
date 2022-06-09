package ca.uhn.fhir.batch2.jobs.parameters;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Nullable;

public class PartitionedJobParameters implements IModelJson {
	@JsonProperty(value = "partitionId")
	@Nullable
	private RequestPartitionId myRequestPartitionId;
	@JsonProperty("batchSize")
	@Nullable
	private Integer myBatchSize;

	@Nullable
	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}

	public void setRequestPartitionId(@Nullable RequestPartitionId theRequestPartitionId) {
		myRequestPartitionId = theRequestPartitionId;
	}

	public void setBatchSize(int theBatchSize) {
		myBatchSize = theBatchSize;
	}

	@Nullable
	public Integer getBatchSize() {
		return myBatchSize;
	}
}
