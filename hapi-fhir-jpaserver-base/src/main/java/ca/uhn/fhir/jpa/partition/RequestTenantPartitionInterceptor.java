package ca.uhn.fhir.jpa.partition;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.interceptor.model.PartitionId;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.jetbrains.annotations.NotNull;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 *
 */
@Interceptor
public class RequestTenantPartitionInterceptor {

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
	public PartitionId PartitionIdentifyCreate(IBaseResource theResource, ServletRequestDetails theRequestDetails) {
		return extractPartitionIdFromRequest(theRequestDetails);
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
	public PartitionId PartitionIdentifyRead(ServletRequestDetails theRequestDetails) {
		return extractPartitionIdFromRequest(theRequestDetails);
	}

	@NotNull
	private PartitionId extractPartitionIdFromRequest(ServletRequestDetails theRequestDetails) {

		// We will use the tenant ID that came from the request as the partition name
		String tenantId = theRequestDetails.getTenantId();
		if (isBlank(tenantId)) {
			throw new InternalErrorException("No tenant ID has been specified");
		}

		return PartitionId.forPartitionName(tenantId);
	}


}
