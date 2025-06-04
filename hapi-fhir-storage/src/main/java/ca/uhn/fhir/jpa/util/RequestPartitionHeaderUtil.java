package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.IDefaultPartitionSettings;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import jakarta.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static ca.uhn.fhir.rest.server.provider.ProviderConstants.ALL_PARTITIONS_TENANT_NAME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.DEFAULT_PARTITION_NAME;

public final class RequestPartitionHeaderUtil {
	private RequestPartitionHeaderUtil() {}

	@Nullable
	public static RequestPartitionId fromHeader(
			@Nullable String thePartitionHeaderValue,
			boolean theIncludeOnlyTheFirst,
			IDefaultPartitionSettings theDefaultPartitionSettings) {
		if (thePartitionHeaderValue == null) {
			return null;
		}
		String[] partitionIdStrings = thePartitionHeaderValue.split(",");
		List<Integer> partitionIds = new ArrayList<>();

		for (String partitionIdString : partitionIdStrings) {

			String trimmedPartitionId = partitionIdString.trim();

			if (trimmedPartitionId.equals(ALL_PARTITIONS_TENANT_NAME)) {
				return RequestPartitionId.allPartitions();
			}

			if (trimmedPartitionId.equals(DEFAULT_PARTITION_NAME)) {
				partitionIds.add(theDefaultPartitionSettings.getDefaultPartitionId());
			} else {
				try {
					int partitionId = Integer.parseInt(trimmedPartitionId);
					partitionIds.add(partitionId);
				} catch (NumberFormatException e) {
					String msg = String.format(
							"Invalid partition ID: '%s' provided in header: %s",
							trimmedPartitionId, Constants.HEADER_X_REQUEST_PARTITION_IDS);
					throw new InvalidRequestException(Msg.code(2643) + msg);
				}
			}

			// return early if we only need the first partition ID
			if (theIncludeOnlyTheFirst) {
				return RequestPartitionId.fromPartitionIds(partitionIds);
			}
		}

		if (partitionIds.isEmpty()) {
			// this case happens only when the header contains nothing but commas
			// since we already checked for blank header before calling this function
			String msg =
					String.format("No partition IDs provided in header: %s", Constants.HEADER_X_REQUEST_PARTITION_IDS);
			throw new InvalidRequestException(Msg.code(2645) + msg);
		}

		return RequestPartitionId.fromPartitionIds(partitionIds);
	}

	@Nullable
	public static RequestPartitionId fromHeader(
			@Nullable String thePartitionHeaderValue, IDefaultPartitionSettings theDefaultPartitionSettings) {
		return fromHeader(thePartitionHeaderValue, false, theDefaultPartitionSettings);
	}
}
