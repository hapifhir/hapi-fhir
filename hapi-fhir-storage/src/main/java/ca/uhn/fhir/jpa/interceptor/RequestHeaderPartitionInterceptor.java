/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.interceptor;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import java.util.ArrayList;
import java.util.List;

import static ca.uhn.fhir.interceptor.api.Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE;
import static ca.uhn.fhir.interceptor.api.Pointcut.STORAGE_PARTITION_IDENTIFY_READ;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.ALL_PARTITIONS_TENANT_NAME;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.DEFAULT_PARTITION_NAME;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This is an interceptor to identify the partition ID from a request header.
 * It reads the value of the X-Request-Partition-IDs header, which is expected to be a comma separated partition ids.
 * For the read operations it uses all the partitions specified in the header.
 * The create operations it uses the first partition ID from the header.
 *
 * The tests for the functionality of this interceptor can be found in the
 * ca.uhn.fhir.jpa.interceptor.RequestHeaderPartitionTest class.
 */
@Interceptor
public class RequestHeaderPartitionInterceptor {

	public static final String PARTITIONS_HEADER = "X-Request-Partition-IDs";

	/**
	 * This method is called to identify the partition ID for create operations.
	 * It reads the value of the X-Request-Partition-IDs header, and parses and returns the first partition ID
	 * from the header value.
	 */
	@Hook(STORAGE_PARTITION_IDENTIFY_CREATE)
	public RequestPartitionId identifyPartitionForCreate(RequestDetails theRequestDetails) {
		String partitionHeader = getPartitionHeaderOrThrowIfBlank(theRequestDetails);
		return parseRequestPartitionIdsFromCommaSeparatedString(partitionHeader, true);
	}

	/**
	 * This method is called to identify the partition ID for read operations.
	 * Parses all the partition IDs from the header into a RequestPartitionId object.
	 */
	@Hook(STORAGE_PARTITION_IDENTIFY_READ)
	public RequestPartitionId identifyPartitionForRead(RequestDetails theRequestDetails) {
		String partitionHeader = getPartitionHeaderOrThrowIfBlank(theRequestDetails);
		return parseRequestPartitionIdsFromCommaSeparatedString(partitionHeader, false);
	}

	private String getPartitionHeaderOrThrowIfBlank(RequestDetails theRequestDetails) {
		String partitionHeader = theRequestDetails.getHeader(PARTITIONS_HEADER);
		if (isBlank(partitionHeader)) {
			String msg = String.format(
					"%s header is missing or blank, it is required to identify the storage partition",
					PARTITIONS_HEADER);
			throw new InvalidRequestException(Msg.code(2642) + msg);
		}
		return partitionHeader;
	}

	private RequestPartitionId parseRequestPartitionIdsFromCommaSeparatedString(
			String thePartitionIds, boolean theIncludeOnlyTheFirst) {
		String[] partitionIdStrings = thePartitionIds.split(",");
		List<Integer> partitionIds = new ArrayList<>();

		for (String partitionIdString : partitionIdStrings) {

			String trimmedPartitionId = partitionIdString.trim();

			if (trimmedPartitionId.equals(ALL_PARTITIONS_TENANT_NAME)) {
				return RequestPartitionId.allPartitions();
			}

			if (trimmedPartitionId.equals(DEFAULT_PARTITION_NAME)) {
				partitionIds.add(RequestPartitionId.defaultPartition().getFirstPartitionIdOrNull());
			} else {
				try {
					int partitionId = Integer.parseInt(trimmedPartitionId);
					partitionIds.add(partitionId);
				} catch (NumberFormatException e) {
					String msg = String.format(
							"Invalid partition ID: '%s' provided in header: %s", trimmedPartitionId, PARTITIONS_HEADER);
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
			String msg = String.format("No partition IDs provided in header: %s", PARTITIONS_HEADER);
			throw new InvalidRequestException(Msg.code(2645) + msg);
		}

		return RequestPartitionId.fromPartitionIds(partitionIds);
	}
}
