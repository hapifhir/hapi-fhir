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
package ca.uhn.fhir.rest.server.interceptor.partition;

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

@Interceptor
public class RequestHeaderPartitionInterceptor {

	public static final String PARTITIONS_HEADER = "X-Request-Partition-IDs";

	@Hook(STORAGE_PARTITION_IDENTIFY_CREATE)
	public RequestPartitionId identifyPartitionForCreate(RequestDetails theRequestDetails) {
		String partitionHeader = getPartitionHeaderOrThrowIfBlank(theRequestDetails);
		return parseRequestPartitionIdsFromCommaSeparatedString(partitionHeader, true);
	}

	@Hook(STORAGE_PARTITION_IDENTIFY_READ)
	public RequestPartitionId identifyPartitionForRead(RequestDetails theRequestDetails) {
		String partitionHeader = getPartitionHeaderOrThrowIfBlank(theRequestDetails);
		return parseRequestPartitionIdsFromCommaSeparatedString(partitionHeader, false);
	}

	private String getPartitionHeaderOrThrowIfBlank(RequestDetails theRequestDetails) {
		String partitionHeader = theRequestDetails.getHeader(PARTITIONS_HEADER);
		if (isBlank(partitionHeader)) {
			// TODO EMRE add Msg.code
			String msg = String.format(
					"%s header is missing or blank, it is required to identify the storage partition",
					PARTITIONS_HEADER);
			throw new InvalidRequestException(msg);
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
							"Invalid partition ID: '%s' provided in header: %s",
							trimmedPartitionId, RequestHeaderPartitionInterceptor.PARTITIONS_HEADER);
					// TODO EMRE add Msg.code
					throw new InvalidRequestException(msg);
				}
			}

			// return early if we only need the first partition ID
			if (theIncludeOnlyTheFirst) {
				return RequestPartitionId.fromPartitionIds(partitionIds);
			}
		}

		RequestPartitionId requestPartitionId = RequestPartitionId.fromPartitionIds(partitionIds);

		return requestPartitionId;
	}
}
