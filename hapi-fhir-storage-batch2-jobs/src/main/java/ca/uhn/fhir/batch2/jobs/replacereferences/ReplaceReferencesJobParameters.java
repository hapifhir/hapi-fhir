package ca.uhn.fhir.batch2.jobs.replacereferences;

import ca.uhn.fhir.batch2.jobs.chunk.FhirIdJson;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.provider.ReplaceReferenceRequest;
import ca.uhn.fhir.model.api.BaseBatchJobParameters;
import com.fasterxml.jackson.annotation.JsonProperty;

import static ca.uhn.fhir.jpa.api.config.JpaStorageSettings.DEFAULT_MAX_TRANSACTION_ENTRIES_FOR_WRITE_STRING;

public class ReplaceReferencesJobParameters extends BaseBatchJobParameters {
	@JsonProperty("sourceId")
	private FhirIdJson mySourceId;

	@JsonProperty("targetId")
	private FhirIdJson myTargetId;

	@JsonProperty(
			value = "batchSize",
			defaultValue = DEFAULT_MAX_TRANSACTION_ENTRIES_FOR_WRITE_STRING,
			required = false)
	private int myBatchSize;

	@JsonProperty("partitionId")
	private RequestPartitionId myPartitionId;

	public ReplaceReferencesJobParameters() {}

	public ReplaceReferencesJobParameters(ReplaceReferenceRequest theRequest) {
		mySourceId = new FhirIdJson(theRequest.sourceId);
		myTargetId = new FhirIdJson(theRequest.targetId);
		myBatchSize = theRequest.batchSize;
		myPartitionId = theRequest.partitionId;
	}

	public FhirIdJson getSourceId() {
		return mySourceId;
	}

	public void setSourceId(FhirIdJson theSourceId) {
		mySourceId = theSourceId;
	}

	public FhirIdJson getTargetId() {
		return myTargetId;
	}

	public void setTargetId(FhirIdJson theTargetId) {
		myTargetId = theTargetId;
	}

	public int getBatchSize() {
		return myBatchSize;
	}

	public void setBatchSize(int theBatchSize) {
		myBatchSize = theBatchSize;
	}

	public RequestPartitionId getPartitionId() {
		return myPartitionId;
	}

	public void setPartitionId(RequestPartitionId thePartitionId) {
		myPartitionId = thePartitionId;
	}
}
