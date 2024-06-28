package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.List;

public class JobPartitionProvider implements IJobPartitionProvider {
	protected final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	public JobPartitionProvider(IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
	}

	@Override
	public List<RequestPartitionId> getPartitions(RequestDetails theRequestDetails, String theOperation) {
		RequestPartitionId partitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequestForServerOperation(
				theRequestDetails, theOperation);
		return List.of(partitionId);
	}
}
