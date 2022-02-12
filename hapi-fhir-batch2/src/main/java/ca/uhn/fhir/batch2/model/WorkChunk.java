package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.Map;

public class WorkChunk implements IModelJson {

	@JsonProperty("id")
	private String myId;
	@JsonProperty("status")
	private StatusEnum myStatus;
	@JsonProperty("jobDefinitionId")
	private String myJobDefinitionId;
	@JsonProperty("jobDefinitionVersion")
	private int myJobDefinitionVersion;
	@JsonProperty("targetStepId")
	private String myTargetStepId;
	@JsonProperty("instanceId")
	private String myInstanceId;
	@JsonProperty("data")
	private Map<String, Object> myData;

	/**
	 * Constructor
	 */
	public WorkChunk(@Nonnull String theId, @Nonnull String theJobDefinitionId, @Nonnull int theJobDefinitionVersion, @Nonnull String theTargetStepId, @Nonnull Map<String, Object> theData, @Nonnull StatusEnum theStatus, @Nonnull String theInstanceId) {
		Validate.notBlank(theJobDefinitionId);
		Validate.notBlank(theId);
		Validate.isTrue(theJobDefinitionVersion >= 1);
		Validate.notBlank(theTargetStepId);
		Validate.notNull(theData);
		myId = theId;
		myJobDefinitionId = theJobDefinitionId;
		myJobDefinitionVersion = theJobDefinitionVersion;
		myTargetStepId = theTargetStepId;
		myData = theData;
		myStatus = theStatus;
		myInstanceId = theInstanceId;
	}

	/**
	 * Constructor
	 */
	public WorkChunk() {
		super();
	}

	public StatusEnum getStatus() {
		return myStatus;
	}

	public void setStatus(StatusEnum theStatus) {
		myStatus = theStatus;
	}

	public String getJobDefinitionId() {
		return myJobDefinitionId;
	}

	public void setJobDefinitionId(String theJobDefinitionId) {
		myJobDefinitionId = theJobDefinitionId;
	}

	public int getJobDefinitionVersion() {
		return myJobDefinitionVersion;
	}

	public void setJobDefinitionVersion(int theJobDefinitionVersion) {
		myJobDefinitionVersion = theJobDefinitionVersion;
	}

	public String getTargetStepId() {
		return myTargetStepId;
	}

	public void setTargetStepId(String theTargetStepId) {
		myTargetStepId = theTargetStepId;
	}

	public Map<String, Object> getData() {
		return myData;
	}

	public void setData(Map<String, Object> theData) {
		myData = theData;
	}

	public String getInstanceId() {
		return myInstanceId;
	}

	public void setInstanceId(String theInstanceId) {
		myInstanceId = theInstanceId;
	}

	public String getId() {
		return myId;
	}
}
