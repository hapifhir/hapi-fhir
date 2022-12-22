package ca.uhn.fhir.mdm.api;

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

import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.provider.MdmControllerUtil;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IIdType;

import javax.annotation.Nullable;
import java.util.List;

public class MdmQuerySearchParameters {
	private IIdType myGoldenResourceId;
	private IIdType mySourceId;
	private MdmMatchResultEnum myMatchResult;
	private MdmLinkSourceEnum myLinkSource;
	private MdmPageRequest myPageRequest;
	private List<Integer> myPartitionIds;
	private String myResourceType;

	public MdmQuerySearchParameters() {}

	public MdmQuerySearchParameters(@Nullable String theGoldenResourceId, @Nullable String theSourceId, @Nullable String theMatchResult, @Nullable String theLinkSource, MdmPageRequest thePageRequest, @Nullable List<Integer> thePartitionIds, @Nullable String theResourceType) {
		setGoldenResourceId(theGoldenResourceId);
		setSourceId(theSourceId);
		setLinkSource(theLinkSource);
		setMatchResult(theMatchResult);
		setPartitionIds(thePartitionIds);
		setPageRequest(thePageRequest);
		setResourceType(theResourceType);
	}

	public MdmQuerySearchParameters(@Nullable IIdType theGoldenResourceId, @Nullable IIdType theSourceId, @Nullable MdmMatchResultEnum theMatchResult, @Nullable MdmLinkSourceEnum theLinkSource, MdmPageRequest thePageRequest, @Nullable List<Integer> thePartitionIds, @Nullable String theResourceType) {
		setGoldenResourceId(theGoldenResourceId);
		setSourceId(theSourceId);
		setLinkSource(theLinkSource);
		setMatchResult(theMatchResult);
		setPartitionIds(thePartitionIds);
		setPageRequest(thePageRequest);
		setResourceType(theResourceType);
	}

	public IIdType getGoldenResourceId() {
		return myGoldenResourceId;
	}

	public void setGoldenResourceId(IIdType theGoldenResourceId) {
		myGoldenResourceId = theGoldenResourceId;
	}

	public void setGoldenResourceId(String theGoldenResourceId) {
		IIdType goldenResourceId = MdmControllerUtil.extractGoldenResourceIdDtOrNull(ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, theGoldenResourceId);
		myGoldenResourceId = goldenResourceId;
	}

	public IIdType getSourceId() {
		return mySourceId;
	}

	public void setSourceId(IIdType theSourceId) {
		mySourceId = theSourceId;
	}

	public void setSourceId(String theSourceId) {
		IIdType sourceId = MdmControllerUtil.extractSourceIdDtOrNull(ProviderConstants.MDM_QUERY_LINKS_RESOURCE_ID, theSourceId);
		mySourceId = sourceId;
	}

	public MdmMatchResultEnum getMatchResult() {
		return myMatchResult;
	}

	public void setMatchResult(MdmMatchResultEnum theMatchResult) {
		myMatchResult = theMatchResult;
	}

	public void setMatchResult(String theMatchResult) {
		MdmMatchResultEnum matchResult = MdmControllerUtil.extractMatchResultOrNull(theMatchResult);
		myMatchResult = matchResult;
	}

	public MdmLinkSourceEnum getLinkSource() {
		return myLinkSource;
	}

	public void setLinkSource(MdmLinkSourceEnum theLinkSource) {
		myLinkSource = theLinkSource;
	}

	public void setLinkSource(String theLinkSource) {
		MdmLinkSourceEnum linkSource = MdmControllerUtil.extractLinkSourceOrNull(theLinkSource);
		myLinkSource = linkSource;
	}

	public MdmPageRequest getPageRequest() {
		return myPageRequest;
	}

	public void setPageRequest(MdmPageRequest thePageRequest) {
		myPageRequest = thePageRequest;
	}

	public List<Integer> getPartitionIds() {
		return myPartitionIds;
	}

	public void setPartitionIds(List<Integer> thePartitionIds) {
		myPartitionIds = thePartitionIds;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String theResourceType) {
		myResourceType = theResourceType;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myGoldenResourceId", myGoldenResourceId)
			.append("mySourceId", mySourceId)
			.append("myMatchResult", myMatchResult)
			.append("myLinkSource", myLinkSource)
			.append("myPartitionId", myPartitionIds)
			.append("myPageRequest", myPageRequest)
			.append("myResourceType", myResourceType)
			.toString();
	}

}
