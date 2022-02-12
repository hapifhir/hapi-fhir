package ca.uhn.fhir.batch2.model;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class JobInstanceStartRequest implements IModelJson {

	@JsonProperty("jobDefinitionId")
	private String myJobDefinitionId;

	@JsonProperty("parameters")
	private List<JobInstanceParameter> myParameters;

	public String getJobDefinitionId() {
		return myJobDefinitionId;
	}

	public void setJobDefinitionId(String theJobDefinitionId) {
		myJobDefinitionId = theJobDefinitionId;
	}

	public List<JobInstanceParameter> getParameters() {
		if (myParameters == null) {
			myParameters = new ArrayList<>();
		}
		return myParameters;
	}

	public void addParameter(JobInstanceParameter theJobInstanceParameter) {
		getParameters().add(theJobInstanceParameter);
	}
}
