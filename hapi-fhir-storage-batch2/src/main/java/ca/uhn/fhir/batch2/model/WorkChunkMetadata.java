package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

public class WorkChunkMetadata implements IModelJson {

	@JsonProperty("id")
	private String myId;

	@JsonProperty("sequence")
	// TODO MB danger - these repeat with a job or even a single step.  They start at 0 for every parent chunk.  Review
	// after merge.
	private int mySequence;

	@JsonProperty("status")
	private WorkChunkStatusEnum myStatus;

	@JsonProperty("jobDefinitionId")
	private String myJobDefinitionId;

	@JsonProperty("jobDefinitionVersion")
	private int myJobDefinitionVersion;

	@JsonProperty("targetStepId")
	private String myTargetStepId;

	@JsonProperty("instanceId")
	private String myInstanceId;

	public WorkChunkStatusEnum getStatus() {
		return myStatus;
	}

	public WorkChunkMetadata setStatus(WorkChunkStatusEnum theStatus) {
		myStatus = theStatus;
		return this;
	}

	public String getJobDefinitionId() {
		return myJobDefinitionId;
	}

	public WorkChunkMetadata setJobDefinitionId(String theJobDefinitionId) {
		Validate.notBlank(theJobDefinitionId);
		myJobDefinitionId = theJobDefinitionId;
		return this;
	}

	public int getJobDefinitionVersion() {
		return myJobDefinitionVersion;
	}

	public WorkChunkMetadata setJobDefinitionVersion(int theJobDefinitionVersion) {
		Validate.isTrue(theJobDefinitionVersion >= 1);
		myJobDefinitionVersion = theJobDefinitionVersion;
		return this;
	}

	public String getTargetStepId() {
		return myTargetStepId;
	}

	public WorkChunkMetadata setTargetStepId(String theTargetStepId) {
		Validate.notBlank(theTargetStepId);
		myTargetStepId = theTargetStepId;
		return this;
	}

	public String getInstanceId() {
		return myInstanceId;
	}

	public WorkChunkMetadata setInstanceId(String theInstanceId) {
		myInstanceId = theInstanceId;
		return this;
	}

	public String getId() {
		return myId;
	}

	public WorkChunkMetadata setId(String theId) {
		Validate.notBlank(theId);
		myId = theId;
		return this;
	}

	public int getSequence() {
		return mySequence;
	}

	public void setSequence(int theSequence) {
		mySequence = theSequence;
	}

	public WorkChunk toWorkChunk() {
		WorkChunk workChunk = new WorkChunk();
		workChunk.setId(getId());
		workChunk.setStatus(getStatus());
		workChunk.setInstanceId(getInstanceId());
		workChunk.setJobDefinitionId(getJobDefinitionId());
		workChunk.setJobDefinitionVersion(getJobDefinitionVersion());
		workChunk.setSequence(getSequence());
		workChunk.setTargetStepId(getTargetStepId());
		return workChunk;
	}
}
