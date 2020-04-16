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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.entity.PartitionEntity;
import ca.uhn.fhir.jpa.model.entity.PartitionablePartitionId;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

public class RequestTenantPartitionInterceptor {

	@Autowired
	private IPartitionConfigSvc myPartitionConfigSvc;
	@Autowired
	private FhirContext myFhirContext;

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_CREATE)
	public PartitionablePartitionId PartitionIdentifyCreate(IBaseResource theResource, ServletRequestDetails theRequestDetails) {
		return extractPartitionIdFromRequest(theRequestDetails);
	}

	@Hook(Pointcut.STORAGE_PARTITION_IDENTIFY_READ)
	public PartitionablePartitionId PartitionIdentifyRead(ServletRequestDetails theRequestDetails) {
		return extractPartitionIdFromRequest(theRequestDetails);
	}

	@NotNull
	private PartitionablePartitionId extractPartitionIdFromRequest(ServletRequestDetails theRequestDetails) {
		String tenantId = theRequestDetails.getTenantId();

		PartitionEntity partition;
		try {
			partition = myPartitionConfigSvc.getPartitionByName(tenantId);
		} catch (IllegalArgumentException e) {
			String msg = myFhirContext.getLocalizer().getMessageSanitized(RequestTenantPartitionInterceptor.class, "unknownTenantName", tenantId);
			throw new ResourceNotFoundException(msg);
		}

		PartitionablePartitionId retVal = new PartitionablePartitionId();
		retVal.setPartitionId(partition.getId());
		return retVal;
	}


}
