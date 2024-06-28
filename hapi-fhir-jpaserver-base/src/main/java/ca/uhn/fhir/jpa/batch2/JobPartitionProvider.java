package ca.uhn.fhir.jpa.batch2;

import ca.uhn.fhir.batch2.api.IJobPartitionProvider;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.partition.IPartitionLookupSvc;
import ca.uhn.fhir.jpa.partition.IRequestPartitionHelperSvc;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The default implementation, which uses {@link IRequestPartitionHelperSvc} and {@link IPartitionLookupSvc} to compute the partition to run a batch2 job.
 * The ladder will be used to handle cases when the job is configured to run against all partitions (bulk system operation).
 */
public class JobPartitionProvider implements IJobPartitionProvider {
	private final IRequestPartitionHelperSvc myRequestPartitionHelperSvc;
	private final IPartitionLookupSvc myPartitionLookupSvc;

	public JobPartitionProvider(
			IRequestPartitionHelperSvc theRequestPartitionHelperSvc, IPartitionLookupSvc thePartitionLookupSvc) {
		myRequestPartitionHelperSvc = theRequestPartitionHelperSvc;
		myPartitionLookupSvc = thePartitionLookupSvc;
	}

	@Override
	public List<RequestPartitionId> getPartitions(RequestDetails theRequestDetails, String theOperation) {
		RequestPartitionId partitionId = myRequestPartitionHelperSvc.determineReadPartitionForRequestForServerOperation(
				theRequestDetails, theOperation);
		if (!partitionId.isAllPartitions()) {
			return List.of(partitionId);
		}
		// handle (bulk) system operations that are typically configured with RequestPartitionId.allPartitions()
		// populate the actual list of all partitions
		List<RequestPartitionId> partitionIdList = myPartitionLookupSvc.listPartitions().stream()
				.map(PartitionEntity::toRequestPartitionId)
				.collect(Collectors.toList());
		partitionIdList.add(RequestPartitionId.defaultPartition());
		return partitionIdList;
	}
}
