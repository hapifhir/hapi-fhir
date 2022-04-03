package ca.uhn.fhir.rest.server.interceptor.partition;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.tenant.ITenantIdentificationStrategy;

import javax.annotation.Nonnull;

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

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
	public RequestPartitionId PartitionIdentifyCreate(RequestDetails theRequestDetails) {
		return extractPartitionIdFromRequest(theRequestDetails);
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
	public RequestPartitionId PartitionIdentifyRead(RequestDetails theRequestDetails) {
		return extractPartitionIdFromRequest(theRequestDetails);
	}

	@Nonnull
	protected RequestPartitionId extractPartitionIdFromRequest(RequestDetails theRequestDetails) {

		// We will use the tenant ID that came from the request as the partition name
		String tenantId = theRequestDetails.getTenantId();
		if (isBlank(tenantId)) {
			throw new InternalErrorException(Msg.code(343) + "No tenant ID has been specified");
		}

		return RequestPartitionId.fromPartitionName(tenantId);
	}


}
