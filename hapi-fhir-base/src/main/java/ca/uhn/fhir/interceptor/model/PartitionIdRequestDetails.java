package ca.uhn.fhir.interceptor.model;

public class PartitionIdRequestDetails {

	/**
	 * The currently provided request partition
	 */
	private RequestPartitionId myRequestPartitionId;

	public RequestPartitionId getRequestPartitionId() {
		return myRequestPartitionId;
	}

	public void setRequestPartitionId(RequestPartitionId theRequestPartitionId) {
		myRequestPartitionId = theRequestPartitionId;
	}
}
