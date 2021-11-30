package ca.uhn.fhir.jpa.partition;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;

public class AllPartitionSystemRequestDetails extends SystemRequestDetails{
	public AllPartitionSystemRequestDetails(){
		super();
		this.setRequestPartitionId(RequestPartitionId.allPartitions());
	}
}
