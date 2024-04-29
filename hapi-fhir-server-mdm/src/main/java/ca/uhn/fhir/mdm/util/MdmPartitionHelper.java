/*-
 * #%L
 * HAPI FHIR - Master Data Management
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
package ca.uhn.fhir.mdm.util;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.mdm.api.IMdmSettings;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.springframework.stereotype.Service;

@Service
public class MdmPartitionHelper {
	private final MessageHelper myMessageHelper;
	private final IMdmSettings myMdmSettings;

	public MdmPartitionHelper(MessageHelper theMessageHelper, IMdmSettings theMdmSettings) {
		myMessageHelper = theMessageHelper;
		myMdmSettings = theMdmSettings;
	}

	/**
	 * Checks the partition of the two resources are in compliance with the settings
	 * If the mdm settings states mdm resources that only matches against resources in the same partition, validate
	 * the resources have the same partition
	 * This is used to check in merging golden resources as well as when creating a link between source and golden resource
	 * @param theFromResource
	 * @param theToResource
	 */
	public void validateMdmResourcesPartitionMatches(IAnyResource theFromResource, IAnyResource theToResource) {
		if (!myMdmSettings.getSearchAllPartitionForMatch()) {
			RequestPartitionId fromGoldenResourcePartitionId =
					(RequestPartitionId) theFromResource.getUserData(Constants.RESOURCE_PARTITION_ID);
			RequestPartitionId toGoldenPartitionId =
					(RequestPartitionId) theToResource.getUserData(Constants.RESOURCE_PARTITION_ID);
			if (fromGoldenResourcePartitionId != null
					&& toGoldenPartitionId != null
					&& fromGoldenResourcePartitionId.hasPartitionIds()
					&& toGoldenPartitionId.hasPartitionIds()
					&& !fromGoldenResourcePartitionId.hasPartitionId(toGoldenPartitionId.getFirstPartitionIdOrNull())) {
				throw new InvalidRequestException(Msg.code(2075)
						+ myMessageHelper.getMessageForMismatchPartition(theFromResource, theToResource));
			}
		}
	}

	/**
	 * Generates the request partition id for a mdm candidate search for a given resource.
	 * If the system is configured to search across all partition for matches, this will return all partition
	 * and if not, this function will return the request partition id of the source resource
	 * @param theResource
	 * @return The RequestPartitionId that should be used for the candidate search for the given resource
	 */
	public RequestPartitionId getRequestPartitionIdFromResourceForSearch(IAnyResource theResource) {
		if (myMdmSettings.getSearchAllPartitionForMatch()) {
			return RequestPartitionId.allPartitions();
		} else {
			return (RequestPartitionId) theResource.getUserData(Constants.RESOURCE_PARTITION_ID);
		}
	}

	public RequestPartitionId getRequestPartitionIdForNewGoldenResources(IAnyResource theSourceResource) {
		if (StringUtils.isBlank(myMdmSettings.getGoldenResourcePartitionName())) {
			return (RequestPartitionId) theSourceResource.getUserData(Constants.RESOURCE_PARTITION_ID);
		} else {
			return RequestPartitionId.fromPartitionName(myMdmSettings.getGoldenResourcePartitionName());
		}
	}
}
