package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class JobInstance implements IModelJson {

	@JsonProperty(value = "jobDefinitionId")
	private String myJobDefinitionId;

	@JsonProperty(value = "jobDefinitionVersion")
	private int myJobDefinitionVersion;

	@JsonProperty(value = "instanceId")
	private String myInstanceId;

	@JsonProperty(value = "status")
	private StatusEnum myStatus;

	@JsonProperty(value = "parameters")
	private List<JobInstanceParameter> myParameters;

	public List<JobInstanceParameter> getParameters() {
		if (myParameters == null) {
			myParameters = new ArrayList<>();
		}
		return myParameters;
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

	public String getInstanceId() {
		return myInstanceId;
	}

	public void setInstanceId(String theInstanceId) {
		myInstanceId = theInstanceId;
	}

	public void addParameter(@Nonnull JobInstanceParameter theParameter) {
		Validate.notNull(theParameter);
		getParameters().add(theParameter);
	}
}
