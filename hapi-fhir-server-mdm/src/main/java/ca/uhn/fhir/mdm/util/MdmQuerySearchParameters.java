package ca.uhn.fhir.mdm.util;

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

import ca.uhn.fhir.mdm.api.MdmLinkSourceEnum;
import ca.uhn.fhir.mdm.api.MdmMatchResultEnum;
import ca.uhn.fhir.mdm.api.paging.MdmPageRequest;
import ca.uhn.fhir.mdm.provider.MdmControllerUtil;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.List;

public class MdmQuerySearchParameters {
	private IIdType myGoldenResourceId;
	private IIdType mySourceId;
	private MdmMatchResultEnum myMatchResult;
	private MdmLinkSourceEnum myLinkSource;
	private MdmPageRequest myPageRequest;
	private List<Integer> myPartitionId;
	private String myResourceType;

	private MdmQuerySearchParameters() {}

	public IIdType getGoldenResourceId() {
		return myGoldenResourceId;
	}

	public IIdType getSourceId() {
		return mySourceId;
	}

	public MdmMatchResultEnum getMatchResult() {
		return myMatchResult;
	}

	public MdmLinkSourceEnum getLinkSource() {
		return myLinkSource;
	}

	public MdmPageRequest getPageRequest() {
		return myPageRequest;
	}

	public List<Integer> getPartitionId() {
		return myPartitionId;
	}

	public String getResourceType() {
		return myResourceType;
	}

	/**
	 * Do not filter, return all mdm-query-links.
	 */
	public static MdmQuerySearchParameters all() {
		return new MdmQuerySearchParameters();
	}

	/**
	 * Builder is the only way to construct a set of search parameters for auditing.
	 */
	public static class Builder {
		private IIdType myGoldenResourceId;
		private IIdType mySourceId;
		private MdmMatchResultEnum myMatchResult;
		private MdmLinkSourceEnum myLinkSource;
		private MdmPageRequest myPageRequest;
		private List<Integer> myPartitionId;
		private String myResourceType;

		public Builder goldenResourceId(IIdType theGoldenResourceId) {
			myGoldenResourceId = theGoldenResourceId;
			return this;
		}

		public Builder goldenResourceId(String theGoldenResourceId) {
			IIdType goldenResourceId = MdmControllerUtil.extractGoldenResourceIdDtOrNull(ProviderConstants.MDM_QUERY_LINKS_GOLDEN_RESOURCE_ID, theGoldenResourceId);
			myGoldenResourceId = goldenResourceId;
			return this;
		}

		public Builder sourceId(IIdType theSourceId) {
			mySourceId = theSourceId;
			return this;
		}

		public Builder sourceId(String theSourceId) {
			IIdType sourceId = MdmControllerUtil.extractSourceIdDtOrNull(ProviderConstants.MDM_QUERY_LINKS_RESOURCE_ID, theSourceId);
			mySourceId = sourceId;
			return this;
		}

		public Builder matchResult(MdmMatchResultEnum theMatchResult) {
			myMatchResult = theMatchResult;
			return this;
		}

		public Builder matchResult(String theMatchResult) {
			MdmMatchResultEnum matchResult = MdmControllerUtil.extractMatchResultOrNull(theMatchResult);
			myMatchResult = matchResult;
			return this;
		}

		public Builder linkSource(MdmLinkSourceEnum theLinkSource) {
			myLinkSource = theLinkSource;
			return this;
		}

		public Builder linkSource(String theLinkSource) {
			MdmLinkSourceEnum linkSource = MdmControllerUtil.extractLinkSourceOrNull(theLinkSource);
			myLinkSource = linkSource;
			return this;
		}

		public Builder pageRequest(MdmPageRequest thePageRequest) {
			myPageRequest = thePageRequest;
			return this;
		}

		public Builder partitionId(List<Integer> thePartitionId) {
			myPartitionId = thePartitionId;
			return this;
		}

		public Builder resourceType(String theResourceType) {
			myResourceType = theResourceType;
			return this;
		}

		public MdmQuerySearchParameters build() {
			MdmQuerySearchParameters mdmQuerySearchParameters = new MdmQuerySearchParameters();
			mdmQuerySearchParameters.myGoldenResourceId = this.myGoldenResourceId;
			mdmQuerySearchParameters.mySourceId = this.mySourceId;
			mdmQuerySearchParameters.myLinkSource = this.myLinkSource;
			mdmQuerySearchParameters.myMatchResult = this.myMatchResult;
			mdmQuerySearchParameters.myPartitionId = this.myPartitionId;
			mdmQuerySearchParameters.myPageRequest = this.myPageRequest;
			mdmQuerySearchParameters.myResourceType = this.myResourceType;

			return mdmQuerySearchParameters;
		}
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
			.append("myGoldenResourceId", myGoldenResourceId)
			.append("mySourceId", mySourceId)
			.append("myMatchResult", myMatchResult)
			.append("myLinkSource", myLinkSource)
			.append("myPartitionId", myPartitionId)
			.append("myPageRequest", myPageRequest)
			.append("myResourceType", myResourceType)
			.toString();
	}

}
