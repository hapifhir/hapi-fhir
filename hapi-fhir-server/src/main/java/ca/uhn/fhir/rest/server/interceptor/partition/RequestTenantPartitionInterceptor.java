/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.rest.server.tenant.ITenantIdentificationStrategy;
import jakarta.annotation.Nonnull;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * This interceptor uses the request tenant ID (as supplied to the server using
 * {@link ca.uhn.fhir.rest.server.RestfulServer#setTenantIdentificationStrategy(ITenantIdentificationStrategy)}
 * to indicate the partition ID. With this interceptor registered, The server treats the tenant name
 * supplied by the {@link ITenantIdentificationStrategy tenant identification strategy} as a partition name.
 * <p>
 * Partition names (aka tenant IDs) must be registered in advance using the partition management operations.
 * </p>
 *
 * @since 5.0.0
 */
@Interceptor
public class RequestTenantPartitionInterceptor {

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_ANY)
	public RequestPartitionId partitionIdentifyCreate(RequestDetails theRequestDetails) {
		return extractPartitionIdFromRequest(theRequestDetails);
	}

	@Nonnull
	protected RequestPartitionId extractPartitionIdFromRequest(RequestDetails theRequestDetails) {

		// We will use the tenant ID that came from the request as the partition name
		String tenantId = theRequestDetails.getTenantId();
		if (isBlank(tenantId)) {
			// this branch is no-op happen when "partitioning.tenant_identification_strategy" is set to URL_BASED
			if (theRequestDetails instanceof SystemRequestDetails) {
				SystemRequestDetails requestDetails = (SystemRequestDetails) theRequestDetails;
				if (requestDetails.getRequestPartitionId() != null) {
					return requestDetails.getRequestPartitionId();
				}
				return RequestPartitionId.defaultPartition();
			}
			throw new InternalErrorException(Msg.code(343) + "No partition ID has been specified");
		}

		// for REQUEST_TENANT partition selection mode, allPartitions is supported when URL includes _ALL as the tenant
		// else if no tenant is provided in the URL, DEFAULT will be used as per UrlBaseTenantIdentificationStrategy
		if (tenantId.equals(ProviderConstants.ALL_PARTITIONS_TENANT_NAME)) {
			return RequestPartitionId.allPartitions();
		}
		return RequestPartitionId.fromPartitionName(tenantId);
	}
}
