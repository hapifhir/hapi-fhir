/*-
 * #%L
 * HAPI FHIR Storage api
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
import ca.uhn.fhir.interceptor.model.IDefaultPartitionSettings;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.messaging.RequestPartitionHeaderUtil;

import java.util.function.BiFunction;

import static ca.uhn.fhir.interceptor.api.Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE;
import static ca.uhn.fhir.interceptor.api.Pointcut.STORAGE_PARTITION_IDENTIFY_READ;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This is an interceptor to identify the partition ID from a request header.
 * It reads the value of the X-Request-Partition-IDs header, which is expected to be a comma separated partition ids.
 * For the read operations it uses all the partitions specified in the header.
 * The create operations it uses the first partition ID from the header.
 * <p>
 * The tests for the functionality of this interceptor can be found in the
 * ca.uhn.fhir.jpa.interceptor.RequestHeaderPartitionTest class.
 */
@Interceptor
public class RequestHeaderPartitionInterceptor {
	private final IDefaultPartitionSettings myDefaultPartitionSettings;

	public RequestHeaderPartitionInterceptor(IDefaultPartitionSettings theDefaultPartitionSettings) {
		myDefaultPartitionSettings = theDefaultPartitionSettings;
	}

	/**
	 * Identifies the partition ID for create operations by parsing the first ID from the X-Request-Partition-IDs header.
	 */
	@Hook(STORAGE_PARTITION_IDENTIFY_CREATE)
	public RequestPartitionId identifyPartitionForCreate(RequestDetails theRequestDetails) {
		return identifyPartitionOrThrowException(theRequestDetails, RequestPartitionHeaderUtil::fromHeaderFirstPartitionOnly);
	}

	/**
	 * Identifies partition IDs for read operations by parsing all IDs from the X-Request-Partition-IDs header.
	 */
	@Hook(STORAGE_PARTITION_IDENTIFY_READ)
	public RequestPartitionId identifyPartitionForRead(RequestDetails theRequestDetails) {
		return identifyPartitionOrThrowException(theRequestDetails, RequestPartitionHeaderUtil::fromHeader);
	}

	/**
	 * Core logic to identify a request's storage partition. It retrieves the partition header,
	 * and if the header is blank for a system request, it returns the default partition.
	 * Otherwise, it uses the provided parsing function to interpret the header.
	 */
	private RequestPartitionId identifyPartitionOrThrowException(RequestDetails theRequestDetails, BiFunction<String, IDefaultPartitionSettings, RequestPartitionId> aHeaderParser) {
		String partitionHeader = theRequestDetails.getHeader(Constants.HEADER_X_REQUEST_PARTITION_IDS);

		if (isBlank(partitionHeader)) {
			if (theRequestDetails instanceof SystemRequestDetails) {
				return myDefaultPartitionSettings.getDefaultRequestPartitionId();
			}
			throw new InvalidRequestException(Msg.code(2642) + String.format(
				"%s header is missing or blank, it is required to identify the storage partition",
				Constants.HEADER_X_REQUEST_PARTITION_IDS));
		}

		return aHeaderParser.apply(partitionHeader, myDefaultPartitionSettings);
	}
}
