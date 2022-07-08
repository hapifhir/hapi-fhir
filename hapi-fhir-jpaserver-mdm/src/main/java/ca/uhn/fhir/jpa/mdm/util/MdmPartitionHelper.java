package ca.uhn.fhir.jpa.mdm.util;

/*-
 * #%L
 * HAPI FHIR JPA Server - Master Data Management
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
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.mdm.util.MessageHelper;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MdmPartitionHelper {
	@Autowired
	MessageHelper myMessageHelper;

	public void validateResourcesInSamePartition(IAnyResource theFromResource, IAnyResource theToResource){
		RequestPartitionId fromGoldenResourcePartitionId = (RequestPartitionId) theFromResource.getUserData(Constants.RESOURCE_PARTITION_ID);
		RequestPartitionId toGoldenPartitionId = (RequestPartitionId) theToResource.getUserData(Constants.RESOURCE_PARTITION_ID);
		if (fromGoldenResourcePartitionId != null && toGoldenPartitionId != null && fromGoldenResourcePartitionId.hasPartitionIds() && toGoldenPartitionId.hasPartitionIds() &&
			!fromGoldenResourcePartitionId.hasPartitionId(toGoldenPartitionId.getFirstPartitionIdOrNull())) {
			throw new InvalidRequestException(Msg.code(2075) + myMessageHelper.getMessageForMismatchPartition(theFromResource, theToResource));
		}
	}
}
