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
package ca.uhn.fhir.mdm.api.params;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.provider.MdmControllerUtil;
import ca.uhn.fhir.rest.api.SortOrderEnum;
import ca.uhn.fhir.rest.api.SortSpec;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import jakarta.annotation.Nullable;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.hibernate.internal.util.StringHelper.isBlank;

public class MdmQuerySearchParameters {

	public static final String GOLDEN_RESOURCE_PID_NAME = "myGoldenResourcePid";
	public static final String SOURCE_PID_NAME = "mySourcePid";
	public static final String MATCH_RESULT_NAME = "myMatchResult";
	public static final String LINK_SOURCE_NAME = "myLinkSource";
	public static final String PARTITION_ID_NAME = "myPartitionId";
	public static final String GOLDEN_RESOURCE_NAME = "myGoldenResource";
	public static final String RESOURCE_TYPE_NAME = "myResourceType";
	public static final String CREATED_NAME = "myCreated";
	public static final String UPDATED_NAME = "myUpdated";
	public static final String SCORE_NAME = "myScore";

	public static final Set<String> ourValidSortParameters = Set.of(
			GOLDEN_RESOURCE_PID_NAME,
			SOURCE_PID_NAME,
			MATCH_RESULT_NAME,
			LINK_SOURCE_NAME,
			PARTITION_ID_NAME,
			GOLDEN_RESOURCE_NAME,
			RESOURCE_TYPE_NAME,
			CREATED_NAME,
			UPDATED_NAME,
			SCORE_NAME);

	private IIdType myGoldenResourceId;
	private IIdType mySourceId;
	private MdmMatchResultEnum myMatchResult;
	private MdmLinkSourceEnum myLinkSource;
	private MdmPageRequest myPageRequest;
	private List<Integer> myPartitionIds;
	private String myResourceType;
	private final List<SortSpec> mySort = new ArrayList<>();

	@Deprecated(since = "2023.02.R01", forRemoval = true)
	public MdmQuerySearchParameters() {}

	public MdmQuerySearchParameters(MdmPageRequest thePageRequest) {
		setPageRequest(thePageRequest);
	}

	@Deprecated(since = "2023.02.R01", forRemoval = true)
	public MdmQuerySearchParameters(
			@Nullable String theGoldenResourceId,
			@Nullable String theSourceId,
			@Nullable String theMatchResult,
			@Nullable String theLinkSource,
			MdmPageRequest thePageRequest,
			@Nullable List<Integer> thePartitionIds,
			@Nullable String theResourceType) {
		setGoldenResourceId(theGoldenResourceId);
		setSourceId(theSourceId);
		setLinkSource(theLinkSource);
		setMatchResult(theMatchResult);
		setPartitionIds(thePartitionIds);
		setPageRequest(thePageRequest);
		setResourceType(theResourceType);
	}

	@Deprecated(since = "2023.02.R01", forRemoval = true)
	public MdmQuerySearchParameters(
			@Nullable IIdType theGoldenResourceId,
			@Nullable IIdType theSourceId,
			@Nullable MdmMatchResultEnum theMatchResult,
			@Nullable MdmLinkSourceEnum theLinkSource,
			MdmPageRequest thePageRequest,
			@Nullable List<Integer> thePartitionIds,
			@Nullable String theResourceType) {
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

	public MdmQuerySearchParameters setGoldenResourceId(IIdType theGoldenResourceId) {
		myGoldenResourceId = theGoldenResourceId;
		return this;
	}

	public MdmQuerySearchParameters setGoldenResourceId(String theGoldenResourceId) {
		myGoldenResourceId = MdmControllerUtil.extractGoldenResourceIdDtOrNull(
				ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, theGoldenResourceId);
		return this;
	}

	public IIdType getSourceId() {
		return mySourceId;
	}

	public MdmQuerySearchParameters setSourceId(IIdType theSourceId) {
		mySourceId = theSourceId;
		return this;
	}

	public MdmQuerySearchParameters setSourceId(String theSourceId) {
		mySourceId =
				MdmControllerUtil.extractSourceIdDtOrNull(ProviderConstants.MDM_QUERY_LINKS_RESOURCE_ID, theSourceId);
		return this;
	}

	public MdmMatchResultEnum getMatchResult() {
		return myMatchResult;
	}

	public MdmQuerySearchParameters setMatchResult(MdmMatchResultEnum theMatchResult) {
		myMatchResult = theMatchResult;
		return this;
	}

	public MdmQuerySearchParameters setMatchResult(String theMatchResult) {
		myMatchResult = MdmControllerUtil.extractMatchResultOrNull(theMatchResult);
		return this;
	}

	public MdmLinkSourceEnum getLinkSource() {
		return myLinkSource;
	}

	public MdmQuerySearchParameters setLinkSource(MdmLinkSourceEnum theLinkSource) {
		myLinkSource = theLinkSource;
		return this;
	}

	public MdmQuerySearchParameters setLinkSource(String theLinkSource) {
		myLinkSource = MdmControllerUtil.extractLinkSourceOrNull(theLinkSource);
		return this;
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

	public MdmQuerySearchParameters setPartitionIds(List<Integer> thePartitionIds) {
		myPartitionIds = thePartitionIds;
		return this;
	}

	public String getResourceType() {
		return myResourceType;
	}

	public MdmQuerySearchParameters setResourceType(String theResourceType) {
		myResourceType = theResourceType;
		return this;
	}

	public List<SortSpec> getSort() {
		return mySort;
	}

	public MdmQuerySearchParameters setSort(String theSortString) {
		if (isBlank(theSortString)) {
			return this;
		}

		for (String param : theSortString.split(",")) {
			String p = (param.startsWith("-") ? param.substring(1) : param).trim();
			if (!MdmQuerySearchParameters.ourValidSortParameters.contains(p)) {
				throw new InvalidRequestException(
						Msg.code(2233) + "Unrecognized sort parameter: " + p + ". Valid parameters are: "
								+ String.join(", ", MdmQuerySearchParameters.ourValidSortParameters));
			}
			SortOrderEnum order = param.startsWith("-") ? SortOrderEnum.DESC : SortOrderEnum.ASC;
			mySort.add(new SortSpec(p, order));
		}
		return this;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(GOLDEN_RESOURCE_PID_NAME, myGoldenResourceId)
				.append(SOURCE_PID_NAME, mySourceId)
				.append(MATCH_RESULT_NAME, myMatchResult)
				.append(LINK_SOURCE_NAME, myLinkSource)
				.append(PARTITION_ID_NAME, myPartitionIds)
				.append(RESOURCE_TYPE_NAME, myResourceType)
				.append("myPageRequest", myPageRequest)
				.toString();
	}
}
