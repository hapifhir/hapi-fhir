package ca.uhn.fhir.rest.server.interceptor.partition;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import java.util.ArrayList;
import java.util.List;

import static ca.uhn.fhir.interceptor.api.Pointcut.STORAGE_PARTITION_IDENTIFY_ANY;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.ALL_PARTITIONS_TENANT_NAME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.DEFAULT_PARTITION_NAME;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Interceptor
public class RequestHeaderPartitionInterceptor {

	public static final String PARTITIONS_HEADER = "X-Request-Partition-IDs";

	// In BaseTransactionProcessor I need to create a SubRequestDetails for the create path

	@Hook(STORAGE_PARTITION_IDENTIFY_ANY)
	public RequestPartitionId identifyPartitionForRead(RequestDetails theRequestDetails) {
		String partitionHeader = getPartitionHeaderOrThrowIfBlank(theRequestDetails);
		return parseRequestPartitionIdsFromCommaSeparatedString(partitionHeader);
	}

	private String getPartitionHeaderOrThrowIfBlank(RequestDetails theRequestDetails) {
		String partitionHeader = theRequestDetails.getHeader(PARTITIONS_HEADER);
		if (isBlank(partitionHeader)) {
			// TODO EMRE: What is the behavior for when the header is missing?
			// throw an exception for now
			// TODO EMRE add Msg.code
			String msg = String.format(
					"%s header is missing, it is required to identify the storage partition", PARTITIONS_HEADER);
			throw new InvalidRequestException(msg);
		}
		return partitionHeader;
	}

	private RequestPartitionId parseRequestPartitionIdsFromCommaSeparatedString(String thePartitionIds) {
		String[] partitionIdStrings = thePartitionIds.split(",");
		List<Integer> partitionIds = new ArrayList<>();
		for (String partitionIdString : partitionIdStrings) {
			String trimmedPartitionId = partitionIdString.trim();
			if (trimmedPartitionId.equals(ALL_PARTITIONS_TENANT_NAME)) {
				return RequestPartitionId.allPartitions();
			}
			if (trimmedPartitionId.equals(DEFAULT_PARTITION_NAME)) {
				partitionIds.add(RequestPartitionId.defaultPartition().getFirstPartitionIdOrNull());
				continue;
			}
			try {
				int partitionId = Integer.parseInt(trimmedPartitionId);
				partitionIds.add(partitionId);
			} catch (NumberFormatException e) {
				String msg = String.format(
						"Invalid partition ID: '%s' provided in header: %s",
						trimmedPartitionId, RequestHeaderPartitionInterceptor.PARTITIONS_HEADER);
				// TODO EMRE add Msg.code
				throw new InvalidRequestException(msg);
			}
		}

		if (partitionIds.isEmpty()) {
			String msg = String.format(
					"No valid partition IDs provided in header: %s",
					RequestHeaderPartitionInterceptor.PARTITIONS_HEADER);
			// TODO EMRE add Msg.code
			throw new InvalidRequestException(msg);
		}

		// we found some numerical partition IDs in the header
		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionIds(partitionIds);

		return requestPartitionId;
	}
}
