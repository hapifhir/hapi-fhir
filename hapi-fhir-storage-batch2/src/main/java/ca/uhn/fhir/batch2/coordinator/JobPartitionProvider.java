package ca.uhn.fhir.batch2.coordinator;

import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.List;

/**
 * The default implementation, which uses {@link IRequestPartitionHelperSvc} to compute the partition to run a batch2 job.
 */
public class JobPartitionProvider implements IJobPartitionProvider {
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;

	public JobPartitionProvider(IRequestPartitionHelperSvc theRequestPartitionHelperSvc) {
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
	}

	@Override
	public List<RequestPartitionId> getPartitions(RequestDetails theRequestDetails, String theOperation) {
		return List.of(myRequestPartitionHelperSvc.determineReadPartitionForRequestForServerOperation(
				theRequestDetails, theOperation));
	}
}
