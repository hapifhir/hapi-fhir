package ca.uhn.fhir.batch2.api;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.RequestDetails;

import java.util.List;

/**
 * Provides the list of partitions that a job should run against.
 * TODO MM: Consider moving UrlPartitioner calls to this class once other batch operations need to support running
 * across all partitions on a multitenant FHIR server.
 * That way all partitioning related logic exists only here for batch jobs.
 * After that PartitionedUrl#myRequestPartitionId can be marked as deprecated.
 */
public interface IJobPartitionProvider {
	/**
	 * Provides the list of partitions to run job steps against, based on the request that initiates the job.
	 * @param theRequestDetails the requestDetails
	 * @param theOperation the operation being run which corresponds to the job
	 * @return the list of partitions
	 */
	List<RequestPartitionId> getPartitions(RequestDetails theRequestDetails, String theOperation);

	// List<RequestPartitionId> getPartitions(RequestDetails theRequestDetails, String theOperation, String theUrls);
}
