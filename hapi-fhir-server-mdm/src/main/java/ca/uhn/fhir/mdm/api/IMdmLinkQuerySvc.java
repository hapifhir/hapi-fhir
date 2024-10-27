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
package ca.uhn.fhir.mdm.api;

import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.api.params.MdmHistorySearchParameters;
import ca.uhn.fhir.mdm.api.params.MdmQuerySearchParameters;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkJson;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkWithRevisionJson;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * This service supports the MDM operation providers for those services that return multiple MDM links.
 */
public interface IMdmLinkQuerySvc {
	@Deprecated
	Page<MdmLinkJson> queryLinks(
			IIdType theGoldenResourceId,
			IIdType theSourceResourceId,
			MdmMatchResultEnum theMatchResult,
			MdmLinkSourceEnum theLinkSource,
			MdmTransactionContext theMdmContext,
			MdmPageRequest thePageRequest);

	@Deprecated
	Page<MdmLinkJson> queryLinks(
			IIdType theGoldenResourceId,
			IIdType theSourceResourceId,
			MdmMatchResultEnum theMatchResult,
			MdmLinkSourceEnum theLinkSource,
			MdmTransactionContext theMdmContext,
			MdmPageRequest thePageRequest,
			List<Integer> thePartitionId);

	Page<MdmLinkJson> queryLinks(
			MdmQuerySearchParameters theMdmQuerySearchParameters, MdmTransactionContext theMdmContext);

	Page<MdmLinkJson> getDuplicateGoldenResources(MdmTransactionContext theMdmContext, MdmPageRequest thePageRequest);

	Page<MdmLinkJson> getDuplicateGoldenResources(
			MdmTransactionContext theMdmContext,
			MdmPageRequest thePageRequest,
			List<Integer> thePartitionId,
			String theRequestResourceType);

	List<MdmLinkWithRevisionJson> queryLinkHistory(MdmHistorySearchParameters theMdmHistorySearchParameters);
}
