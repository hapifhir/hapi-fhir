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
import ca.uhn.fhir.mdm.model.MdmCreateOrUpdateParams;
import ca.uhn.fhir.mdm.model.MdmMergeGoldenResourcesParams;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.model.MdmUnduplicateGoldenResourceParams;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkJson;
import ca.uhn.fhir.mdm.model.mdmevents.MdmLinkWithRevisionJson;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import jakarta.annotation.Nullable;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.instance.model.api.IBaseParameters;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface IMdmControllerSvc {
	@Deprecated
	Page<MdmLinkJson> queryLinks(
			@Nullable String theGoldenResourceId,
			@Nullable String theSourceResourceId,
			@Nullable String theMatchResult,
			@Nullable String theLinkSource,
			MdmTransactionContext theMdmTransactionContext,
			MdmPageRequest thePageRequest);

	@Deprecated
	Page<MdmLinkJson> queryLinks(
			@Nullable String theGoldenResourceId,
			@Nullable String theSourceResourceId,
			@Nullable String theMatchResult,
			@Nullable String theLinkSource,
			MdmTransactionContext theMdmTransactionContext,
			MdmPageRequest thePageRequest,
			RequestDetails theRequestDetails);

	Page<MdmLinkJson> queryLinks(
			MdmQuerySearchParameters theMdmQuerySearchParameters,
			MdmTransactionContext theMdmTransactionContext,
			RequestDetails theRequestDetails);

	@Deprecated
	Page<MdmLinkJson> queryLinksFromPartitionList(
			@Nullable String theGoldenResourceId,
			@Nullable String theSourceResourceId,
			@Nullable String theMatchResult,
			@Nullable String theLinkSource,
			MdmTransactionContext theMdmTransactionContext,
			MdmPageRequest thePageRequest,
			List<Integer> thePartitionIds);

	Page<MdmLinkJson> queryLinksFromPartitionList(
			MdmQuerySearchParameters theMdmQuerySearchParameters, MdmTransactionContext theMdmTransactionContext);

	List<MdmLinkWithRevisionJson> queryLinkHistory(
			MdmHistorySearchParameters theMdmHistorySearchParameters, RequestDetails theRequestDetails);

	Page<MdmLinkJson> getDuplicateGoldenResources(
			MdmTransactionContext theMdmTransactionContext, MdmPageRequest thePageRequest);

	Page<MdmLinkJson> getDuplicateGoldenResources(
			MdmTransactionContext theMdmTransactionContext,
			MdmPageRequest thePageRequest,
			RequestDetails theRequestDetails,
			String theRequestResourceType);

	@Deprecated(forRemoval = true, since = "6.8.0")
	void notDuplicateGoldenResource(
			String theGoldenResourceId,
			String theTargetGoldenResourceId,
			MdmTransactionContext theMdmTransactionContext);

	default void unduplicateGoldenResource(MdmUnduplicateGoldenResourceParams theParams) {
		notDuplicateGoldenResource(
				theParams.getGoldenResourceId(), theParams.getTargetGoldenResourceId(), theParams.getMdmContext());
	}

	@Deprecated(forRemoval = true, since = "6.8.0")
	IAnyResource mergeGoldenResources(
			String theFromGoldenResourceId,
			String theToGoldenResourceId,
			IAnyResource theManuallyMergedGoldenResource,
			MdmTransactionContext theMdmTransactionContext);

	default IAnyResource mergeGoldenResources(MdmMergeGoldenResourcesParams theParams) {
		return mergeGoldenResources(
				theParams.getFromGoldenResourceId(),
				theParams.getToGoldenResourceId(),
				theParams.getManuallyMergedResource(),
				theParams.getMdmTransactionContext());
	}

	@Deprecated(forRemoval = true, since = "6.8.0")
	IAnyResource updateLink(
			String theGoldenResourceId,
			String theSourceResourceId,
			String theMatchResult,
			MdmTransactionContext theMdmTransactionContext);

	default IAnyResource updateLink(MdmCreateOrUpdateParams theParams) {
		String matchResult = theParams.getMatchResult() == null
				? null
				: theParams.getMatchResult().name();
		return updateLink(
				theParams.getGoldenResourceId(), theParams.getResourceId(), matchResult, theParams.getMdmContext());
	}

	@Deprecated(forRemoval = true, since = "6.8.0")
	IAnyResource createLink(
			String theGoldenResourceId,
			String theSourceResourceId,
			@Nullable String theMatchResult,
			MdmTransactionContext theMdmTransactionContext);

	default IAnyResource createLink(MdmCreateOrUpdateParams theParams) {
		String matchResult = theParams.getMatchResult() == null
				? null
				: theParams.getMatchResult().name();
		return createLink(
				theParams.getGoldenResourceId(), theParams.getResourceId(), matchResult, theParams.getMdmContext());
	}

	IBaseParameters submitMdmClearJob(
			List<String> theResourceNames,
			IPrimitiveType<BigDecimal> theBatchSize,
			ServletRequestDetails theRequestDetails);

	IBaseParameters submitMdmSubmitJob(
			List<String> theUrls, IPrimitiveType<BigDecimal> theBatchSize, ServletRequestDetails theRequestDetails);
}
