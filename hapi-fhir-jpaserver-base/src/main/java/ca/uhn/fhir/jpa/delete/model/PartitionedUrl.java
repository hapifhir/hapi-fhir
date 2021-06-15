package ca.uhn.fhir.jpa.delete.model;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PartitionedUrl implements IModelJson {
	@JsonProperty("url")
	private String myUrl;

	@JsonProperty("requestPartitionId")
	private RequestPartitionId myRequestPartitionId;

	public PartitionedUrl() {
	}

	public PartitionedUrl(String theUrl, RequestPartitionId theRequestPartitionId) {
		myUrl = theUrl;
		myRequestPartitionId = theRequestPartitionId;
	}

	public String getUrl() {
		return myUrl;
	}

	public void setUrl(String theUrl) {
		myUrl = theUrl;
	}

	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}

	public void setRequestPartitionId(RequestPartitionId theRequestPartitionId) {
		myRequestPartitionId = theRequestPartitionId;
	}
}
